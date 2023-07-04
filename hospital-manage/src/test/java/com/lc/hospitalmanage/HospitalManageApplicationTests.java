package com.lc.hospitalmanage;

import com.lc.hospital.ManageApplication;
import com.lc.hospital.util.MD5;
import lombok.With;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

@SpringBootTest(classes = ManageApplication.class)
class HospitalManageApplicationTests {

    @Test
    void contextLoads() {
        String encrypt = MD5.encrypt("666");
        String encrypt1 = MD5.encrypt("666");
        System.out.println(encrypt);
        System.out.println(encrypt1);
    }


}
