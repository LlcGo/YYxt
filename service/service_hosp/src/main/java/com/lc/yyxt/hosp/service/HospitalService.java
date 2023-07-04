package com.lc.yyxt.hosp.service;

import com.lc.yygh.model.hosp.Hospital;

import java.util.Map;

public interface HospitalService {

    void save(Map<String, Object> param);

    Hospital getHospital(String hoscode);

}