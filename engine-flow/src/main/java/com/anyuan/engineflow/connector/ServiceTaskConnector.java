package com.anyuan.engineflow.connector;

import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.model.IntegrationContext;
import org.activiti.api.process.runtime.connector.Connector;
import org.springframework.stereotype.Service;

/**
 * 连接器
 * 描述：实现服务任务的连接器
 * @author liangjy on 2021/4/14.
 */
@Slf4j
@Service("serviceTaskImpl")
public class ServiceTaskConnector implements Connector {

    @Override
    public IntegrationContext apply(IntegrationContext integrationContext) {
        log.info("Some service task logic... [processInstanceId=" + integrationContext.getProcessInstanceId() + "]");
        return integrationContext;
    }

}
