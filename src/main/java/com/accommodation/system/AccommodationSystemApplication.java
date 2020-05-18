package com.accommodation.system;

import com.accommodation.system.entity.User;
import org.apache.ibatis.type.MappedTypes;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MappedTypes({User.class})
@MapperScan("com.api.user.mapper")
@SpringBootApplication
public class AccommodationSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccommodationSystemApplication.class, args);
    }

}
