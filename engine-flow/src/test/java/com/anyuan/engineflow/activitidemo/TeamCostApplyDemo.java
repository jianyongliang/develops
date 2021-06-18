//package com.anyuan.adfactiviti.activitidemo;
//
//import com.anyuan.adfactiviti.AdfActivitiApplication;
//import com.anyuan.adfactiviti.constants.ApproveStatus;
//import com.anyuan.adfactiviti.service.*;
//import lombok.extern.slf4j.Slf4j;
//import org.activiti.engine.impl.identity.Authentication;
//import org.activiti.engine.repository.Deployment;
//import org.activiti.engine.runtime.ProcessInstance;
//import org.activiti.engine.task.Task;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.*;
//
///**
// * @author liangjy on 2021/4/21.
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = AdfActivitiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Slf4j
//public class TeamCostApplyDemo {
//
//    @Autowired
//    private ActivitiIdentityService identityService;
//
//    @Autowired
//    private ActivitiHistoryService historyService;
//
//    @Autowired
//    private ActivitiRepositoryService repositoryService;
//
//    @Autowired
//    private ActivitiRuntimeService runtimeService;
//
//    @Autowired
//    private ActivitiTaskService taskService;
//
//
//    @Test
//    public void test(){
//        //1.先部署最新版bpmn模型
//        //repositoryService.removeFlow("tdjsfy");
//        //repositoryService.removeFlow("tdjsfy2");
//        //Deployment deployment=repositoryService.deployFlow("tdjsfy2","团队建设费用申请");
////        if(1==1?true:false){
////            //return;
////        }
//        //2.发起流程 设置发起人,启动流程节点   7.x版本取消了消息
//        //设置为liangjianyong
//        Authentication.setAuthenticatedUserId("liangjianyong");
//        Map<String,Object> variableMap=new HashMap<>();
//        variableMap.put("departLeader","huangtao");
//        variableMap.put("startUser","liangjinayong");
//        ProcessInstance instance=runtimeService.startFlow("tdjsfy2", variableMap);
//        log.debug("startUser:{}",instance.getStartUserId());
//        //3.查询本人任务节点 (下一步)
//        List<Task> taskList=taskService.queryUserTasksByUserId("tdjsfy2","liangjinayong");
//        Optional.of(taskList).orElse(new ArrayList<>()).forEach(task->{
//            taskService.compateTask(task.getId(),variableMap);
//        });
//        //4.部门审核
//        Authentication.setAuthenticatedUserId("huangtao");
//        List<Task> taskList2=taskService.queryUnApprove("huangtao");
//        variableMap.put("xzzy","xzzy");
//        variableMap.put("bmshjg",0);
//        Optional.of(taskList2).orElse(new ArrayList<>()).stream().forEach(task->{
//            taskService.compateTask(task.getId(),variableMap);
//        });
//        //5.行政备注
//        Authentication.setAuthenticatedUserId("xzzy");
//        List<Task> taskList3=taskService.queryUnApprove("xzzy");
//        Optional.of(taskList3).orElse(new ArrayList<>()).forEach(task->{
//            taskService.compateTask(task.getId(),variableMap);
//        });
//
//        //5.行政备注
//
//        //6.行政经理审核
//
//        //7.高管审核
//
//        //8.公司生来
//        return;
//    }
//
//}
