package com.lc.yyxt.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lc.HospitalQueryVo;
import com.lc.HospitalSetQueryVo;
import com.lc.jyxt.common.exception.YyghException;
import com.lc.jyxt.common.result.Result;
import com.lc.jyxt.common.result.ResultCodeEnum;
import com.lc.yygh.model.hosp.HospitalSet;
import com.lc.yyxt.common.utils.MD5;
import com.lc.yyxt.hosp.service.HospitalSetService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * @Author Lc
 * @Date 2023/6/18
 * @Description
 */
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
@CrossOrigin
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

    /**
     * 查询所有入驻医院
     * @return 所有医院信息
     */
    @GetMapping("/findAll")
    public Result<List<HospitalSet>> findAllHospitalSet() {
        return  Result.ok(hospitalSetService.list());
    }

    /**
     * 根据入驻医院id来删除信息（逻辑删除）
     * @param id 入驻医院id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> deleteHospitalSetById(@PathVariable("id")Long id){
        boolean flag = hospitalSetService.removeById(id);
        if(flag){
            return Result.ok();
        }else {
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
    }

    /**
     * 条件查询带分页
     * @param current 当前页
     * @param limit  一页几行
     * @param hospitalSetQueryVo 封装前端传来的数据 hoscode  and hosname
     * @return
     */
    @PostMapping("/findPageHospSet/{current}/{limit}")
    public Result<Page<HospitalSet>> Page(@PathVariable("current") Integer current,
                                          @PathVariable("limit") Integer limit,
                                          @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo){
        Page<HospitalSet> page = new Page<>(current, limit);
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
        //获取前端可能发送过来的查询信息
        String hoscode = hospitalSetQueryVo.getHoscode();
        String hosname = hospitalSetQueryVo.getHosname();
        //如果不为空就加上
        queryWrapper.eq(StringUtils.checkValNotNull(hosname),"hosname",hosname);
        queryWrapper.eq(StringUtils.checkValNotNull(hoscode),"hoscode",hoscode);
        //查询出来的值
        Page<HospitalSet> hospitalList = hospitalSetService.page(page, queryWrapper);
        return Result.ok(hospitalList);
    }

    /**
     *  新增信息
     * @param hospitalSet 前端发送要新增的信息
     * @return data true or null
     */
    @PutMapping("/add")
    public Result<Boolean> addHospitalSet(@RequestBody HospitalSet hospitalSet){
        if (hospitalSet == null){
            return Result.fail();
        }
        //因为数据库默认为0不能使用 设置为1可以使用
        hospitalSet.setStatus(1);
        //签名密钥生成
        Random random = new Random();
        String encrypt = MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000));
        hospitalSet.setSignKey(encrypt);
        boolean flag = hospitalSetService.save(hospitalSet);
        if(!flag){
          throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        return Result.ok();
    }

    /**
     * 根据id来获取所有的医院设置
     * @param id 医院id
     * @return 入驻医院的信息
     */
    @GetMapping("/{id}")
    public Result<HospitalSet> getHospitalById(@PathVariable("id") Long id){
        if(id == null){
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        if(hospitalSet == null){
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        return Result.ok(hospitalSet);
    }


    /**
     *修改医院信息
     * @param hospitalSet
     * @return
     */
    @PostMapping("/update")
    public Result<Boolean> updateHospital(@RequestBody HospitalSet hospitalSet){
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if(!flag){
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        return Result.ok(flag);
    }

    /**
     * 根据 集合id批量删除
     * @param ids id集合
     * @return
     */
    @DeleteMapping("/beach")
    public Result<Boolean> deleteBeachHospitalByIds(@RequestBody List<Integer> ids){
        boolean flag = hospitalSetService.removeByIds(ids);
        if(!flag){
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        return Result.ok(flag);
    }

    /**
     * 根据 医院 id 和 状态 修改 状态
     * @param id 入驻的医院id
     * @param status 1 为解锁 0为 锁定
     * @return
     */
    @PutMapping("/lockHospitalSet/{id}/{status}")
    public Result<Boolean> lockHospitalSet(@PathVariable("id") Long id,
                                  @PathVariable("status") Integer status){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.setStatus(status);
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if(!flag){
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        return Result.ok(flag);
    }

    // 发送签名密钥
    @PutMapping("/sendKey/{id}")
    public Result lockHospitalSet(@PathVariable("id") Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        return null;
        //TODO 发送短信
    }
}
