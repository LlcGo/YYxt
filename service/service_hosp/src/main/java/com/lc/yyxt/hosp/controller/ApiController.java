package com.lc.yyxt.hosp.controller;

import com.lc.jyxt.common.exception.YyghException;
import com.lc.jyxt.common.result.Result;
import com.lc.jyxt.common.result.ResultCodeEnum;
import com.lc.yygh.model.hosp.Department;
import com.lc.yygh.model.hosp.Hospital;
import com.lc.yygh.model.hosp.Schedule;
import com.lc.yyxt.hosp.helper.HttpRequestHelper;
import com.lc.yyxt.hosp.service.DepartmentService;
import com.lc.yyxt.hosp.service.HospitalService;
import com.lc.yyxt.hosp.service.HospitalSetService;
import com.lc.yyxt.hosp.service.ScheduleService;
import com.lc.yyxt.hosp.utils.MD5;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author Lc
 * @Date 2023/7/3
 * @Description
 */
@Slf4j
@Api(tags = "医院管理Api接口")
@RestController
@RequestMapping("/api/hosp")
public class ApiController {

    @Resource
    private HospitalService hospitalService;

    @Resource
    private HospitalSetService hospitalSetService;

    @Resource
    private ScheduleService scheduleService;

    @Resource
    private DepartmentService departmentService;

    @PostMapping("/hospital/show")
    public Result<Hospital> getHospital(HttpServletRequest request) {
        Map<String, String[]> requesMap = request.getParameterMap();
        Map<String, Object> param = HttpRequestHelper.switchMap(requesMap);
        checkSignKey(param);
        String hoscode = (String) param.get("hoscode");
        Hospital hospital = hospitalService.getHospital(hoscode);
        return Result.ok(hospital);
    }

    @PostMapping("/saveHospital")
    public Result saveHospital(HttpServletRequest request) {
        Map<String, String[]> requesMap = request.getParameterMap();
        Map<String, Object> param = HttpRequestHelper.switchMap(requesMap);
        checkSignKey(param);
        //改变图片的格式base64 讲 “ ” 改为 “+”
        String logoData = (String) param.get("logoData");
        String newLogoDate = logoData.replaceAll(" ", "+");
        param.put("logoData", newLogoDate);
        hospitalService.save(param);
        return Result.ok();
    }

    //进行校验根据（医院管理接口传过来的唯一标识来进行判断sign_key）
    private void checkSignKey(Map<String, Object> param) {
        String sign_key = (String) param.get("sign");
        log.info("sign_key = " + sign_key);
        String hoscode = (String) param.get("hoscode");
        String sqlSign_key = hospitalSetService.getSignKeyByHsopitalCode(hoscode);
        //fae0b27c451c728867a567e8c1bb4e53
        String encrypt = MD5.encrypt(sqlSign_key);
        if (!sign_key.equals(encrypt)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
    }

    @PostMapping("/saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        Map<String, String[]> requesMap = request.getParameterMap();
        Map<String, Object> param = HttpRequestHelper.switchMap(requesMap);
        checkSignKey(param);
        boolean res = departmentService.saveDepartment(param);
        if (!res) {
            throw new YyghException(ResultCodeEnum.SERVICE_ERROR);
        }
        return Result.ok();
    }

    //查询所有的科室接口
    @PostMapping("/department/list")
    public Result<Page<Department>> list(HttpServletRequest request) {
        Map<String, String[]> requesMap = request.getParameterMap();
        Map<String, Object> param = HttpRequestHelper.switchMap(requesMap);
        checkSignKey(param);
        Page<Department> departmentList = departmentService.findDepartment(param);
        return Result.ok(departmentList);
    }

    @PostMapping("/department/remove")
    public Result remove(HttpServletRequest request) {
        Map<String, String[]> requesMap = request.getParameterMap();
        Map<String, Object> param = HttpRequestHelper.switchMap(requesMap);
        checkSignKey(param);
        Boolean remove = departmentService.remove(param);
        if (!remove) {
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        return Result.ok();
    }

    @PostMapping("/schedule/list")
    public Result<Page<Schedule>> scheduleList(HttpServletRequest request) {
        Map<String, String[]> requesMap = request.getParameterMap();
        Map<String, Object> param = HttpRequestHelper.switchMap(requesMap);
        checkSignKey(param);
        Page<Schedule> schedulePage = scheduleService.schedulePage(param);
        return Result.ok(schedulePage);
    }

    @PostMapping("/schedule/remove")
    public Result scheduleRemove(HttpServletRequest request) {
        Map<String, String[]> requesMap = request.getParameterMap();
        Map<String, Object> param = HttpRequestHelper.switchMap(requesMap);
        checkSignKey(param);
        scheduleService.remove(param);
        return Result.ok();
    }

    @PostMapping("/saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {
        Map<String, String[]> requesMap = request.getParameterMap();
        Map<String, Object> param = HttpRequestHelper.switchMap(requesMap);
        checkSignKey(param);
        scheduleService.saveSchedule(param);
        return Result.ok();
    }
}
