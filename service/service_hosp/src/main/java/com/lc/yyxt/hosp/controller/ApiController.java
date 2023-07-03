package com.lc.yyxt.hosp.controller;

import com.lc.jyxt.common.result.Result;
import com.lc.yyxt.hosp.helper.HttpRequestHelper;
import com.lc.yyxt.hosp.service.HospitalService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author Lc
 * @Date 2023/7/3
 * @Description
 */
@Api(tags = "医院管理Api接口")
@RestController
@RequestMapping("/api/hosp")
public class ApiController {

    @Resource
    private HospitalService hospitalService;

    @PostMapping("/saveHospital")
    public Result saveHospital(HttpServletRequest request){
        Map<String, String[]> requesMap = request.getParameterMap();
        Map<String, Object> param = HttpRequestHelper.switchMap(requesMap);
        hospitalService.save(param);
        return Result.ok();
    }
}
