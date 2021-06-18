//package com.anyuan.engineflow.service;
//
//import com.anyuan.commons.base.BaseRpcResult;
//import com.anyuan.commons.constants.CommandReason;
//import com.anyuan.commons.constants.Constants;
//import com.anyuan.commons.dto.HistoricActivityInstanceDto;
//import com.anyuan.commons.dto.HistoricTaskInstanceDto;
//import com.anyuan.commons.dto.TaskDto;
//import com.anyuan.commons.exception.FlowException;
//import com.anyuan.commons.exception.FlowValidateException;
//import com.anyuan.commons.service.flowengine.EngineTaskService;
//import com.anyuan.engineflow.util.BeanUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.activiti.bpmn.model.*;
//import org.activiti.engine.*;
//import org.activiti.engine.history.*;
//import org.activiti.engine.impl.HistoricActivityInstanceQueryProperty;
//import org.activiti.engine.impl.HistoricTaskInstanceQueryProperty;
//import org.activiti.engine.impl.cmd.DeleteTaskCmd;
//import org.activiti.engine.impl.identity.Authentication;
//import org.activiti.engine.runtime.Execution;
//import org.activiti.engine.task.Task;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.apache.dubbo.config.annotation.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.*;
//import java.util.function.BinaryOperator;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
///**
// * 任务管理
// * @author liangjy on 2021/4/14.
// */
//@Slf4j
//@Service
//public class EngineTaskServiceImpl implements EngineTaskService {
//
//    @Autowired
//    private TaskService taskService;
//
//    @Autowired
//    private RepositoryService repositoryService;
//
//    @Autowired
//    private RuntimeService runtimeService;
//
//    @Autowired
//    private HistoryService historyService;
//
//    @Autowired
//    private ManagementService managementService;
//
//    /**
//     * 设置流程变量
//     * @param processInstanceId  流程实例ID
//     * @param assignee  流程定义ID
//     * @param variables 变量
//     */
//    @Override
//    public void setProcessVariables(String processInstanceId, String assignee, Map<String, Object> variables){
//        //获取本人的任务ID
//        Task task = taskService.createTaskQuery()
//                .processInstanceId(processInstanceId)
//                .taskAssignee(assignee)
//                .singleResult();
//        //设置流程变量
//        taskService.setVariables(task.getId(),variables);
//    }
//
//    /**
//     * 获取流程变量
//     * @param processInstanceId
//     * @param assignee
//     * @return
//     */
//    @Override
//    public Map<String,Object> getProcessVariables(String processInstanceId,String assignee){
//        Task task=taskService.createTaskQuery().processInstanceId(processInstanceId)
//                .taskAssignee(assignee)
//                .singleResult();
//        return taskService.getVariables(task.getId());
//    }
//
//    /**
//     * 根据人员查询待审批任务
//     * @param assignee
//     * @return
//     */
//    @Override
//    public List<Task> queryUnApprove(String assignee){
//        return taskService.createTaskQuery()
//                .taskCandidateOrAssigned(assignee)
//                .list();
//    }
//
//    /**
//     * 根据userId查询个人任务
//     * @param assignee  流程定义ID
//     * @param assignee  用户ID
//     * @return
//     */
//    @Override
//    public List<Task> queryUserTasksByUserId(String definitionKey,String assignee){
//        return taskService.createTaskQuery()
//                .processDefinitionKey(definitionKey)
//                .taskAssignee(assignee)
//                .list();
//    }
//
//    /**
//     * 根据userId查询个人任务
//     * @param definitionKey  流程定义ID
//     * @param candidateUser  用户ID
//     * @return
//     */
//    @Override
//    public List<Task> queryCandidateUserTasksByUserId(String definitionKey,String candidateUser){
//        return taskService.createTaskQuery()
//                .processDefinitionKey(definitionKey)
//                .taskCandidateUser(candidateUser)
//                .list();
//    }
//
//    /**
//     * 根据userId查询用户所在组任务
//     * @param assignee
//     * @param userId
//     * @return
//     */
//    @Override
//    public List<Task> queryGroupTasksByUserId(String assignee,String userId){
//        return taskService.createTaskQuery()
//                .taskCandidateUser(userId)
//                .list();
//    }
//
//    /**
//     * 查询组任务
//     * @param assignee
//     * @param groupIds
//     * @return
//     */
//    @Override
//    public List<Task> queryGroupTasksByGroupIds(String assignee,String...groupIds){
//         return taskService.createTaskQuery()
//                 .processDefinitionKey(assignee)
//                 .taskCandidateGroupIn(Arrays.asList(groupIds))
//                 .list();
//    }
//
//    /**
//     * 查询当前用户和其所在组的任务列表
//     * @param assignee
//     * @param userId
//     * @return
//     */
//    @Override
//    public List<Task> queryUserAndGroupTaskList(String assignee,String userId){
//        List<Task> userTask = queryUserTasksByUserId(assignee,userId);
//        List<Task> groupTaskList = queryGroupTasksByUserId(assignee,userId);
//        List<Task> resultTaskList=Collections.emptyList();
//        resultTaskList.addAll(userTask);
//        resultTaskList.addAll(groupTaskList);
//        log.info("当前用户和其所在组的任务列表: {}",resultTaskList.size());
//        return resultTaskList;
//    }
//
//    /**
//     * 完成，结束任务
//     * @param taskId
//     * @param varibales
//     */
//    @Override
//    public void completeTask(String taskId,Map<String,Object> varibales){
//        taskService.setVariables(taskId,varibales);
//        taskService.complete(taskId);
//        log.info("任务:{}  已完成",taskId);
//    }
//
//    /**
//     * 添加组中用户
//     * @param taskId
//     * @param userId
//     */
//    @Override
//    public void addGroupUser(String taskId,String userId){
//        log.info("添加组中用户： taskId:{} userId:{}",taskId,userId);
//        taskService.addCandidateUser(taskId,userId);
//
//    }
//
//    /**
//     * 删除组中用户
//     * @param taskId
//     * @param userId
//     */
//    @Override
//    public void deleteGroupUser(String taskId,String userId){
//        log.info("删除组中用户： taskId:{} userId:{}",taskId,userId);
//        taskService.deleteCandidateUser(taskId,userId);
//    }
//
//    /**
//     * 创建任务节点（单人审批）
//     * @param id
//     * @param name
//     * @param assignee
//     * @return
//     */
//    @Override
//    public UserTask createUserTask(String id,String name,String assignee){
//        UserTask userTask=new UserTask();
//        userTask.setName(name);
//        userTask.setId(id);
//        userTask.setAssignee(assignee);
//        return userTask;
//    }
//
//    /**
//     * 处理当前用户的任务
//     * @param processDefinitionKey
//     */
//    @Override
//    public void complateTaskByProcessDefinitionKey(String processDefinitionKey){
//        taskService.createTaskQuery().processDefinitionKey(processDefinitionKey)
//                .taskAssignee(
//                 Authentication.getAuthenticatedUserId()
//                ).list().stream().forEach(task->taskService.complete(task.getId()));
//    }
//
//    /**
//     * 处理当前用户的任务
//     * @param processDefinitionKey
//     */
//    @Override
//    public void complateTaskByProcessDefinitionKey(String processDefinitionKey,Map<String,Object> variable){
//        List<Task> taskList=taskService.createTaskQuery().processDefinitionKey(processDefinitionKey)
//                .taskAssignee(
//                        Authentication.getAuthenticatedUserId()
//                ).list();
//        taskList.stream().forEach(
//                        task->{
//                            log.info("正在提交任务  任务ID:{} 变量:{}",task.getId(),variable);
//                            taskService.complete(task.getId(),variable);
//                        });
//    }
//
//    /**
//     * 完成任务
//     * @param taskId  任务ID
//     * @param variables  变量
//     */
//    @Override
//    public void compateTask(String taskId,Map<String,Object> variables){
//        log.info("正在提交任务  任务ID:{} 变量:{}",taskId,variables);
//        taskService.complete(taskId,variables);
//    }
//
//
//
//    /**
//     * 根据人员查询待审批任务
//     * @param assignee
//     * @return
//     */
//    @Override
//    public BaseRpcResult<List<TaskDto>> findUnApprove(String assignee) {
//        List<Task> list=taskService.createTaskQuery().taskCandidateOrAssigned(assignee).list();
//        return BeanUtils.beanTypetransform(list,TaskDto.class);
//    }
//
//    /**
//     * 进行审批
//     * @param msg  审批意见
//     * @param isAgree  是否同意  1 同意  0 拒绝
//     * @param taskId    任务ID
//     * @param processId  流程ID
//     * @return
//     */
//    @Override
//    public Boolean approve(String msg, com.anyuan.commons.constants.ApproveStatus isAgree, String taskId, String processId, Map<String, Object> varibale) {
//        Task task=taskService.createTaskQuery().taskId(taskId).singleResult();
//        //拒绝，结束流程
//        if(isAgree.getVal()==0){
//            BpmnModel model = repositoryService.getBpmnModel(task.getProcessDefinitionId());
//            Execution execution =runtimeService.createExecutionQuery().executionId(task.getExecutionId())
//                    .singleResult();
//            String activitId=execution.getActivityId();
//            FlowNode flowNode= (FlowNode) model.getMainProcess().getFlowElement(activitId);
//            flowNode.getOutgoingFlows().clear();
//            SequenceFlow newSequenceFlow=new SequenceFlow();
//            newSequenceFlow.setId(String.valueOf(System.currentTimeMillis()%100000L));
//            newSequenceFlow.setSourceFlowElement(flowNode);
//            newSequenceFlow.setTargetFlowElement(createEndEvent());
//        } else if(isAgree.getVal()==1) {
//            taskService.addComment(task.getId(),task.getProcessInstanceId(),msg);
//            taskService.complete(task.getId(),varibale);
//        }
//        return true;
//    }
//
//    /**
//     * 撤回任务
//     * @param currentTaskId  当前任务ID
//     * @param targetTaskId  目标任务ID,默认上级节点，如果找到上级有2个，目标任务必须得万
//     */
////    @Override
////    public void backTask(String currentTaskId,String targetTaskId) throws FlowValidateException {
////        //根据任务ID获取任务
////        Task currentTask=taskService.createTaskQuery().taskId(currentTaskId).singleResult();
////        //获取流程实例ID
////        String processInstanceId=currentTask.getProcessInstanceId();
////        //根据流程实例ID查询任务历史数据,并转map <ID,ENTITY>
////        Map<String,HistoricTaskInstance> historicTaskInstancesMap = historyService
////                .createHistoricTaskInstanceQuery()
////                .processInstanceId(currentTask.getProcessInstanceId())
////                .orderBy(HistoricTaskInstanceQueryProperty.HISTORIC_TASK_INSTANCE_ID)
////                .desc().list()
////                .stream()
////                .collect(Collectors.toMap(HistoricTaskInstance::getId, Function.identity()));
////        //根据流程实例ID,查询,并根据id分组
////        List<HistoricActivityInstance> historicActivityInstances =historyService.createHistoricActivityInstanceQuery()
////                .processInstanceId(processInstanceId)
////                .orderBy(HistoricActivityInstanceQueryProperty.HISTORIC_ACTIVITY_INSTANCE_ID)
////                .asc().list();
////        Map<String, List<HistoricActivityInstance>> historicActivityInstanceMap=historicActivityInstances.stream().collect(Collectors.groupingBy(HistoricActivityInstance::getActivityId));
////        //获取节点
////        Map<String, FlowNode> flowNodeMap =getFlowNodeMap(historicActivityInstances,currentTask.getProcessDefinitionId());
////        //排除当前任务外的所有正在进行的任务
////        List<Task> taskList=taskService.createTaskQuery().processInstanceId(processInstanceId).list()
////                .stream().filter(task -> !task.getId().endsWith(currentTask.getId()))
////                .collect(Collectors.toList());
////        //处理回退
////        handleBackTask(currentTask,currentTask.getTaskDefinitionKey(),targetTaskId,historicTaskInstancesMap
////            ,historicActivityInstanceMap,flowNodeMap,taskList,historicActivityInstances);
////    }
//
//    @Override
//    @Transactional(rollbackFor = FlowException.class)
//    public void handleBackTask(TaskDto currentTask, String taskDefinitionKey, String targetTaskId, Map<String, HistoricTaskInstanceDto> historicTaskInstanceMap, Map<String, List<HistoricActivityInstanceDto>> historicActivityInstanceMap, Map<String, FlowNode> flowNodeMap, List<TaskDto> taskList, List<HistoricActivityInstanceDto> historicActivityInstanceList) throws FlowValidateException {
//        //判断是否并行
//        if (taskList == null || taskList.isEmpty()) {
//            //串行
//            handleSerial(currentTask, taskDefinitionKey, targetTaskId, historicTaskInstanceMap, historicActivityInstanceMap, flowNodeMap, taskList, historicActivityInstanceList);
//        } else {
//            //并行
//            handleParallel(currentTask, taskDefinitionKey, targetTaskId, historicTaskInstanceMap, historicActivityInstanceMap, flowNodeMap, taskList, historicActivityInstanceList);
//        }
//    }
//
//    /**
//     * 处理串行的任务
//     * @param currentTask  当前任务
//     * @param taskDefinitionKey   任务key：节点定义
//     * @param targetTaskId        目标任务ID
//     * @param historicTaskInstanceMap     历史任务实例map
//     * @param historicActivityInstanceMap
//     * @param flowNodeMap
//     * @param taskList
//     * @param historicActivityInstanceList
//     */
//    @Transactional(rollbackFor = FlowException.class)
//    public void handleSerial(Task currentTask, String taskDefinitionKey, String targetTaskId, Map<String, HistoricTaskInstance> historicTaskInstanceMap, Map<String, List<HistoricActivityInstance>> historicActivityInstanceMap, Map<String, FlowNode> flowNodeMap, List<Task> taskList, List<HistoricActivityInstance> historicActivityInstanceList) throws FlowValidateException {
//        //获取 当前流程节点
//        FlowNode currentNode = flowNodeMap.get(taskDefinitionKey);
//        //获取当前流程节点和上一步流程节点的连线
//        List<SequenceFlow> sequenceFlows = currentNode.getIncomingFlows();
//        if (sequenceFlows.size() == 1) {   //只有一个上一步流程节点
//            SequenceFlow sequenceFlow = sequenceFlows.get(0);
//            HistoricActivityInstance historicActivityInstance = historicActivityInstanceMap.get(sequenceFlow.getSourceRef()).get(0);
//            //网关
//            if (historicActivityInstance.getActivityType().equals(Constants.NODE_PARALLEL_GATEWAY)
//                    || historicActivityInstance.getActivityType().equals(Constants.NODE_EXCLUSIVE_GATEWAY)) {   //如果上级节点是网关
//                //查找网关的父任务
//                Set<String> parentFlowNodes = queryParentFlowNode(historicActivityInstance.getActivityId(), flowNodeMap);
//                if (!parentFlowNodes.isEmpty()) {
//                    handleBackTaskSingle(parentFlowNodes, currentTask, targetTaskId, historicTaskInstanceMap, historicActivityInstanceMap, flowNodeMap, taskList, historicActivityInstanceList);
//                } else {
//                    //当前节点的上级节点有多条 这里需要指定要回退的taskId
//                    deleteTaskMultiple(flowNodeMap, historicTaskInstanceMap, targetTaskId, null, currentTask, taskList, null);
//                }
//            } else if (historicActivityInstance.getActivityType().equals(Constants.NODE_USER_TASK)) {   //如果上级节点是用户任务
//                deleteTaskSingle(flowNodeMap, historicActivityInstance.getActivityId(), currentTask.getId());
//            } else {
//                //todo 还没想好这种场景
//                throw new FlowValidateException(Constants.BPMN_NOT_SUPPORT);
//            }
//        } else {  //多个连线
//            Map<String, HistoricVariableInstance> historicVariableInstanceMap = getHistoricVariableInstanceMap(currentTask.getProcessInstanceId());
//            //串行的也有多条连线，可能是通过排他网关过来的
//            Set<HistoricActivityInstance> historicActivityInstances = new HashSet<>();
//            for (SequenceFlow sequenceFlow : sequenceFlows) {
//                //这边他的parent可能是没做过的，要找做过的
//                if (historicActivityInstanceMap.get(sequenceFlow.getSourceRef()) != null && querySequenceFlowCondition(sequenceFlow, historicVariableInstanceMap)) {
//                    historicActivityInstances.addAll(historicActivityInstanceMap.get(sequenceFlow.getSourceRef()));
//                }
//            }
//            //走过的只有一个
//            if (historicActivityInstances.size() == 1) {
//                List<HistoricActivityInstance> historicActivityInstancesList = new ArrayList<>(historicActivityInstances);
//                if (historicActivityInstancesList.get(0).getActivityType().equals(Constants.NODE_USER_TASK)) {
//                    deleteTaskSingle(flowNodeMap, historicActivityInstancesList.get(0).getActivityId(), currentTask.getId());
//                } else if (historicActivityInstancesList.get(0).getActivityType().equals(Constants.NODE_EXCLUSIVE_GATEWAY)) {
//                    //排他网关
//                    Set<String> parentFlowNodes = queryParentFlowNode(historicActivityInstancesList.get(0).getActivityId(), flowNodeMap);
//                    handleBackTaskSingle(parentFlowNodes, currentTask, targetTaskId, historicTaskInstanceMap, historicActivityInstanceMap, flowNodeMap, taskList, historicActivityInstanceList);
//                } else {
//                    //todo 还没想好这种场景
//                    throw new FlowValidateException(Constants.BPMN_NOT_SUPPORT);
//                }
//            } else {
//                //当前节点的上级节点有多条 这里需要指定要回退的taskId
//                deleteTaskMultiple(flowNodeMap, historicTaskInstanceMap, targetTaskId, null, currentTask, taskList, null);
//            }
//        }
//    }
//
//    /**
//     * 处理简单任务回退
//     * @param parentFlowNodes  上级流程节点
//     * @param currentTask    当前任务
//     * @param targetTaskId  目标任务ID
//     * @param historicTaskInstanceMap   历史任务实例map
//     * @param historicActivityInstanceMap  历史活跃实例map
//     * @param flowNodeMap  流程节点map
//     * @param taskList  任务列表
//     * @param historicActivityInstanceList  历史活跃实例list
//     */
//    @Transactional(rollbackFor = FlowException.class)
//    public void handleBackTaskSingle(Set<String> parentFlowNodes,Task currentTask,String targetTaskId,Map<String,HistoricTaskInstance> historicTaskInstanceMap,Map<String,List<HistoricActivityInstance>> historicActivityInstanceMap
//            ,Map<String,FlowNode> flowNodeMap,List<Task> taskList,List<HistoricActivityInstance> historicActivityInstanceList) throws FlowValidateException {
//        if(parentFlowNodes.size()==1){
//            //单来源-上级流程节点（单任务）
//            List<String> parentFlowNodeList=new ArrayList<>();
//            if(historicActivityInstanceMap.get(parentFlowNodeList.get(0)).get(0).getActivityType().equals(Constants.NODE_USER_TASK)){
//                //如果上级任务节点是用户任务
//                deleteTaskSingle(flowNodeMap,parentFlowNodeList.get(0),currentTask.getId());
//            }else{
//                //递归找父流程节点的上一个任务
//                handleBackTask(currentTask, historicActivityInstanceMap.get(parentFlowNodeList.get(0)).get(0).getActivityId(), targetTaskId, historicTaskInstanceMap, historicActivityInstanceMap, flowNodeMap, taskList, historicActivityInstanceList);
//            }
//        }else{
//            //多个来源，指定来源节点
//            deleteTaskMultiple(flowNodeMap,historicTaskInstanceMap,targetTaskId,null,currentTask,taskList,null);
//        }
//    }
//
//
//
//    @Transactional(rollbackFor = Exception.class)
//    public void handleParallel(Task currentTask, String taskDefinitionKey, String targetTaskId, Map<String, HistoricTaskInstance> historicTaskInstanceMap, Map<String, List<HistoricActivityInstance>> historicActivityInstanceMap, Map<String, FlowNode> flowNodeMap, List<Task> taskList, List<HistoricActivityInstance> historicActivityInstanceList) throws FlowValidateException {
//        List<SequenceFlow> sequenceFlows = flowNodeMap.get(taskDefinitionKey).getIncomingFlows();
//        if (sequenceFlows.size() == 1) {
//            //当前节点的上级节点只有一条
//            SequenceFlow sequenceFlow = sequenceFlows.get(0);
//            //查询历史节点
//            HistoricActivityInstance historicActivityInstance = historicActivityInstanceList.stream().filter(query -> query.getActivityId().equals(sequenceFlow.getSourceRef())).collect(Collectors.toList()).get(0);
//            //判断来源类型
//            if (historicActivityInstance.getActivityType().equals(Constants.NODE_PARALLEL_GATEWAY)) {
//                //网关
//                //查找网关的父任务
//                Set<String> parentFlowNodes = queryParentFlowNode(historicActivityInstance.getActivityId(), flowNodeMap);
//                if (!parentFlowNodes.isEmpty()) {
//                    if (parentFlowNodes.size() == 1) {
//                        //如果只有一个父节点
//                        String activityId = new ArrayList<>(parentFlowNodes).get(0);
//                        if (historicActivityInstanceMap.get(activityId).get(0).getActivityType().equals(Constants.NODE_USER_TASK)) {
//                            //用户任务
//                            deleteTaskMultiple(flowNodeMap, null, null, activityId, currentTask, taskList, historicActivityInstance.getActivityId());
//                        } else {
//                            //递归去查找父任务的前一个
//                            handleBackTask(currentTask, historicActivityInstanceMap.get(activityId).get(0).getActivityId(), targetTaskId, historicTaskInstanceMap, historicActivityInstanceMap, flowNodeMap, taskList, historicActivityInstanceList);
//                        }
//                    } else {
//                        //当前节点的上级节点有多条 这里需要指定要回退的taskId
//                        deleteTaskMultiple(flowNodeMap, historicTaskInstanceMap, targetTaskId, null, currentTask, taskList, historicActivityInstance.getActivityId());
//                    }
//                } else {
//                    //没有父级任务，图有问题
//                    throw new FlowValidateException("bpmn doc error");
//                }
//
//            } else if (historicActivityInstance.getActivityType().equals(Constants.NODE_USER_TASK)) {
//                //用户任务
//                deleteTaskMultiple(flowNodeMap, null, null, historicActivityInstance.getActivityId(), currentTask, taskList, historicActivityInstance.getActivityId());
//            } else {
//                //todo 还没想好这种场景
//                throw new FlowValidateException(Constants.BPMN_NOT_SUPPORT);
//            }
//        } else {
//            //当前节点的上级节点有多条 这里需要指定要回退的taskId
//            deleteTaskMultiple(flowNodeMap, historicTaskInstanceMap, targetTaskId, null, currentTask, taskList, null);
//        }
//    }
//
//    /**
//     * 删除多任务节点
//     * @param flowNodeMap  流程节点map
//     * @param historicTaskInstanceMap   历史流程任务实例map
//     * @param targetTaskId   历史任务节点ID
//     * @param targetTaskDefinitionKey  目标任务定义key
//     * @param currentTask  当前任务
//     * @param taskList  任务ID
//     * @param targetParentTaskDefinitionKey  目标上级任务定义key
//     */
//    @Transactional(rollbackFor = FlowException.class)
//    public void deleteTaskMultiple(Map<String, FlowNode> flowNodeMap, Map<String, HistoricTaskInstance> historicTaskInstanceMap, String targetTaskId, String targetTaskDefinitionKey, Task currentTask, List<Task> taskList, String targetParentTaskDefinitionKey){
//        if(StringUtils.isEmpty(targetTaskDefinitionKey) || StringUtils.isBlank(targetTaskDefinitionKey)){
//
//        }
//        FlowNode targetNode=flowNodeMap.get(targetTaskDefinitionKey);
//        //删除当前任务
//        managementService.executeCommand(new DeleteTaskCmd(currentTask.getId(),CommandReason.ROLLBACK.getMsg(),false));
//        //删除当前运行的其他相同父任务的子任务
//        Set<Task> sameParentTasks= getSameParentTasks(flowNodeMap,taskList,targetParentTaskDefinitionKey);
//        for(Task task:sameParentTasks){
//            managementService.executeCommand(new DeleteTaskCmd(task.getId(), CommandReason.ROLLBACK.getMsg(),false));
//        }
//    }
//
//
//
//    /**
//     * 删除单任务节点
//     * @param flowNodeMap  流程节点map
//     * @param targetTaskActivitiId   历史流程任务实例map
//     * @param currentTaskId  当前任务ID
//     */
//    @Transactional(rollbackFor = FlowException.class)
//    public void deleteTaskSingle(Map<String, FlowNode> flowNodeMap, String targetTaskActivitiId, String currentTaskId){
//        //目标流程节点
//        FlowNode targetNode=flowNodeMap.get(targetTaskActivitiId);
//        //删除当前运行任务
//        managementService.executeCommand(new DeleteTaskCmd(currentTaskId,CommandReason.ROLLBACK.getMsg(),false));
//        //回退到原来的任务节点
//    }
//
//
//    /**
//     * 获取具有相同上级节点的子任务
//     * @param flowNodeMap  流程节点map
//     * @param taskList    任务列表
//     * @param taskDefinitionKey  任务定义key
//     * @return
//     */
//    private Set<Task> getSameParentTasks(Map<String,FlowNode> flowNodeMap,List<Task> taskList,String taskDefinitionKey){
//        if(taskDefinitionKey==null){
//            return new HashSet<>();
//        }
//        return taskList.stream().filter(task->
//                flowNodeMap.get(taskDefinitionKey).getIncomingFlows().stream().filter(commingFlow->commingFlow.getSourceRef().equals(taskDefinitionKey)).collect(Collectors.toSet()).size()>0
//        ).collect(Collectors.toSet());
//    }
//
//    /**
//     * 获取历史变量实例
//     * @param processInstanceId
//     * @return
//     */
//    private Map<String, HistoricVariableInstance> getHistoricVariableInstanceMap(String processInstanceId) {
//        List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery()
//                .processInstanceId(processInstanceId).list();
//        Map<String, HistoricVariableInstance> historicVariableInstanceMap = historicVariableInstances.stream()
//                .collect(Collectors.toMap(HistoricVariableInstance::getVariableName,
//                        historicVariableInstance -> historicVariableInstance, BinaryOperator.maxBy(Comparator.comparing(HistoricVariableInstance::getId))));
//        return historicVariableInstanceMap;
//    }
//
//    /**
//     * 查询顺序流程条件
//     * @param pvmTransition
//     * @param historicVariableInstanceMap
//     * @return
//     */
//    private boolean querySequenceFlowCondition(SequenceFlow pvmTransition, Map<String, HistoricVariableInstance> historicVariableInstanceMap) {
//        String conditionExpression = pvmTransition.getConditionExpression();
//        if (StringUtils.isNotEmpty(conditionExpression) && StringUtils.isNotBlank(conditionExpression)) {
//            conditionExpression = conditionExpression.substring(conditionExpression.indexOf('{') + 1, conditionExpression.indexOf('}'));
//            List<String> strings = Arrays.asList(conditionExpression.split("=="));
//            strings.forEach(s -> s = s.replace(" ", ""));
//            if (historicVariableInstanceMap.get(strings.get(0)).getValue().equals(strings.get(1).replaceAll("\"", ""))) {
//                return true;
//            } else {
//                return false;
//            }
//        } else {
//            return true;
//        }
//
//    }
//
//
//    /**
//     * 创建结束节点
//     * @return
//     */
//    private EndEvent createEndEvent(){
//        EndEvent endEvent = new EndEvent();
//        endEvent.setId("endEvent");
//        endEvent.setName("end");
//        return endEvent;
//    }
//
//    /**
//     * 查询上级流程节点
//     * @param activityId
//     * @param flowNodeMap
//     * @return
//     */
//    private Set<String> queryParentFlowNode(String activityId,Map<String,FlowNode> flowNodeMap){
//        Set<String> flowNodeList=new HashSet<>();
//        flowNodeMap.keySet().stream().filter(k-> !StringUtils.equalsIgnoreCase(k,activityId)).forEach(k->{
//            FlowNode flowNode=flowNodeMap.get(k);
//            flowNode.getOutgoingFlows().stream().filter(seqFlow->seqFlow.getTargetRef().equals(activityId))
//                    .forEach(key->{
//                        flowNodeList.add(k);
//                    });
//        });
//        return flowNodeList;
//    }
//
//    /**
//     * 获取流程节点map
//     * @param historicActivityInstanceList
//     * @param processDefinitionId
//     * @return
//     */
//    private Map<String, FlowNode> getFlowNodeMap(List<HistoricActivityInstance> historicActivityInstanceList, String processDefinitionId) {
//        org.activiti.bpmn.model.Process process = repositoryService
//                .getBpmnModel(processDefinitionId)
//                .getMainProcess();
//        Map<String, FlowNode> flowNodeMap = new HashMap<>(historicActivityInstanceList.size());
//        historicActivityInstanceList.stream().forEach(instance->{
//            if (flowNodeMap.get(instance.getActivityId()) == null) {
//                FlowNode sourceNode = (FlowNode) process.getFlowElement(instance.getActivityId());
//                flowNodeMap.put(instance.getActivityId(), sourceNode);
//            }
//        });
//        return flowNodeMap;
//    }
//
//}
