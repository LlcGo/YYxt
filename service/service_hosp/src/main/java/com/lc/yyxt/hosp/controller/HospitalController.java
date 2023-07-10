package com.lc.yyxt.hosp.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.lc.HospitalQueryVo;
import com.lc.jyxt.common.exception.YyghException;
import com.lc.jyxt.common.result.Result;
import com.lc.jyxt.common.result.ResultCodeEnum;
import com.lc.yygh.model.hosp.Hospital;
import com.lc.yyxt.hosp.service.HospitalService;
import com.lc.yyxt.hosp.service.HospitalSetService;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author Lc
 * @Date 2023/7/5
 * @Description
 */
@RestController
@RequestMapping("/admin/hosp/hospital")
@CrossOrigin
public class HospitalController {

    @Resource
    private HospitalService hospitalService;

    @GetMapping("/page/{pageSize}/{pageNum}")
    private Result<Page<Hospital>> hospitalPage(@PathVariable Integer pageSize,
                                                @PathVariable Integer pageNum,
                                                HospitalQueryVo hospitalQueryVo){
       return Result.ok(hospitalService.selectPage(pageSize,pageNum,hospitalQueryVo));

    }

    @GetMapping("/update/hospital/{id}/{status}")
    private Result<String> updateHospital(@PathVariable String id,
                                 @PathVariable Integer status){
        if(StringUtils.isBlank(id)){
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        if(status == null || status < 0){
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
       hospitalService.updateHosptalById(id,status);
        return Result.ok();
    }

    @GetMapping("/show/hospital/{id}")
    private Result<Map<String,Object>> showHospital(@PathVariable String id){
        if(StringUtils.isBlank(id)){
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        return Result.ok(hospitalService.getHospitalById(id));
    }
}
