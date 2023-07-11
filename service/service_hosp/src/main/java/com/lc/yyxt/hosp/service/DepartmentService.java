package com.lc.yyxt.hosp.service;

import com.lc.DepartmentVo;
import com.lc.yygh.model.hosp.Department;
import com.lc.yygh.model.hosp.Schedule;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface DepartmentService {
    /**
     * 保存部门
     * @param param
     * @return
     */
    boolean saveDepartment(Map<String, Object> param);

    /**
     * 分页查询数据部门
     * @param param
     * @return
     */
    Page<Department> findDepartment(Map<String, Object> param);


    /**
     * 删除部门信息
     * @param  param
     * @return
     */
    Boolean remove(Map<String, Object> param);


    /**
     * 根据 医院标号查询科室并且 将大科室下的小科室分层放入
     * @param hoscode
     * @return
     */
    //根据医院编号，查询医院所有科室列表
    List<DepartmentVo> findDeptTree(String hoscode);




}
