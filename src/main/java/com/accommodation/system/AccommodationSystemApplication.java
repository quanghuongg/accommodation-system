package com.accommodation.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.accommodation.system.mapper")
@SpringBootApplication
@EnableScheduling
public class AccommodationSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccommodationSystemApplication.class, args);
    }

}
