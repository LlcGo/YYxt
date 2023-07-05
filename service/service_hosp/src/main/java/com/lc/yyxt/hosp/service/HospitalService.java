package com.lc.yyxt.hosp.service;

import com.lc.HospitalQueryVo;
import com.lc.yygh.model.hosp.Hospital;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface HospitalService {

    void save(Map<String, Object> param);

    Hospital getHospital(String hoscode);

    /**
     * 管理界面接口，分页查询医院信息
     * @param pageSize
     * @param pageNum
     * @param hospitalQueryVo
     * @return
     */
    Page<Hospital> selectPage(Integer pageSize, Integer pageNum, HospitalQueryVo hospitalQueryVo);
}