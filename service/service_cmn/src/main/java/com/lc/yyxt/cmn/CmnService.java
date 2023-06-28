package com.lc.yyxt.cmn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author Lc
 * @Date 2023/6/26
 * @Description
 */
@SpringBootApplication
@ComponentScan("com.lc")
public class CmnService {
    public static void main(String[] args) {
        SpringApplication.run(CmnService.class,args);
    }
}
