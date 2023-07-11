package com.lc.yyxt.hosp.controller;

import com.lc.DepartmentVo;
import com.lc.jyxt.common.result.Result;
import com.lc.yyxt.hosp.service.DepartmentService;
import com.lc.yyxt.hosp.service.ScheduleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/hosp/department")
@CrossOrigin
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Resource
    private ScheduleService scheduleService;

    //根据医院编号，查询医院所有科室列表
    @ApiOperation(value = "查询医院所有科室列表")
    @GetMapping("/getDeptList/{hoscode}")
    public Result getDeptList(@PathVariable String hoscode) {
        List<DepartmentVo> list = departmentService.findDeptTree(hoscode);
        return Result.ok(list);
    }

    //根据医院编号 和 科室编号 ，查询排班规则数据
    @ApiOperation(value ="查询排班规则数据")
    @GetMapping("/getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(@PathVariable long page,
                                  @PathVariable long limit,
                                  @PathVariable String hoscode,
                                  @PathVariable String depcode) {
        Map<String,Object> map
                = scheduleService.getRuleSchedule(page,limit,hoscode,depcode);
        return Result.ok(map);
    }
}