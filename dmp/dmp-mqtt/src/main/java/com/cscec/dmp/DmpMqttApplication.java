package com.cscec.dmp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@MapperScan("com.cscec.**.mapper")
@SpringBootApplication
public class DmpMqttApplication {

    public static void main(String[] args) {
        SpringApplication.run(DmpMqttApplication.class, args);
    }

}
