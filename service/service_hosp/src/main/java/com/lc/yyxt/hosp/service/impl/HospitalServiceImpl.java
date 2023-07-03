package com.lc.yyxt.hosp.service.impl;

import com.alibaba.fastjson.JSON;
import com.lc.yygh.model.hosp.Hospital;
import com.lc.yygh.model.hosp.HospitalSet;
import com.lc.yyxt.hosp.repostiory.HospitalRepository;
import com.lc.yyxt.hosp.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Override
    public void save(Map<String, Object> param) {
        //1.解析数据
        String jsonString = JSON.toJSONString(param);
        Hospital hospital = JSON.parseObject(jsonString, Hospital.class);
        //2.判断是否第一次修改
        Hospital selectHospital = hospitalRepository.getHospitalByHoscode(hospital.getHoscode());
        //（1）no 修改
        if(selectHospital == null){
            hospital.setIsDeleted(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setStatus(0);
        }else{
            // （2）yes 新增
            hospital.setStatus(hospital.getStatus());
            hospital.setUpdateTime(new Date());
            hospital.setCreateTime(selectHospital.getCreateTime());
            hospital.setIsDeleted(0);
        }
        hospitalRepository.save(hospital);

    }
}