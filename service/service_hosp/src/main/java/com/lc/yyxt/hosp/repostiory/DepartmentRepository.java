package com.lc.yyxt.hosp.repostiory;

import com.lc.yygh.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends MongoRepository<Department,String> {
    Department getDepartmentByHoscodeAndDepname(String hoscode, String depname);

    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);
}
