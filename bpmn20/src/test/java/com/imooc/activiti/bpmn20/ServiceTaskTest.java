package com.imooc.activiti.bpmn20;

import com.google.common.collect.Maps;
import org.activiti.engine.ActivitiEngineAgenda;
import org.activiti.engine.ManagementService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class ServiceTaskTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTaskTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process-servicetask1.bpmn20.xml"})
    public void testServiceTask() {
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        List<HistoricActivityInstance> historicActivityInstances = activitiRule.getHistoryService().createHistoricActivityInstanceQuery().orderByHistoricActivityInstanceEndTime().asc().list();
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances){
            LOGGER.info("activiti = {}",historicActivityInstance);
        }
        Execution execution = activitiRule.getRuntimeService().createExecutionQuery().activityId("someTask").singleResult();
        LOGGER.info("execution = {}",execution);

        ManagementService managementService = activitiRule.getManagementService();
        managementService.executeCommand(new Command<Object>() {
            @Override
            public Object execute(CommandContext commandContext) {
                ActivitiEngineAgenda agenda = commandContext.getAgenda();
                agenda.planTakeOutgoingSequenceFlowsOperation((ExecutionEntity) execution,false);
                return null;
            }
        });

        historicActivityInstances = activitiRule.getHistoryService().createHistoricActivityInstanceQuery().orderByHistoricActivityInstanceEndTime().asc().list();
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances){
            LOGGER.info("activiti = {}",historicActivityInstance);
        }
    }


    @Test
    @Deployment(resources = {"my-process-servicetask2.bpmn20.xml"})
    public void testServiceTask2() {
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        List<HistoricActivityInstance> historicActivityInstances = activitiRule.getHistoryService().createHistoricActivityInstanceQuery().orderByHistoricActivityInstanceEndTime().asc().list();
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances){
            LOGGER.info("activiti = {}",historicActivityInstance);
        }
    }


    @Test
    @Deployment(resources = {"my-process-servicetask3.bpmn20.xml"})
    public void testServiceTask3() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("desc","the test java delegate");
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process",variables);
        List<HistoricActivityInstance> historicActivityInstances = activitiRule.getHistoryService().createHistoricActivityInstanceQuery().orderByHistoricActivityInstanceEndTime().asc().list();
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances){
            LOGGER.info("activiti = {}",historicActivityInstance);
        }
    }

}
