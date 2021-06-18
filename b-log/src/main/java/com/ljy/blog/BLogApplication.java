package com.ljy.blog;

import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.discovery.EnableNacosDiscovery;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableNacosDiscovery
@EnableNacosConfig
public class BLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BLogApplication.class, args);
    }

}
