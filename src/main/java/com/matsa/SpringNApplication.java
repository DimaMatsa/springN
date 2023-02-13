package com.matsa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;



@SpringBootApplication
@EnableOpenApi
public class SpringNApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringNApplication.class, args);
    }
}
