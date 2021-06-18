//package com.anyuan.adfactiviti.activiti;
//
//import com.anyuan.adfactiviti.AdfActivitiApplication;
//import lombok.extern.slf4j.Slf4j;
//import org.activiti.engine.RepositoryService;
//import org.activiti.engine.repository.Deployment;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
///**
// * 管理流程定义
// * @author liangjy on 2021/4/14.
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = AdfActivitiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Slf4j
//public class ActivitiRepositoryTest {
//
//    @Autowired
//    private RepositoryService repositoryService;
//
//    /**
//     * 流程部署
//     */
//    @Test
//    public void deployFlow(){
//        Deployment deployment = repositoryService.createDeployment()
//                .addClasspathResource("diagram/holiday.bpmn")//添加bpmn资源
//                .addClasspathResource("diagram/holiday.png")
//                .name("请假申请单流程")
//                .deploy();
//        log.info("流程部署id:" + deployment.getName());
//        log.info("流程部署名称:" + deployment.getId());
//    }
//
//    @Test
//    public void removeFlow(){
//        String deploymentId="0c3a9ec4-9cd4-11eb-b7c1-005056c00001\"";
//        repositoryService.deleteDeployment(deploymentId);
//        log.info("流程部署id:{} 已删除",deploymentId);
//    }
//
//
//}
