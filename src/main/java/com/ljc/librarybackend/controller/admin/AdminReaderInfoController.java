package com.ljc.librarybackend.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljc.librarybackend.pojo.entity.ReaderCard;
import com.ljc.librarybackend.pojo.entity.ReaderInfo;
import com.ljc.librarybackend.service.ReaderCardService;
import com.ljc.librarybackend.service.ReaderInfoService;
import com.ljc.librarybackend.utils.ReaderInfoQuery;
import com.ljc.librarybackend.utils.ResultModel;
import com.ljc.librarybackend.vo.AdminReaderInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ljc
 * @since 2023-02-27
 */
@RestController
@RequestMapping("/admin/readerInfo")
@Api("读者管理页面")
public class AdminReaderInfoController {

    @Autowired
    private ReaderInfoService readerInfoService;

    @Autowired
    private ReaderCardService readerCardService;

    @PostMapping("/pageQuery")
    @ApiOperation("获取读者信息列表")
    public ResultModel pageQuery(@RequestBody ReaderInfoQuery readerInfoQuery){
        try {
            LambdaQueryWrapper<ReaderInfo> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(readerInfoQuery.getReaderId()!=null,ReaderInfo::getReaderId,readerInfoQuery.getReaderId());
            queryWrapper.like(readerInfoQuery.getUsername()!=null,ReaderInfo::getUsername,readerInfoQuery.getUsername());
            Page<ReaderInfo> pageParam=new Page<>(readerInfoQuery.getCurrentPage(),readerInfoQuery.getPageSize());
            Page<ReaderInfo> readerInfoPage = readerInfoService.page(pageParam, queryWrapper);
            return ResultModel.success("查询成功",readerInfoPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("查询失败！请杀后重试");
        }
    }

    @GetMapping("/getById/{readerId}")
    @ApiOperation("根据id获取读者信息")
    public ResultModel getById(@PathVariable Integer readerId){
        try {
            ReaderInfo readerInfo = readerInfoService.getById(readerId);
            ReaderCard readerCard = readerCardService.getById(readerId);
            AdminReaderInfoVo adminReaderInfoVo=new AdminReaderInfoVo();
            BeanUtils.copyProperties(readerInfo,adminReaderInfoVo);
            BeanUtils.copyProperties(readerCard,adminReaderInfoVo);
            return ResultModel.success("查询成功！",adminReaderInfoVo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！请稍后重试");
        }
    }

    @PostMapping("/addReaderInfo")
    @ApiOperation("新增读者信息")
    public ResultModel addReaderInfo(@RequestBody AdminReaderInfoVo adminReaderInfoVo){
        try {
            //先查询数据库中是否已存在表单中输入的邮箱
            LambdaQueryWrapper<ReaderCard> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(ReaderCard::getEmail,adminReaderInfoVo.getEmail());
            ReaderCard readerCard1 = readerCardService.getOne(queryWrapper);
            if (readerCard1==null) {
                ReaderInfo readerInfo=new ReaderInfo();
                ReaderCard readerCard=new ReaderCard();
                BeanUtils.copyProperties(adminReaderInfoVo,readerInfo);
                BeanUtils.copyProperties(adminReaderInfoVo,readerCard);
                readerInfoService.save(readerInfo);
                readerCardService.save(readerCard);
                return ResultModel.success("新增读者成功！");
            } else {
                return ResultModel.error("邮箱已存在！");
            }
        } catch (BeansException e) {
            e.printStackTrace();
            return ResultModel.error("新增读者失败！");
        }
    }

    @PutMapping("/updateReaderInfo")
    @ApiOperation("更新读者信息接口")
    public ResultModel updateReaderInfo(@RequestBody AdminReaderInfoVo adminReaderInfoVo){
        try {
            ReaderInfo readerInfo=new ReaderInfo();
            ReaderCard readerCard=new ReaderCard();
            BeanUtils.copyProperties(adminReaderInfoVo,readerInfo);
            BeanUtils.copyProperties(adminReaderInfoVo,readerCard);
            readerInfoService.updateById(readerInfo);
            readerCardService.updateById(readerCard);
            return ResultModel.success("编辑读者信息成功！");
        } catch (BeansException e) {
            e.printStackTrace();
            return ResultModel.error("编辑失败！，请稍后再试！");
        }

    }

    @PutMapping("/changeReaderStatus/{readerId}")
    @ApiOperation("更改读者账号状态")
    public ResultModel changeReaderStatus(@PathVariable Integer readerId){
        try {
            ReaderInfo readerInfo = readerInfoService.getById(readerId);
            readerInfo.setStatus(readerInfo.getStatus()==0 ? 1:0);
            readerInfoService.updateById(readerInfo);
            return ResultModel.success(readerInfo.getStatus()==0?"已解禁用户":"已禁用用户");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("操作失败，请稍后再试！");
        }
    }
}

