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

    /**
     * 根据id 修改 医院上线 状态
     * @param id
     * @param status
     * @return
     */
    boolean updateHosptalById(String id,Integer status);

    /**
     * 根据id 查询医院信息
     * @param id
     * @return
     */
    Map<String,Object> getHospitalById(String id);


    String getHospName(String hoscode);
}