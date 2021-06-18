package com.anyuan.engineflow.service;

import com.anyuan.commons.base.BaseRpcResult;
import com.anyuan.commons.dto.HistoricActivityInstanceDto;
import com.anyuan.commons.dto.HistoricProcessInstanceDto;
import com.anyuan.commons.dto.HistoricTaskInstanceDto;
import com.anyuan.commons.dto.HistoricVariableInstanceDto;
import com.anyuan.commons.service.flowengine.EngineHistoryService;
import com.anyuan.engineflow.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 历史管理(执行完的数据的管理)
 * @author liangjy on 2021/4/14.
 */
@Slf4j
@Service
public class EngineHistoryServiceImpl implements EngineHistoryService
{

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 查询历史流程变量
     * @param variableName  变量名称
     * @return
     */
    @Override
    public BaseRpcResult<List<HistoricVariableInstanceDto>> queryHistoryProcessVariables(String variableName){
        List<HistoricVariableInstanceDto> resultList=new ArrayList<>();
        List<HistoricVariableInstance> historyVariableList=historyService.createHistoricVariableInstanceQuery().variableName(variableName).list();
        return BeanUtils.beanTypetransform(historyVariableList,HistoricVariableInstanceDto.class);
    }

    /**
     * 查询历史流程实例
     * @param processInstanceId  流程实例ID
     * @return
     */
    @Override
    public BaseRpcResult<HistoricProcessInstanceDto> queryHistoryProcessInstance(String processInstanceId){
        HistoricProcessInstance historicProcessInstance=historyService.createHistoricProcessInstanceQuery()
            .processInstanceId(processInstanceId)
            .orderByProcessInstanceStartTime()
                .asc().singleResult();
        return BeanUtils.beanTypetransform(historicProcessInstance,HistoricProcessInstanceDto.class);
    }

    /**
     * 查询历史活动实例的查询
     * @param processInstanceId 流程实例ID
     * @return
     */
    @Override
    public BaseRpcResult<List<HistoricActivityInstanceDto>> queryHistoryActivity(String processInstanceId){
        List<HistoricActivityInstance> list=historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime()
                .asc().list();
        return  BeanUtils.beanTypetransform(list,HistoricActivityInstanceDto.class);
    }

    /**
     * 查询历史任务
     * @param processInstanceId 流程实例ID
     * @return
     */
    @Override
    public BaseRpcResult<List<HistoricTaskInstanceDto>> queryHistoryTask(String processInstanceId){
        List<HistoricTaskInstance> list=historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceStartTime().asc()
                .list();
        return BeanUtils.beanTypetransform(list,HistoricTaskInstanceDto.class);
    }

    /**
     * 查询历史流程变量
     * @param processInstanceId 流程实例ID
     * @return
     */
    @Override
    public BaseRpcResult<List<HistoricVariableInstanceDto>> queryHistoricVariableInstanceQuery(String processInstanceId){
        List<HistoricVariableInstance> list=historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
        return BeanUtils.beanTypetransform(list,HistoricVariableInstanceDto.class);
    }

    /**
     * 查询本人已申请的流程（已完成的任务）
     * @param userId   用户ID
     * @param processDefinitionKey  流程key
     * @return
     */
    @Override
    public BaseRpcResult<List<HistoricProcessInstanceDto>> queryApplyHistory(String userId,String processDefinitionKey){
        List<HistoricProcessInstance> list= historyService.createHistoricProcessInstanceQuery().finished()
                .orderByProcessInstanceEndTime()
                .finished().list();
        return BeanUtils.beanTypetransform(list,HistoricProcessInstanceDto.class);
    }

    /**
     * 查询本人已执行的任务（已完成的任务）
     * @param userId
     * @param processDefinitionKey
     * @return
     */
    @Override
    public BaseRpcResult<List<HistoricTaskInstanceDto>> queryFinishedTask(String userId,String processDefinitionKey){
        List<HistoricProcessInstance> list=historyService.createHistoricProcessInstanceQuery().processDefinitionKey(processDefinitionKey)
                .involvedUser(userId).finished().list();
        return BeanUtils.beanTypetransform(list,HistoricTaskInstanceDto.class);
    }


    /**
     * 查询流程已执行的步骤
     * @param bpmnModel  模型
     * @param historicActivityInstances  历史活跃流程实例列表
     * @param historicActivityDoneList   历史已处理活跃流程列表
     * @param taskIds
     * @return
     */
    @Override
    public BaseRpcResult<List<String>> queryExecutedFlows(BpmnModel bpmnModel,List<HistoricActivityInstanceDto> historicActivityInstances
                ,List<String> historicActivityDoneList,List<String> taskIds){
        List<String> highFlows = new ArrayList<>();
        if(CollectionUtils.isEmpty(historicActivityInstances)){
            return BeanUtils.beanTypetransform(highFlows,String.class);
        }
        Map<String,HistoricActivityInstanceDto> historicActivityInstanceMap=historicActivityInstances.stream()
                .collect(Collectors.toMap(HistoricActivityInstanceDto::getActivityId,
                        historicActivityInstanceDto -> historicActivityInstanceDto
                                , BinaryOperator.maxBy(Comparator.comparing(HistoricActivityInstanceDto::getId))));
        //取第一个实例获取变量
        Map<String,HistoricVariableInstance> historicVariableInstanceMap= queryHistoricVariableInstanceMap(historicActivityInstances.get(0).getProcessInstanceId());
        //遍历历史节点
        Map<String, FlowElement> flowElementMap=bpmnModel.getMainProcess().getFlowElements().stream().collect(Collectors.toMap(flowElement->flowElement.getId(), Function.identity()));
        historicActivityInstances.forEach( instance->{
            FlowNode flowNode = (FlowNode)bpmnModel.getMainProcess().getFlowElement(instance.getActivityId());
            List<SequenceFlow> pvmTransitions=flowNode.getOutgoingFlows();
            pvmTransitions.forEach(pvm->{
                if(historicActivityDoneList.contains(pvm.getSourceRef()) && historicActivityInstanceMap.get(pvm.getSourceRef())!=null){
                    if(taskIds !=null && !taskIds.isEmpty()){
                        if(!taskIds.contains(historicActivityInstanceMap.get(pvm.getSourceRef()).getTaskId())){
                            if(flowElementMap.get(pvm.getTargetRef()) instanceof ExclusiveGateway){
                                if(querySequenceFlowCondition(pvm,historicVariableInstanceMap)){
                                    highFlows.add(pvm.getId());
                                }
                            }else{
                                //还有任务，只选择结束之前的线
                                if(!(flowElementMap.get(pvm.getTargetRef()) instanceof EndEvent)){
                                    if(historicActivityInstanceMap.get(pvm.getTargetRef())!=null
                                            && querySequenceFlowCondition(pvm,historicVariableInstanceMap)){
                                        highFlows.add(pvm.getId());
                                    }
                                }
                            }
                        }
                    } else {
                        if (historicActivityInstanceMap.get(pvm.getTargetRef()) != null && querySequenceFlowCondition(pvm, historicVariableInstanceMap)) {
                            highFlows.add(pvm.getId());
                        }
                    }
                }
            });
        });
        return BeanUtils.beanTypetransform(highFlows,String.class);
    }

    /**
     * 返回结果：输出图像
     * @param outputStream
     * @param bpmnModel
     * @param flowIds
     * @param executedActivityIdList
     */
    @Override
    public void outputImg(OutputStream outputStream,BpmnModel bpmnModel,List<String> flowIds,List<String> executedActivityIdList){
        InputStream imageStream = null;
        try {
            imageStream = new DefaultProcessDiagramGenerator().generateDiagram(bpmnModel, executedActivityIdList, flowIds, "宋体", "微软雅黑", "黑体", true, "png");
            // 输出资源内容到相应对象
            byte[] b = new byte[1024];
            int len;
            while ((len = imageStream.read(b, 0, 1024)) != -1) {
                outputStream.write(b, 0, len);
            }
            outputStream.flush();
        } catch ( Exception e){
            log.error("输出图像发生异常：{}",e);
        } finally {
            try {
                if (imageStream != null) {
                    imageStream.close();
                }
            } catch (IOException e) {
                log.error("IoException", e);
            }
        }
    }



    /**
     * 获取下一个节点信息
     * @param bpmnModel   流程模型
     * @param historicActivityInstanceMap
     * @param i   当前以遍历到的历史节点索引（找下一个节点从此节点后）
     * @param historicActivityInstance
     * @return
     */
    @Override
    public BaseRpcResult getNextFlowNode(BpmnModel bpmnModel,Map<String,HistoricActivityInstanceDto> historicActivityInstanceMap
            ,int i,HistoricActivityInstanceDto historicActivityInstance){
        // 保存后一个节点
        List<FlowNode> flowNodeList=new ArrayList<>();
        // 如果当前节点不是用户任务节点，则取排序的下一个节点为后续节点
        // 是最后一个节点，没有下一个节点
        if(i == historicActivityInstanceMap.size()){
            return BaseRpcResult.builder().data(flowNodeList).build();
        }
        // 不是最后一个节点，取下一个节点为后继节点
        FlowNode activityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId());
        // 取出节点的所有出去的线，对所有的线进行遍历
        List<SequenceFlow> pvmTransitions = activityImpl.getOutgoingFlows();
        if (pvmTransitions.size() == 1) {
            if (historicActivityInstanceMap.get(pvmTransitions.get(0).getTargetRef()) != null) {
                FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstanceMap.get(pvmTransitions.get(0).getTargetRef()).getActivityId());
                flowNodeList.add(flowNode);
                return BaseRpcResult.builder().data(flowNodeList).build();
            }

        } else {
            for (SequenceFlow sequenceFlow : pvmTransitions) {
                FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(sequenceFlow.getTargetRef());
                flowNodeList.add(flowNode);
            }
            // 返回
            return BaseRpcResult.builder().data(flowNodeList).build();
        }
        return BaseRpcResult.builder().data(flowNodeList).build();
    }

    /**
     * 查询流程历史信息
     * @param processDefinitionKey  流程key
     * @return
     */
    @Override
    public BaseRpcResult<List<HistoricActivityInstanceDto>> queryHistory(String processDefinitionKey){
        List<HistoricActivityInstance> list=new ArrayList<>();
        ProcessDefinition definition =repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).singleResult();
        //遍历查询结果
        if(definition==null){
            list= historyService.createHistoricActivityInstanceQuery().processDefinitionId(definition.getId()).list();
        }
        return BeanUtils.beanTypetransform(list,HistoricActivityInstanceDto.class);
    }

    /**
     * 查询流程是否完成
     * @param processInstanceId
     * @return
     */
    @Override
    public boolean judgeProcessInstanceFinished(String processInstanceId){
        return historyService.createHistoricActivityInstanceQuery().finished().processInstanceId(processInstanceId).count()>0;
    }

    /**
     * 根据processInstanceId获取历史流程变量
     * @param processInstanceId
     * @return
     */
    private Map<String,HistoricVariableInstance> queryHistoricVariableInstanceMap(String processInstanceId){
        List<HistoricVariableInstance> historicVariableInstances=historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId).list();
        return historicVariableInstances.stream()
                .collect(Collectors.toMap(HistoricVariableInstance::getVariableName,
                        historicVariableInstance -> historicVariableInstance,
                        BinaryOperator.maxBy(Comparator.comparing(HistoricVariableInstance::getId))));
    }

    /**
     *
     * @param pvmTransition
     * @param historicVariableInstanceMap
     * @return
     */
    private boolean querySequenceFlowCondition(SequenceFlow pvmTransition, Map<String, HistoricVariableInstance> historicVariableInstanceMap) {
        String conditionExpression = pvmTransition.getConditionExpression();
        if (StringUtils.isNotEmpty(conditionExpression) && StringUtils.isNotBlank(conditionExpression)) {
            conditionExpression = conditionExpression.substring(conditionExpression.indexOf('{') + 1, conditionExpression.indexOf('}'));
            List<String> strings = Arrays.asList(conditionExpression.split("=="));
            strings.forEach(s -> s = s.replace(" ", ""));
            if (historicVariableInstanceMap.get(strings.get(0)).getValue().equals(strings.get(1).replaceAll("\"", ""))) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }



}
