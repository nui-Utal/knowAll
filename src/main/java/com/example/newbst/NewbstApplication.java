package com.example.newbst;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@MapperScan("com.example.newbst.mapper")
@EnableScheduling
public class NewbstApplication {
    public static void main(String[] args) {
        SpringApplication.run(NewbstApplication.class, args);
        log.info("项目启动...");
    }
}
