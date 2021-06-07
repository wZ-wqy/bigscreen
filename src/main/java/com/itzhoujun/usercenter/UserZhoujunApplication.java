package com.itzhoujun.usercenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

// 扫描mybatis哪些包里面的接口
@MapperScan("com.itzhoujun")
@SpringBootApplication
public class UserZhoujunApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                UserZhoujunApplication.class, args);
    }

}
