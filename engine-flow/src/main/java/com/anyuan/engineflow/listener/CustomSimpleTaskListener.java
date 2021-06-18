package com.anyuan.engineflow.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @author liangjy on 2021/4/22.
 */
@Slf4j
public class CustomSimpleTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("task notify:{}",delegateTask);
        delegateTask.setOwner("liangjianyong");
        delegateTask.setAssignee("liangjianyong");
    }
}
