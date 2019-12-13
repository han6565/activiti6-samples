package com.imooc.activiti.coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;

public class HistoryServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FormServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_history.cfg.xml");

    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testHistory(){
        HistoryService historyService = activitiRule.getHistoryService();

        ProcessInstanceBuilder processInstanceBuilder = activitiRule.getRuntimeService().createProcessInstanceBuilder();

        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key0","value0");
        variables.put("key1","value1");
        variables.put("key2","value2");
        Map<String, Object> transientVariable = Maps.newHashMap();
        transientVariable.put("tkey1","tvalue1");
        ProcessInstance processInstance = processInstanceBuilder.processDefinitionKey("my-process").variables(variables).transientVariables(transientVariable).start();
        activitiRule.getRuntimeService().setVariable(processInstance.getId(),"key1","value1_1");

        Task task = activitiRule.getTaskService().createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        Map<String, String> properties = Maps.newHashMap();
        properties.put("fkey1","fValue1");
        properties.put("key2","value_2_2");

        activitiRule.getFormService().submitTaskFormData(task.getId(),properties);

        List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery().listPage(0, 100);
        for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
            LOGGER.info("historicProcessInstance = {}", ToStringBuilder.reflectionToString(historicProcessInstance, ToStringStyle.JSON_STYLE));
        }

        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery().listPage(0, 100);
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            LOGGER.info("historicActivityInstance = {}",historicActivityInstance);
        }

        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().listPage(0, 100);
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
            LOGGER.info("historicTaskInstance = {}",ToStringBuilder.reflectionToString(historicTaskInstance,ToStringStyle.JSON_STYLE));
        }

        List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery().listPage(0, 100);
        for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
            LOGGER.info("historicVariableInstance = {}",historicVariableInstance);
        }

        List<HistoricDetail> historicDetails = historyService.createHistoricDetailQuery().listPage(0, 100);
        for (HistoricDetail historicDetail : historicDetails) {
            LOGGER.info("historicDetail = {}",historicDetail);
        }

        ProcessInstanceHistoryLog processInstanceHistoryLog = historyService.createProcessInstanceHistoryLogQuery(processInstance.getId()).includeVariables().includeFormProperties().includeComments().includeTasks().includeActivities().includeVariableUpdates().singleResult();
        List<HistoricData> historicDataList = processInstanceHistoryLog.getHistoricData();
        for (HistoricData historicData : historicDataList){
            LOGGER.info("historicData = {}",historicData);
        }

        historyService.deleteHistoricProcessInstance(processInstance.getId());

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();
        LOGGER.info("historicProcessInstance = {}",historicProcessInstance);

    }

}
