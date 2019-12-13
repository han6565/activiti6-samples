package com.imooc.activiti.coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.util.CollectionUtil;
import org.activiti.engine.task.*;
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

public class TaskServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg.xml");

    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskService(){
        Map<String,Object> variables = Maps.newHashMap();
        variables.put("message","my test message !!!");
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", variables);
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        LOGGER.info("task = {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        LOGGER.info("task.description = {}", task.getDescription());

        taskService.setVariable(task.getId(),"key1","value1");
        taskService.setVariableLocal(task.getId(),"localkey1","localvalue1");

        Map<String, Object> taskServiceVariables = taskService.getVariables(task.getId());
        Map<String, Object> taskServiceVariablesLocal = taskService.getVariablesLocal(task.getId());
        Map<String, Object> processVariables = activitiRule.getRuntimeService().getVariables(task.getExecutionId());
        LOGGER.info("taskServiceVariables = {}",taskServiceVariables);
        LOGGER.info("taskServiceVariablesLocal = {}",taskServiceVariablesLocal);
        LOGGER.info("processVariables = {}",processVariables);

        Map<String,Object> completeVar = Maps.newHashMap();
        completeVar.put("cKey1","cValue1");
        taskService.complete(task.getId(),completeVar);

        Task task1 = taskService.createTaskQuery().taskId(task.getId()).singleResult();
        LOGGER.info("task1 = {}",task1);
    }


    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskServiceUser(){
        Map<String,Object> variables = Maps.newHashMap();
        variables.put("message","my test message !!!");
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", variables);
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        LOGGER.info("task = {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        LOGGER.info("task.description = {}", task.getDescription());

        taskService.setOwner(task.getId(),"user1");
        //taskService.setAssignee(task.getId(),"jimmy");
        List<Task> taskList = taskService.createTaskQuery().taskCandidateUser("jimmy").taskUnassigned().listPage(0, 100);

        for (Task task1 :taskList){
            try {
                taskService.claim(task1.getId(),"jimmy");
            } catch (Exception e) {
                LOGGER.error(e.getMessage(),e);
            }
        }

        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(task.getId());
        for (IdentityLink identityLink : identityLinksForTask) {
            LOGGER.info("identityLink = {}",identityLink);
        }

        List<Task> jimmys = taskService.createTaskQuery().taskAssignee("jimmy").listPage(0, 100);
        for (Task jimmy : jimmys){
            Map<String,Object> vars = Maps.newHashMap();
            vars.put("cKey1","cvalue1");
            taskService.complete(jimmy.getId(),vars);
        }

        jimmys = taskService.createTaskQuery().taskAssignee("jimmy").listPage(0,100);
        LOGGER.info("是否存在 {}", CollectionUtil.isEmpty(jimmys));
    }

    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskAttachment(){
        Map<String,Object> variables = Maps.newHashMap();
        variables.put("message","my test message !!!");
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", variables);
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();

        taskService.createAttachment("url",task.getId(),task.getProcessInstanceId(),"name","desc","/url/test.png");

        List<Attachment> taskAttachments = taskService.getTaskAttachments(task.getId());

        for (Attachment attachment : taskAttachments){
            LOGGER.info("taskAttachment = {}",ToStringBuilder.reflectionToString(attachment,ToStringStyle.JSON_STYLE));
        }
    }


    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskComment(){
        Map<String,Object> variables = Maps.newHashMap();
        variables.put("message","my test message !!!");
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", variables);
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        taskService.setOwner(task.getId(),"user1");
        taskService.setAssignee(task.getId(),"jimmy");

        taskService.addComment(task.getId(),task.getProcessInstanceId(),"record note 1");
        taskService.addComment(task.getId(),task.getProcessInstanceId(),"record note 2");

        List<Comment> comments = taskService.getTaskComments(task.getId());

        for (Comment comment : comments){
            LOGGER.info("comment = {}",ToStringBuilder.reflectionToString(comment,ToStringStyle.JSON_STYLE));
        }

        List<Event> taskEvents = taskService.getTaskEvents(task.getId());
        for (Event taskEvent : taskEvents){
            LOGGER.info("taskEvent = {}",ToStringBuilder.reflectionToString(taskEvent,ToStringStyle.JSON_STYLE));
        }
    }
}
