package com.ljy.blog.config;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Log
@Data
@Configuration
@NacosPropertySource(dataId = "business-log", autoRefreshed = true)
public class NacosConfig {

    @NacosValue(value = "${es.address.host:}")
    private List<String> eshost;

    @NacosValue(value = "${es.address.prot:}")
    private List<Integer> esPort;

    @NacosValue(value = "${es.address.schemeName:}")
    private List<String> esSchemeName;


}
