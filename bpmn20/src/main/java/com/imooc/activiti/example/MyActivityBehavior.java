package com.imooc.activiti.example;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.delegate.ActivityBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class MyActivityBehavior implements ActivityBehavior, Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyActivityBehavior.class);

    @Override
    public void execute(DelegateExecution delegateExecution) {
        LOGGER.info("run my activity behavior ");
    }
}
