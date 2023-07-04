package com.lc.yyxt.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.yygh.model.hosp.HospitalSet;
import com.lc.yyxt.hosp.mapper.HospitalSetMapper;
import com.lc.yyxt.hosp.service.HospitalSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Lc
 * @Date 2023/6/18
 * @Description
 */
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {

    @Override
    public String getSignKeyByHsopitalCode(String hoscode) {
        QueryWrapper<HospitalSet> hospitalSetQueryWrapper = new QueryWrapper<>();
        hospitalSetQueryWrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = this.getOne(hospitalSetQueryWrapper);
        return hospitalSet.getSignKey();
    }
}
