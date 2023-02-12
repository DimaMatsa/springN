package com.matsa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class SpringNApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringNApplication.class, args);
    }

    @GetMapping("/")
    public List<String> hello() {
        return List.of("Hello", "World");
    }

}
