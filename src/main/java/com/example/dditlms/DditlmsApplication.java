package com.example.dditlms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;

//springBoot가 @Mapper가 붙은 MyBatis 매퍼를 스캔하여 빈으로 등록할 수 있도록 한다.
@MapperScan(value = {"com.example.dditlms.mapper"})
@SpringBootApplication(exclude = {MultipartAutoConfiguration.class})
public class DditlmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DditlmsApplication.class, args);
    }

}
