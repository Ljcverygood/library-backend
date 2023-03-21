package com.ljc.librarybackend.controller;

import com.ljc.librarybackend.pojo.entity.AdviceInfo;
import com.ljc.librarybackend.service.AdviceInfoService;
import com.ljc.librarybackend.utils.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/reader/adviceInfo")
@Api("读者建议反馈界面")
public class AdviceInfoController {

    @Autowired
    private AdviceInfoService adviceInfoService;

    @PostMapping("/saveAdvice")
    @ApiOperation("保存用户建议")
    public ResultModel saveAdvice(@RequestBody AdviceInfo adviceInfo, HttpServletRequest request){
        try {
            //获取当前登录读者id
            Integer id = (Integer) request.getSession().getAttribute("id");
            adviceInfo.setReaderId(Long.valueOf(id));
            adviceInfoService.save(adviceInfo);
            return ResultModel.success("反馈成功！感谢您提出的宝贵建议！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！");
        }
    }

}
