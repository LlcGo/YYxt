package com.lc.yyxt.hosp.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.lc.jyxt.common.exception.YyghException;
import com.lc.jyxt.common.result.ResultCodeEnum;
import com.lc.yygh.model.hosp.Schedule;
import com.lc.yyxt.hosp.repostiory.ScheduleRepository;
import com.lc.yyxt.hosp.service.ScheduleService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
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
}
