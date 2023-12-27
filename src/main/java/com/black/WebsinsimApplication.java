package com.black;

import com.black.dao.WellInitParamDao;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class WebsinsimApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebsinsimApplication.class, args);

    }
}
