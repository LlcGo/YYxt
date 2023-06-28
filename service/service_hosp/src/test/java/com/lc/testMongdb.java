package com.lc;
import com.google.common.collect.Maps;

import com.lc.yygh.model.acl.User;
import com.lc.yyxt.hosp.ServiceHospApplication;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author Lc
 * @Date 2023/6/28
 * @Description
 */

@SpringBootTest(classes = ServiceHospApplication.class)
@RunWith(SpringRunner.class)
public class testMongdb {

    @Resource
    private MongoTemplate mongoTemplate;

    // 文档的添加
    @Test
    public void addDocument() {
//        User user = new User();
//        user.setId(6L);
//        user.setUsername("aa");
//        user.setPassword("123456");
//////        mongoTemplate.save(user); // _id存在时会把旧数据进行覆盖；
//        User save = mongoTemplate.save(user);// _id存在时会提示主键重复的异常；
//        System.out.println(save);
//        Set<String> collectionNames = mongoTemplate.getCollectionNames();
//        System.out.println(collectionNames);
        List<User> users = mongoTemplate.findAll(User.class);
        System.out.println(users);
    }


}
