package com.cscec.dmp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.cscec.**.mapper")
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class DmpHttpApplication {

    public static void main(String[] args) {
        SpringApplication.run(DmpHttpApplication.class, args);
    }

}
