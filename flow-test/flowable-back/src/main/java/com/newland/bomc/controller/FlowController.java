package com.newland.bomc.controller;

import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * @Auther: AlexChen
 * @Date: 2020/8/21
 * @Description:
 */
@Controller
@RequestMapping(value = "flow")
public class FlowController {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;

    @Qualifier("processEngine")
    @Autowired
    private ProcessEngine processEngine;

    @RequestMapping(value = "add")
    @ResponseBody
    public String addExpense(String userId, Integer money, String descption) {
        //启动流程
        HashMap<String, Object> map = new HashMap<>();
        map.put("taskUser", userId);
        map.put("money", money);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Expense", map);

        ProcessDefinitionEntity processDefinitionEntity = null;


        return "提交成功.流程Id为：" + processInstance.getId();
    }

    @GetMapping(value = "deplay")
    @ResponseBody
    public String deploy(){
        RepositoryService repositoryService=processEngine.getRepositoryService();
        //加载流程
        Deployment deployment=repositoryService.createDeployment()
                .addClasspathResource("holiday-request.bpmn20.xml")
                .deploy();
        return "deploy";
    }

    @RequestMapping(value = "node",method = RequestMethod.GET)
    @ResponseBody
    public String getNodeList(@RequestParam String id){

        try {
            ProcessDefinition processDefinition = repositoryService.getProcessDefinition("Expense:10:781cf3d9-ec30-11ea-b41b-2ae3478a2d4e");
            processDefinition.getDeploymentId();
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
            Process process = bpmnModel.getProcesses().get(0);
            Collection<FlowElement> flowElements = process.getFlowElements();
            for (FlowElement flowElement : flowElements) {
                if (flowElement instanceof UserTask) {
                    UserTask u = (UserTask) flowElement;
                    List<SequenceFlow> outgoingFlows = u.getOutgoingFlows();
                    System.err.println("outgoingFlows:" + outgoingFlows);
                }
                System.err.println(flowElement.getId() + "--->>>>"
                        + flowElement.getName());

            }
        }catch (Exception e){
            e.printStackTrace();
        }


        return "node";
    }




}
