package com.lc.yyxt.hosp.service.impl;

import com.alibaba.fastjson.JSON;
import com.lc.HospitalQueryVo;
import com.lc.cmn.client.DictFeignClient;
import com.lc.jyxt.common.result.Result;
import com.lc.yygh.model.hosp.Hospital;
import com.lc.yyxt.hosp.repostiory.HospitalRepository;
import com.lc.yyxt.hosp.service.HospitalService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Resource
    private DictFeignClient dictFeignClient;

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

    @Override
    public Hospital getHospital(String hoscode) {
        return hospitalRepository.getHospitalByHoscode(hoscode);
    }

    @Override
    public Page<Hospital> selectPage(Integer pageSize, Integer pageNum, HospitalQueryVo hospitalQueryVo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //0为第一页
        Pageable pageable = PageRequest.of(pageSize-1, pageNum, sort);

        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小
        //创建实例
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo,hospital);

        Example<Hospital> example = Example.of(hospital, matcher);
        Page<Hospital> hospitalPage = hospitalRepository.findAll(example, pageable);
        //所有医院信息集合
        List<Hospital> hospitalList = hospitalPage.getContent();
        hospitalList.forEach(this::sethospType);
        return hospitalPage;

    }

    @Override
    public boolean updateHosptalById(String id, Integer status) {
        Hospital hospital = hospitalRepository.findById(id).get();
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        hospitalRepository.save(hospital);
        return true;
    }

    @Override
    public Map<String,Object> getHospitalById(String id) {
        HashMap<String, Object> result = new HashMap<>();
        Hospital hospital = hospitalRepository.findById(id).get();
        this.sethospType(hospital);
        result.put("hospital",hospital);
        result.put("bookingRule",hospital.getBookingRule());
        hospital.setBookingRule(null);
        return result;
    }

    @Override
    public String getHospName(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        if(null != hospital) {
            return hospital.getHosname();
        }
        return "";
    }

    private void sethospType(Hospital hospital) {
        Result<String> hostype = dictFeignClient.getName(hospital.getHostype(), "Hostype");
        Result<String> cityString = dictFeignClient.getName(hospital.getCityCode());
        Result<String> disString = dictFeignClient.getName(hospital.getDistrictCode());
        Result<String> provString = dictFeignClient.getName(hospital.getProvinceCode());
        Map<String, Object> param = hospital.getParam();
        param.put("hospTypeString",hostype.getData());
        param.put("fullAddress",provString.getData() + cityString.getData() + disString.getData());
    }
}