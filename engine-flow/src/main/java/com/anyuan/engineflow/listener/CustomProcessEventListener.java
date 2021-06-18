package com.anyuan.engineflow.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.api.model.shared.event.RuntimeEvent;
import org.activiti.api.model.shared.event.VariableCreatedEvent;
import org.activiti.api.process.model.events.SequenceFlowEvent;
import org.activiti.api.process.runtime.events.*;
import org.activiti.api.process.runtime.events.listener.ProcessRuntimeEventListener;
import org.springframework.stereotype.Service;

/**
 * 自定义流程运行时信息
 * @author liangjy on 2021/4/15.
 */
@Slf4j
@Service
public class CustomProcessEventListener implements ProcessRuntimeEventListener {
    @Override
    public void onEvent(RuntimeEvent runtimeEvent) {
        if (runtimeEvent instanceof ProcessStartedEvent)
            log.info("Do something, process is started: " + runtimeEvent.toString());
        else if (runtimeEvent instanceof ProcessCompletedEvent)
            log.info("Do something, process is completed: " + runtimeEvent.toString());
        else if (runtimeEvent instanceof ProcessCancelledEvent)
            log.info("Do something, process is cancelled: " + runtimeEvent.toString());
        else if (runtimeEvent instanceof ProcessSuspendedEvent)
            log.info("Do something, process is suspended: " + runtimeEvent.toString());
        else if (runtimeEvent instanceof ProcessResumedEvent)
            log.info("Do something, process is resumed: " + runtimeEvent.toString());
        else if (runtimeEvent instanceof ProcessCreatedEvent)
            log.info("Do something, process is created: " + runtimeEvent.toString());
        else if (runtimeEvent instanceof SequenceFlowEvent)
            log.info("Do something, sequence flow is taken: " + runtimeEvent.toString());
        else if (runtimeEvent instanceof VariableCreatedEvent)
            log.info("Do something, variable was created: " + runtimeEvent.toString());
        else
            log.info("Unknown event: " + runtimeEvent.toString());
    }
}
