package com.lc;

import com.lc.yyxt.hosp.ServiceHospApplication;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

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
        User user = new User();
        user.setUsername("测试用户名");
        user.setPassword("测试密码");
        user.setNickName("测试昵称");
        mongoTemplate.insert(user);
    }


    @Test
    public void addBachDocument() {
        ArrayList<User> userArrayList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            User user = new User();
            user.setUsername("测试用户名" + i);
            user.setPassword("测试密码" + i);
            user.setNickName("测试昵称" + i);
            userArrayList.add(user);
        }
        mongoTemplate.insertAll(userArrayList);
    }

    @Test
    public void findUser() {
        List<User> userList = mongoTemplate.findAll(User.class);
        userList.forEach(System.out::println);
    }

    @Test
    public void getById() {
        User user = mongoTemplate.findById("649fce4ba170a422ffb5f136", User.class);
        Assert.assertNotNull(user);
        System.out.println(user);
    }

    @Test
    public void findUserList() {
        Query query = new Query(Criteria.where("username").is("测试用户名").and("password").is("测试密码"));
        List<User> users = mongoTemplate.find(query, User.class);
        users.forEach(System.out::println);
    }

    //模糊查询
    @Test
    public void findUserLikeUsername() {
        String username = "test";
        //^.*用.*$
        String regex = String.format("%s%s%s", "^.*", username, ".*$");
        Pattern compile = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        ;
        Query query = new Query(Criteria.where("username").regex(compile));
        List<User> users = mongoTemplate.find(query, User.class);
        Assert.assertNotNull(users);
        users.forEach(System.out::println);
        System.out.println(users);
    }

    @Test
    public void findUserPage() {
        String username = "用";
        int pageNo = 1;
        int pageSize = 10;
        Query query = new Query();
        String regex = String.format("%s%s%s", "^.*", username, ".*$");
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        query.addCriteria(Criteria.where("username").regex(pattern));
        int totalCount = (int) mongoTemplate.count(query, User.class);
        List<User> users = mongoTemplate.find(query.skip((pageNo - 1) * pageSize).limit(pageSize), User.class);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("list", users);
        hashMap.put("totalCount", totalCount);
        System.out.println("totalCount: " + totalCount);
        Object list = hashMap.get("list");
        System.out.println(list);
    }

    //修改
    @Test
    public void updateUser(){
        User user = mongoTemplate.findById("649fd314c55e175c246cfc27", User.class);
        user.setUsername("test1");
        user.setPassword("test2");
        user.setNickName("test3");
        Query query = new Query(Criteria.where("_id").is(user.getId()));
        Update update = new Update();
        update.set("username",user.getUsername());
        update.set("password",user.getPassword());
        update.set("nickName",user.getNickName());
        UpdateResult upsert = mongoTemplate.upsert(query, update, User.class);
        long matchedCount = upsert.getMatchedCount();
        System.out.println(matchedCount);
    }

    //删除操作
    @Test
    public void delete() {
        Query query =
                new Query(Criteria.where("_id").is("649fd314c55e175c246cfc27"));
        DeleteResult result = mongoTemplate.remove(query, User.class);
        long count = result.getDeletedCount();
        System.out.println(count);
    }
}
