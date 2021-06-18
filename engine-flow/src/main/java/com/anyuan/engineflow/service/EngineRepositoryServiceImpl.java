package com.anyuan.engineflow.service;

import com.anyuan.commons.base.BaseRpcResult;
import com.anyuan.commons.constants.Constants;
import com.anyuan.commons.dto.DeploymentDto;
import com.anyuan.commons.dto.ProcessDefinitionDto;
import com.anyuan.commons.service.flowengine.EngineRepositoryService;
import com.anyuan.engineflow.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 管理流程定义
 * @author liangjy on 2021/4/14.
 */
@Slf4j
@Service
public class EngineRepositoryServiceImpl implements EngineRepositoryService {

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 自动扫描，部署流程 (遍历diagram目录下bpmn,定义流程)
     * @return
     */
    @Override
    public BaseRpcResult<List<String>> autoDeployFlow(){
        List<String> list=new ArrayList<>();
        log.info("正在部署流程-> STEP 1:正在扫描文件");
        try {
            String diagramPath=ResourceUtils.getURL("classpath:diagram").getPath();
            File diagramDirectory=new File(diagramPath);
            Assert.state(diagramDirectory.exists() && diagramDirectory.isDirectory(),"diagram目录不存在");
            log.info("正在部署流程-> STEP 2:准备部署流程");
            Arrays.stream(diagramDirectory.listFiles()).filter(f-> StringUtils.endsWithIgnoreCase(f.getName(),"bpmn")).forEach(file->{
                String fName=file.getName().substring(0,file.getName().lastIndexOf("."));
                log.info("--正在部署流程-> 流程名：{}",fName);
                deployFlow(fName,fName);
                list.add(fName);
            });
            log.info("流程部署成功-> ...");
        } catch (FileNotFoundException e) {
            log.error("读取不到diagram 目录：{}",e);
        }
        return BeanUtils.beanTypetransform(list,String.class);
    }


    /**
     * 流程部署
     * @param processDefineKey  流程定义的key:  如holiday
     * @param processDefineName  流程定义名称:  如请假申请
     * 默认文件放在 resource/daigram 目录下  key.bpmn   key.png  包含这两个文件
     * @return
     */
    @Override
    public BaseRpcResult<DeploymentDto> deployFlow(String processDefineKey, String processDefineName){
        return deployFlow(processDefineKey,processDefineName, Constants.DEFAULT_ACTIVITI_FOLDER_NAME+processDefineKey+".bpmn"
                , Constants.DEFAULT_ACTIVITI_FOLDER_NAME+processDefineKey+".png");
    }

    /**
     * 流程部署
     * @param processDefineKey  流程定义的key:  如holiday
     * @param processDefineName  流程定义名称:  如请假申请
     * @param bpmnPath bpmn文件本地路径
     * @param imgPath png 文件本地路径
     * @return
     */
    @Override
    public BaseRpcResult<DeploymentDto> deployFlow(String processDefineKey, String processDefineName, String bpmnPath, String imgPath){
        log.info("正在部署流程-> .....");
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(bpmnPath)
                .addClasspathResource(imgPath)
                .name(processDefineName)
                .deploy();
        log.info("流程部署成功->流程部署id:{}   流程部署名称:{}",deployment.getName(),deployment.getId());
        return BeanUtils.beanTypetransform(deployment,DeploymentDto.class);
    }

    /**
     * 根据部署ID获取图片
     * @param deploymentId  部署ID
     * @return
     */
    @Override
    public BaseRpcResult<Map<String,Object>> viewPng(String deploymentId,String localPath){
        List<String> list=repositoryService.getDeploymentResourceNames(deploymentId);
        //获得资源名称后缀.png
        String resourceName = "";
        if (list != null && list.size() > 0) {
            for (String name : list) {
                //返回包含该字符串的第一个字母的索引位置
                if (name.contains(".png")) {
                    resourceName = name;
                }
            }
        }
        //获取输入流，输入流中存放.PNG的文件
        InputStream in = repositoryService
                .getResourceAsStream(deploymentId, resourceName);
        //将获取到的文件保存到本地
        try {
            FileUtils.copyInputStreamToFile(in, new File(localPath + resourceName));
        } catch (IOException e) {
            log.error("viewPng error", e);
        }
        return null;
    }

    /**
     * 移除流程（包含级联的流程实例和历史记录）
     */
    @Override
    public void removeFlow(String deploymentId){
        log.info("正在移除流程->流程部署id{}",deploymentId);
        repositoryService.deleteDeployment(deploymentId);
        log.info("流程移除成功->流程部署id:{} 已被删除",deploymentId);
    }

    /**
     * 根据启动key获取最新流程
     * @param processDefinitionKey
     * @return
     */
    @Override
    public BaseRpcResult<List<ProcessDefinitionDto>> queryLastestProcess(String processDefinitionKey){
        List<ProcessDefinition> list= repositoryService.createProcessDefinitionQuery().processDefinitionCategory(processDefinitionKey)
                .orderByProcessDefinitionVersion().desc().list();
        return BeanUtils.beanTypetransform(list,ProcessDefinitionDto.class);
    }


}
