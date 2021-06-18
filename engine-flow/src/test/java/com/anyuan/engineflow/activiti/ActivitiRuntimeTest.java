//package com.anyuan.adfactiviti.activiti;
//
//import com.anyuan.adfactiviti.AdfActivitiApplication;
//import lombok.extern.slf4j.Slf4j;
//import org.activiti.engine.RuntimeService;
//import org.activiti.engine.runtime.ProcessInstance;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 执行管理，包括启动、推进、删除流程实例等操作
// * @author liangjy on 2021/4/14.
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = AdfActivitiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Slf4j
//public class ActivitiRuntimeTest {
//
//    @Autowired
//    private RuntimeService runtimeService;
//
//    /**
//     * 启动流程实例
//     */
//    @Test
//    public void startFlow(){
//        ProcessInstance instance=runtimeService.startProcessInstanceByKey("holiday");
//        log.info("启动已定义的流程实例:instanceID:{}",instance.getProcessInstanceId());
//    }
//
//    /**
//     * 启动流程实例（带变量的）(请假申请)
//     */
//    @Test
//    public void startFlow2(){
//        Map<String, Object> map = new HashMap<String,Object>();
//        map.put("self", "liangjy");  //根据流程图，这边指定具体的用户，这个用户目前叫经理
//        ProcessInstance instance=runtimeService.startProcessInstanceByKey("holiday",map);
//        log.info("启动已定义的流程实例:instanceID:{}",instance.getProcessInstanceId());
//    }
//
//    /**
//     * 启动流程实例（带变量的）（经理审批）
//     */
//    @Test
//    public void startFlow3(){
//        Map<String, Object> map = new HashMap<String,Object>();
//        map.put("manager", "huangtao");  //根据流程图，这边指定具体的用户，这个用户目前叫经理
//        ProcessInstance instance=runtimeService.startProcessInstanceByKey("holiday",map);
//        log.info("启动已定义的流程实例:instanceID:{}",instance.getProcessInstanceId());
//    }
//
//    /**
//     * 启动流程实例（带变量的）（HR审批）
//     */
//    @Test
//    public void startFlow4(){
//        Map<String, Object> map = new HashMap<String,Object>();
//        map.put("hr", "chenghua");  //根据流程图，这边指定具体的用户，这个用户目前叫经理
//        ProcessInstance instance=runtimeService.startProcessInstanceByKey("holiday",map);
//        log.info("启动已定义的流程实例:instanceID:{}",instance.getProcessInstanceId());
//    }
//
//
//    /**
//     * 查看流程是否结束
//     */
//    @Test
//    public void isFlowEnd(){
//        String instanceId="bfd06675-9ce2-11eb-bce4-005056c00001";
//        ProcessInstance instance=runtimeService.createProcessInstanceQuery()
//                .processInstanceId(instanceId)
//                .singleResult();
//        if(instance==null){
//            log.info("流程已经结束");
//        }else{
//            log.info("流程尚未结束");
//        }
//    }
//
//}
