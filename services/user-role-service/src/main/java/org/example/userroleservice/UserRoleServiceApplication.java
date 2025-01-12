package org.example.userroleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableCaching
@EnableDiscoveryClient
public class UserRoleServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserRoleServiceApplication.class, args);
        System.out.println("Hello");
        System.out.println("http://localhost:8081");
    }

}
