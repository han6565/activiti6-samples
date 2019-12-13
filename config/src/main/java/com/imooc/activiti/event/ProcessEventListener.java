package com.imooc.activiti.event;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessEventListener implements ActivitiEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessEventListener.class);

    @Override
    public void onEvent(ActivitiEvent activitiEvent) {
        ActivitiEventType eventType = activitiEvent.getType();
        if(ActivitiEventType.PROCESS_STARTED.equals(eventType)){
            LOGGER.info("流程启动 {} \t {}",eventType,activitiEvent.getProcessInstanceId());
        }else if(ActivitiEventType.PROCESS_COMPLETED.equals(eventType)){
            LOGGER.info("流程完成 {} \t {}",eventType,activitiEvent.getProcessInstanceId());
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
