package com.imooc.activiti.delegate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloBean.class);
    public void sayHello(){
        LOGGER.info("sayHello");
    }
}
