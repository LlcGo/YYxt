package com.lc.yyxt.hosp.service;

import com.lc.yygh.model.hosp.Schedule;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ScheduleService {
    /**
     * 分页查询科室排班
     * @param request
     * @return
     */
    Page<Schedule> schedulePage(Map<String, Object> request);

    /**
     * 删除科室排班
     * @param param
     */
    void remove(Map<String, Object> param);

    /**
     * 保存科室排班
     * @param param
     */
    void saveSchedule(Map<String, Object> param);

    /**
     * 根据医院编号 和 科室编号 ，查询排班规则数据
     * @param page
     * @param limit
     * @param hoscode
     * @param depcode
     * @return
     */
    Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode);
}
