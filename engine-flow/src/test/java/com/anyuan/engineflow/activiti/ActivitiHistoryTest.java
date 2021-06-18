//package com.anyuan.adfactiviti.activiti;
//
//import com.anyuan.adfactiviti.AdfActivitiApplication;
//import lombok.extern.slf4j.Slf4j;
//import org.activiti.engine.HistoryService;
//import org.activiti.engine.history.HistoricActivityInstance;
//import org.activiti.engine.history.HistoricProcessInstance;
//import org.activiti.engine.history.HistoricTaskInstance;
//import org.activiti.engine.history.HistoricVariableInstance;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.List;
//
///**
// * 历史管理(执行完的数据的管理)
// * @author liangjy on 2021/4/14.
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = AdfActivitiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Slf4j
//public class ActivitiHistoryTest {
//
//    @Autowired
//    private HistoryService historyService;
//
//    /**
//     * 查询历史流程实例
//     */
//    @Test
//    public void findHistoryProcessInstance(){
//        String processInstanceId="";
//        HistoricProcessInstance instance=historyService.createHistoricProcessInstanceQuery()
//            .processInstanceId(processInstanceId)
//            .orderByProcessInstanceStartTime()
//                .asc().singleResult();
//    }
//
//    /**
//     * 查询历史活动实例的查询
//     */
//    @Test
//    public void findHistoryActivity(){
//        String processInstanceId="";
//        List<HistoricActivityInstance> list=historyService.createHistoricActivityInstanceQuery()
//                .processInstanceId(processInstanceId)
//                .orderByHistoricActivityInstanceStartTime()
//                .asc().list();
//    }
//
//    /**
//     * 查询历史任务
//     */
//    @Test
//    public void findHistoryTask(){
//        String processInstanceId="";
//        List<HistoricTaskInstance> list=historyService.createHistoricTaskInstanceQuery()
//                .processInstanceId(processInstanceId)
//                .orderByHistoricTaskInstanceStartTime().asc()
//                .list();
//    }
//
//    /**
//     * 查询历史流程变量
//     */
//    @Test
//    public void findHistoricVariableInstanceQuery(){
//        String processInstanceId="";
//        List<HistoricVariableInstance> list=historyService.createHistoricVariableInstanceQuery()
//                .processInstanceId(processInstanceId)
//                .list();
//    }
//
//
//}
