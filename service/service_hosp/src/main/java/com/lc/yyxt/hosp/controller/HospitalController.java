package com.lc.yyxt.hosp.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.lc.HospitalQueryVo;
import com.lc.jyxt.common.result.Result;
import com.lc.yygh.model.hosp.Hospital;
import com.lc.yyxt.hosp.service.HospitalService;
import com.lc.yyxt.hosp.service.HospitalSetService;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author Lc
 * @Date 2023/7/5
 * @Description
 */
@RestController
@RequestMapping("/admin/hosp/hospital")
public class HospitalController {

    @Resource
    private HospitalService hospitalService;

    @GetMapping("/page/{pageSize}/{pageNum}")
    private Result<Page<Hospital>> hospitalPage(@PathVariable Integer pageSize,
                                                @PathVariable Integer pageNum,
                                                HospitalQueryVo hospitalQueryVo){
       return Result.ok(hospitalService.selectPage(pageSize,pageNum,hospitalQueryVo));

    }
}
