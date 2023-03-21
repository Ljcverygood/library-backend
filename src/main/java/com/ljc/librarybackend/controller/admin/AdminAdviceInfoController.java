package com.ljc.librarybackend.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljc.librarybackend.pojo.entity.AdviceInfo;
import com.ljc.librarybackend.service.AdviceInfoService;
import com.ljc.librarybackend.utils.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin/adviceInfo")
@Api("管理员建议反馈界面")
public class AdminAdviceInfoController {

    @Autowired
    private AdviceInfoService adviceInfoService;

    @PostMapping("/getAdvice/{currentPage}/{pageSize}")
    @ApiOperation("获取读者反馈列表")
    public ResultModel getAdvice(@PathVariable Integer currentPage,@PathVariable Integer pageSize){
        try {

            Page<AdviceInfo> page=new Page<>(currentPage,pageSize);
            Page<AdviceInfo> adviceInfoPage = adviceInfoService.page(page,new LambdaQueryWrapper<AdviceInfo>().orderByDesc(AdviceInfo::getCreateTime));
            return ResultModel.success("查询成功！",adviceInfoPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！");
        }
    }

}
