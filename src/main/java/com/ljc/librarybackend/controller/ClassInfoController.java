package com.ljc.librarybackend.controller;


import com.ljc.librarybackend.pojo.entity.ClassInfo;
import com.ljc.librarybackend.service.ClassInfoService;
import com.ljc.librarybackend.utils.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ljc
 * @since 2023-02-27
 */
@RestController
@RequestMapping("/classInfo")
@Api("分类信息表")
public class ClassInfoController {

    @Autowired
    private ClassInfoService classInfoService;



    @GetMapping("/getClassInfo")
    @ApiOperation("获取分类信息")
    public ResultModel getClassInfo() {
        try {
            List<ClassInfo> list = classInfoService.list();
            return ResultModel.success("获取分类信息成功！",list);
        } catch (Exception e) {
            return ResultModel.error("获取分类信息失败！");
        }
    }



}

