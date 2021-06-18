//package com.anyuan.adfactiviti.activiti;
//
//import com.anyuan.adfactiviti.AdfActivitiApplication;
//import lombok.extern.slf4j.Slf4j;
//import org.activiti.engine.TaskService;
//import org.activiti.engine.task.Task;
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
// * 任务管理
// * @author liangjy on 2021/4/14.
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = AdfActivitiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Slf4j
//public class ActivitiTaskTest {
//
//    @Autowired
//    private TaskService taskService;
//
//    /**
//     * 查询个人
//     */
//    @Test
//    public void queryCurrentUserTaskList(){
//        Task task=taskService.createTaskQuery()
//                .processDefinitionKey("holiday")
//                //.taskAssignee("liangjy")
//                //.taskAssignee("huangtao")
//                .taskAssignee("chenghua")
//                .singleResult();
//        log.info("流程实例ID:{}" +
//                "任务ID:{}" +
//                "任务负责人:{}" +
//                "任务名称:{}",task.getProcessInstanceId(),task.getId(),task.getAssignee(),task.getName());
//    }
//
//    /**
//     * 完成，结束任务
//     */
//    @Test
//    public void completeTask(){
//        //String taskId="6128da2c-9d00-11eb-9e43-005056c00001";
//        //String taskId="3eae9916-9d01-11eb-a59c-005056c00001";
//        String taskId="74535ada-9d01-11eb-8d45-005056c00001";
//        Map<String,Object> map=new HashMap<>();
//        //map.put("manager","huangtao");
//        //map.put("hr","chenghua");
//        //taskService.complete(taskId,map);
//        taskService.complete(taskId);
//        log.info("任务:{}  已完成",taskId);
//    }
//
//
//
//}
