package com.lc.yyxt.hosp.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.lc.yygh.model.hosp.Department;
import com.lc.yyxt.hosp.repostiory.DepartmentRepository;
import com.lc.yyxt.hosp.service.DepartmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * @Author Lc
 * @Date 2023/7/4
 * @Description
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Resource
    private DepartmentRepository departmentRepository;

    @Override
    public boolean saveDepartment(Map<String, Object> param) {
        String jsonString = JSON.toJSONString(param);
        Department department = JSON.parseObject(jsonString, Department.class);
        Department queryDepartment = departmentRepository.getDepartmentByHoscodeAndDepname(department.getHoscode(),department.getDepname());
        if(queryDepartment != null){
            department.setIsDeleted(0);
            department.setUpdateTime(new Date());
        }else {
            department.setIsDeleted(0);
            department.setUpdateTime(new Date());
            department.setCreateTime(new Date());
        }
        Department save = departmentRepository.save(department);
        return !ObjectUtils.isEmpty(save);
    }
}
