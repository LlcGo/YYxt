package com.lc.yyxt.hosp.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.lc.BookingScheduleRuleVo;
import com.lc.jyxt.common.exception.YyghException;
import com.lc.jyxt.common.result.ResultCodeEnum;
import com.lc.yygh.model.hosp.Department;
import com.lc.yygh.model.hosp.Schedule;
import com.lc.yyxt.hosp.repostiory.ScheduleRepository;
import com.lc.yyxt.hosp.service.HospitalService;
import com.lc.yyxt.hosp.service.ScheduleService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Lc
 * @Date 2023/7/5
 * @Description
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Resource
    private ScheduleRepository scheduleRepository;

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private HospitalService hospitalService;

    @Override
    public Page<Schedule> schedulePage(Map<String, Object> param) {
        //必须参数校验 略
        String hoscode = (String)param.get("hoscode");
        if(StringUtils.isEmpty(hoscode)){
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        int page = StringUtils.isEmpty(param.get("page")) ? 1 : Integer.parseInt((String)param.get("page"));
        int limit = StringUtils.isEmpty(param.get("limit")) ? 10 : Integer.parseInt((String)param.get("limit"));
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //0为第一页
        Pageable pageable = PageRequest.of(page-1, limit, sort);

        Schedule schedule = new Schedule();
        schedule.setHoscode(hoscode);
        schedule.setIsDeleted(0);

        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小
        //创建实例

        Example<Schedule> example = Example.of(schedule, matcher);
        return scheduleRepository.findAll(example, pageable);
    }

    @Override
    public void remove(Map<String, Object> param) {
        String hoscode = (String)param.get("hoscode");
        String hosScheduleId = (String)param.get("hosScheduleId");
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if(schedule != null){
            scheduleRepository.deleteById(schedule.getId());
        }
    }

    @Override
    public void saveSchedule(Map<String, Object> param) {
        String jsonString = JSON.toJSONString(param);
        Schedule schedule = JSON.parseObject(jsonString, Schedule.class);
        Schedule querySchedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(),schedule.getHosScheduleId());
        if(querySchedule != null){
            querySchedule.setIsDeleted(0);
            querySchedule.setUpdateTime(new Date());
            querySchedule.setStatus(1);
            scheduleRepository.save(querySchedule);
        }else {
            schedule.setIsDeleted(0);
            schedule.setUpdateTime(new Date());
            schedule.setCreateTime(new Date());
            schedule.setStatus(1);
            scheduleRepository.save(schedule);
        }
    }

    @Override
    public Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode) {
        //1 根据医院编号 和 科室编号 查询
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);

        //2 根据工作日workDate期进行分组
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),//匹配条件
                Aggregation.group("workDate")//分组字段
                        .first("workDate").as("workDate")
                        //3 统计号源数量
                        .count().as("docCount")
                        .sum("reservedNumber").as("reservedNumber")
                        .sum("availableNumber").as("availableNumber"),
                //排序
                Aggregation.sort(Sort.Direction.DESC,"workDate"),
                //4 实现分页
                Aggregation.skip((page-1)*limit),
                Aggregation.limit(limit)
        );
        //调用方法，最终执行
        AggregationResults<BookingScheduleRuleVo> aggResults =
                mongoTemplate.aggregate(agg, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = aggResults.getMappedResults();

        //分组查询的总记录数
        Aggregation totalAgg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );
        AggregationResults<BookingScheduleRuleVo> totalAggResults =
                mongoTemplate.aggregate(totalAgg,
                        Schedule.class, BookingScheduleRuleVo.class);
        int total = totalAggResults.getMappedResults().size();

        //把日期对应星期获取
        for(BookingScheduleRuleVo bookingScheduleRuleVo:bookingScheduleRuleVoList) {
            Date workDate = bookingScheduleRuleVo.getWorkDate();
            String dayOfWeek = this.getDayOfWeek(new DateTime(workDate));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);
        }

        //设置最终数据，进行返回
        Map<String, Object> result = new HashMap<>();
        result.put("bookingScheduleRuleList",bookingScheduleRuleVoList);
        result.put("total",total);

        //获取医院名称
        String hosName = hospitalService.getHospName(hoscode);
        //其他基础数据
        Map<String, String> baseMap = new HashMap<>();
        baseMap.put("hosname",hosName);
        result.put("baseMap",baseMap);

        return result;
    }

    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
            default:
                break;
        }
        return dayOfWeek;
    }
}
