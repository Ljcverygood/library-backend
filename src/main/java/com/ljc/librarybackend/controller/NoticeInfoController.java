package com.ljc.librarybackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljc.librarybackend.pojo.entity.NoticeInfo;
import com.ljc.librarybackend.service.NoticeInfoService;
import com.ljc.librarybackend.utils.NoticeInfoQuery;
import com.ljc.librarybackend.utils.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reader/noticeInfo")
@Api("公告管理")
@CrossOrigin
@Slf4j
public class NoticeInfoController {

    @Autowired
    private NoticeInfoService noticeInfoService;
    //读者获取公告列表
    @PostMapping("/getNotice/{currentPage}/{pageSize}")
    @ApiOperation("获取公告列表")
    public ResultModel getNotice(@PathVariable Integer currentPage,@PathVariable Integer pageSize) {
        try {
            Page<NoticeInfo> noticeInfoPage=new Page<>(currentPage,pageSize);
            Page<NoticeInfo> page = noticeInfoService.page(noticeInfoPage,new LambdaQueryWrapper<NoticeInfo>().orderByDesc(NoticeInfo::getCreateTime));
            return ResultModel.success("查询成功！",page);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！");
        }
    }

    @PostMapping("/pubNotice")
    @ApiOperation("发布公告")
    public ResultModel pubNotice(@RequestBody NoticeInfo noticeInfo){
        try {
            noticeInfoService.save(noticeInfo);
            return ResultModel.success("发布公告成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！");
        }
    }

    @DeleteMapping("/deleteById/{id}")
    @ApiOperation("根据id删除公告")
    public ResultModel deleteById(@PathVariable Long id){
        try {
            noticeInfoService.removeById(id);
            return ResultModel.success("删除公告成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！");
        }
    }

    @GetMapping("/getById/{id}")
    @ApiOperation("根据id获取公告信息")
    public ResultModel getById(@PathVariable Long id){
        try {
            NoticeInfo noticeInfo = noticeInfoService.getById(id);
            return ResultModel.success("查询成功！",noticeInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！");
        }
    }
}
