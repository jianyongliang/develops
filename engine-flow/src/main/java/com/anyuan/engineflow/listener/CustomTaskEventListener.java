package com.anyuan.engineflow.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.api.model.shared.event.RuntimeEvent;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.runtime.events.*;
import org.activiti.api.task.runtime.events.listener.TaskRuntimeEventListener;
import org.springframework.stereotype.Service;

/**
 * 自定义task事件
 * @author liangjy on 2021/4/15.
 */
@Slf4j
@Service
public class CustomTaskEventListener implements TaskRuntimeEventListener {
    @Override
    public void onEvent(RuntimeEvent runtimeEvent) {
        if (runtimeEvent instanceof TaskActivatedEvent)
            log.info("Do something, task is activated: " + runtimeEvent.toString());
        else if (runtimeEvent instanceof TaskAssignedEvent) {
            TaskAssignedEvent taskEvent = (TaskAssignedEvent)runtimeEvent;
            Task task = taskEvent.getEntity();
            log.info("Do something, task is assigned: " + task.toString());
        } else if (runtimeEvent instanceof TaskCancelledEvent)
            log.info("Do something, task is cancelled: " + runtimeEvent.toString());
        else if (runtimeEvent instanceof TaskCompletedEvent)
            log.info("Do something, task is completed: " + runtimeEvent.toString());
        else if (runtimeEvent instanceof TaskCreatedEvent)
            log.info("Do something, task is created: " + runtimeEvent.toString());
        else if (runtimeEvent instanceof TaskSuspendedEvent)
            log.info("Do something, task is suspended: " + runtimeEvent.toString());
        else
            log.info("Unknown event: " + runtimeEvent.toString());
    }
}
