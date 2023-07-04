package com.lc.yyxt.hosp.repostiory;

import com.lc.yygh.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DepartmentRepository extends MongoRepository<Department,String> {
    Department getDepartmentByHoscodeAndDepname(String hoscode, String depname);
}
