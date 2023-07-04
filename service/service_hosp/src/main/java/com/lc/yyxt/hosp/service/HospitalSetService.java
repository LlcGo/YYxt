package com.lc.yyxt.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.yygh.model.hosp.HospitalSet;

public interface HospitalSetService extends IService<HospitalSet> {
    String getSignKeyByHsopitalCode(String hoscode);
}