package com.imooc.activiti.example;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class MyJavaDelegate implements JavaDelegate, Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyJavaDelegate.class);

    private Expression name;
    private Expression desc;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        if(name != null) {
            Object value = name.getValue(delegateExecution);
            LOGGER.info("name = {}",value);
        }
        if(desc != null) {
            Object value = desc.getValue(delegateExecution);
            LOGGER.info("desc = {}",value);
        }
        LOGGER.info("run my java delegate {}",this);

    }
}
