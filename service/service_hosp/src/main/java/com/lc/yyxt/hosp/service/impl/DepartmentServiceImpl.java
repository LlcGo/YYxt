package com.lc.yyxt.hosp.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.lc.jyxt.common.exception.YyghException;
import com.lc.jyxt.common.result.ResultCodeEnum;
import com.lc.yygh.model.hosp.Department;
import com.lc.yygh.model.hosp.Schedule;
import com.lc.yyxt.hosp.repostiory.DepartmentRepository;
import com.lc.yyxt.hosp.service.DepartmentService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    @Override
    public Page<Department> findDepartment(Map<String, Object> param) {
        //必须参数校验 略
        String hoscode = (String)param.get("hoscode");
        if(StringUtils.isEmpty(hoscode)){
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        int page = StringUtils.isEmpty(param.get("page")) ? 1 : Integer.parseInt((String)param.get("page"));
        int limit = StringUtils.isEmpty(param.get("limit")) ? 10 : Integer.parseInt((String)param.get("limit"));
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //0为第一页
        Pageable pageable = PageRequest.of(page-1, limit, sort);

        Department department = new Department();
        department.setHoscode(hoscode);
        department.setIsDeleted(0);

       //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小
        //创建实例

        Example<Department> example = Example.of(department, matcher);
        return departmentRepository.findAll(example, pageable);
    }

    @Override
    public Boolean remove(Map<String, Object> param) {
        String hoscode = (String)param.get("hoscode");
        String depcode = (String)param.get("depcode");
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode,depcode);
        if(department != null){
            departmentRepository.deleteById(department.getId());
        }
        return true;
    }


}
