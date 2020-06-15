package com.userManager;

import com.utils.WriteTxt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

import java.io.IOException;

@SpringBootApplication
//@EnableNeo4jRepositories
public class UsermanagerMS {
    public static void main(String[] args) {

        SpringApplication.run(UsermanagerMS.class ,args);
    }
}
