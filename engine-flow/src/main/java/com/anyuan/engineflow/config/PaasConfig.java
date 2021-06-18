//package com.anyuan.engineflow.config;
//
//import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
//import com.alibaba.nacos.api.config.annotation.NacosValue;
//import com.qycloud.service.core.Config;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.util.Properties;
//
//import static java.lang.Thread.sleep;
//
///**
// * Created by luzaiqiang on 19-11-21.
// */
//@Component
//@Data
//@Slf4j
//public class PaasConfig {
//
//    @NacosValue(value = "${pass.host}", autoRefreshed = true)
//    private String host;// = "http://www.safetydev.aysaas.com:23003"; //pass平台提供的服务
//    @NacosValue(value = "${pass.appId}", autoRefreshed = true)
//    private String appId;// = ""; //pass
//    @NacosValue(value = "${pass.appKey}", autoRefreshed = true)
//    private String appKey;// = ""; //pass
//    @NacosValue(value = "${pass.jwtAlgs}", autoRefreshed = true)
//    private String jwtAlgs;// = "HS256";
//    @NacosValue(value = "${pass.jwtKey}", autoRefreshed = true)
//    private String jwtKey;// = "9&p3RG?B";
//
//    //@PostConstruct
//    public void init() {
//        appId = appId == null ? StringUtils.EMPTY : appId;
//        appKey = appKey == null ? StringUtils.EMPTY : appKey;
//
//        Config.init(host, appId, appKey, jwtAlgs, jwtKey); //Step 1: 初始化
//        log.info("pass init suc,config={}", this.toString());
//    }
//
//    public void reinit() {
//        init();
//    }
//
//
//    @NacosConfigListener(dataId = "cmm-paas", timeout = 6000) //监听paas配置
//    public void onReceived(Properties properties) {
//        log.info(properties.toString());
//        try {
//            sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        reinit();
//    }
//
//}
