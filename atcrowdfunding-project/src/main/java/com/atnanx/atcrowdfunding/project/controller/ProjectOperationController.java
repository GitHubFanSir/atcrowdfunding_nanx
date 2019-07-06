package com.atnanx.atcrowdfunding.project.controller;

import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import com.atnanx.atcrowdfunding.project.service.IReturnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "项目操作模块")
@RequestMapping("/project/operation")
@RestController
public class ProjectOperationController {
    @Autowired
    private IReturnService returnService;

    @ApiOperation("项目删除")
    @DeleteMapping("/delete")
    public ServerResponse<String> delete(){
        return null;
    }

    @ApiOperation("项目预览")
    @GetMapping("/preshow")
    public ServerResponse<String> preshow(){
        return null;
    }

    @ApiOperation("项目问题查看")
    @GetMapping("/question")
    public ServerResponse<String> question(){
        return null;
    }

    @ApiOperation("项目问题添加")
    @PostMapping("/question")
    public ServerResponse<String> addQuestion(){
        return null;
    }

    @ApiOperation("项目问题答案添加")
    @PostMapping("/question/answer")
    public ServerResponse<String> addQuestionAnswer(){
        return null;
    }

    @ApiOperation("项目关注")
    @PostMapping("/star")
    public ServerResponse<String> star(){
        return null;
    }

    @ApiOperation("项目取消关注")
    @DeleteMapping("/star")
    public ServerResponse<String> deletestar(){
        return null;
    }

    @ApiOperation("项目修改")
    @PostMapping("/update")
    public ServerResponse<String> update(){
        return null;
    }

    @ApiOperation("项目修改")
    @PostMapping("/update_return")
    public ServerResponse reduceProjectReturnStock(Integer rtnCount,Integer returnId){
        return returnService.reduceProjectReturnStock(rtnCount,returnId);
    }
}

