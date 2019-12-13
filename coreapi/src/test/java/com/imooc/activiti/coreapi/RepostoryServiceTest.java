package com.imooc.activiti.coreapi;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RepostoryServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepostoryServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg.xml");

    @Test
    public void TestRepostory(){
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.name("测试部署资源").addClasspathResource("my-process.bpmn20.xml").addClasspathResource("second_approve.bpmn20.xml");
        Deployment deploy = deploymentBuilder.deploy();
        LOGGER.info("deploy = {}",deploy);

        DeploymentBuilder deploymentBuilder2 = repositoryService.createDeployment();
        deploymentBuilder2.name("测试部署资源2").addClasspathResource("my-process.bpmn20.xml").addClasspathResource("second_approve.bpmn20.xml");
        Deployment deploy2 = deploymentBuilder2.deploy();
        LOGGER.info("deploy2 = {}",deploy2);


        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        List<Deployment> deploymentList = deploymentQuery
                //.deploymentId(deploy.getId())
                .orderByDeploymenTime().asc()
                //.singleResult();
                .listPage(0,100);
        for (Deployment deployment : deploymentList){
            LOGGER.info("deployment = {}",deployment);
        }
        LOGGER.info("deploymentList.size = {}",deploymentList.size());
        List<ProcessDefinition> definitionList = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionKey().asc()
                //.deploymentId(deployment.getId())
            .listPage(0, 100);

        for (ProcessDefinition processdefinition : definitionList){
            LOGGER.info("processDefinition = {},version = {},key = {},id = {}",processdefinition,processdefinition.getVersion(),processdefinition.getKey(),processdefinition.getId());
        }

    }

    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testSuspend(){
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        LOGGER.info("processDefinition.id = {}",processDefinition.getId());
        repositoryService.suspendProcessDefinitionById(processDefinition.getId());
        try {
            LOGGER.info("启动");
            activitiRule.getRuntimeService().startProcessInstanceById(processDefinition.getId());
            LOGGER.info("启动成功");
        } catch (Exception e) {
            LOGGER.info("启动失败");
            LOGGER.error(e.getMessage(),e);
        }
        repositoryService.activateProcessDefinitionById(processDefinition.getId());
        LOGGER.info("开始启动");
        activitiRule.getRuntimeService().startProcessInstanceById(processDefinition.getId());
        LOGGER.info("启动成功");
    }


    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testCandidateStarter(){
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        LOGGER.info("processDefinition.id = {}",processDefinition.getId());
       repositoryService.addCandidateStarterUser(processDefinition.getId(),"user");
       repositoryService.addCandidateStarterGroup(processDefinition.getId(),"groupM");

        List<IdentityLink> identityLinkList = repositoryService.getIdentityLinksForProcessDefinition(processDefinition.getId());
        for (IdentityLink identityLink : identityLinkList) {
            LOGGER.info("identityLink = {}",identityLink);
        }
        repositoryService.deleteCandidateStarterGroup(processDefinition.getId(),"groupM");
        repositoryService.deleteCandidateStarterUser(processDefinition.getId(),"user");


    }
}
