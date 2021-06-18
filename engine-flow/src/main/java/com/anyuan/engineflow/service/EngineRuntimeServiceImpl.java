package com.anyuan.engineflow.service;

import com.anyuan.commons.base.BaseRpcResult;
import com.anyuan.commons.dto.ProcessInstanceDto;
import com.anyuan.commons.service.flowengine.EngineRuntimeService;
import com.anyuan.engineflow.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.dubbo.config.annotation.Service;

import java.util.List;
import java.util.Map;

/**
 * 执行管理，包括启动、推进、删除流程实例等操作
 * @author liangjy on 2021/4/14.
 */
@Slf4j
@Service
public class EngineRuntimeServiceImpl implements EngineRuntimeService {

    @Autowired
    private RuntimeService runtimeService;

    /**
     * 启动流程实例
     */
    @Override
    public void startFlow(String processDefinitionKey){
        ProcessInstance instance=runtimeService.startProcessInstanceByKey(processDefinitionKey);
        log.info("启动已定义的流程实例:instanceID:{}",instance.getProcessInstanceId());
    }

    /**
     * 启动流程实例（带变量的）
     */
    @Override
    public BaseRpcResult<ProcessInstanceDto> startFlow(String processDefinitionKey, Map<String,Object> variables){
        ProcessInstance instance=runtimeService.startProcessInstanceByKey(processDefinitionKey,variables);
        log.info("启动已定义的流程实例:instanceID:{}",instance.getProcessInstanceId());
        return BeanUtils.beanTypetransform(instance,ProcessInstanceDto.class);
    }

    /**
     * 查询本人发起的执行中的任务
     * @param userId
     * @return
     */
    @Override
    public BaseRpcResult<List<ProcessInstanceDto>> queryUserStartedProcessInstance(String userId){
        List<ProcessInstance> list=runtimeService.createProcessInstanceQuery().startedBy(userId).list();
        return BeanUtils.beanTypetransform(list,ProcessInstanceDto.class);
    }

    /**
     * 查看流程是否结束
     */
    @Override
    public void isFlowEnd(String instanceId){
        ProcessInstance instance=runtimeService.createProcessInstanceQuery()
                .processInstanceId(instanceId)
                .singleResult();
        if(instance==null){
            log.info("流程已经结束");
        }else{
            log.info("流程尚未结束");
        }
    }

}
