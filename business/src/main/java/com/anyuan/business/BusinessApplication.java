package com.anyuan.business;

import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author liangjy on 2021/4/28.
 */
//@EnableDubbo
//@DubboComponentScan(basePackages = "com.anyuan")
@EnableDiscoveryClient
//@EnableNacosConfig
@NacosPropertySource(dataId = "cmm", autoRefreshed = true)
@NacosPropertySource(dataId = "cmm-datasource", autoRefreshed = true)
@NacosPropertySource(dataId = "cmm-paas", autoRefreshed = true)
@NacosPropertySource(dataId = "cmm-rabbit", autoRefreshed = true)
@NacosPropertySource(dataId = "g7", autoRefreshed = true)
@SpringBootApplication
public class BusinessApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusinessApplication.class, args);
    }

}
