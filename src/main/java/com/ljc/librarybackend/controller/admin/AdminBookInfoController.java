package com.ljc.librarybackend.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljc.librarybackend.pojo.entity.BookInfo;
import com.ljc.librarybackend.service.BookInfoService;
import com.ljc.librarybackend.utils.BookInfoQuery;
import com.ljc.librarybackend.utils.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ljc
 * @since 2023-02-25
 */
@RestController
@RequestMapping("/admin/bookInfo")
@Api("图书管理")
@CrossOrigin
@Slf4j
public class AdminBookInfoController {

    @Autowired
    private BookInfoService bookInfoService;

    @PostMapping("/pageQuery")
    @ApiOperation("查询图书")
    public ResultModel pageQuery(@RequestBody BookInfoQuery bookInfoQuery){
        try {
            LambdaQueryWrapper<BookInfo> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.like(bookInfoQuery.getName()!=null,BookInfo::getName,bookInfoQuery.getName());
            queryWrapper.like(bookInfoQuery.getAuthor()!=null,BookInfo::getAuthor,bookInfoQuery.getAuthor());
            queryWrapper.eq(bookInfoQuery.getClassId()!=null,BookInfo::getClassId,bookInfoQuery.getClassId());
            Page<BookInfo> pageParam=new Page<>(bookInfoQuery.getCurrentPage(),bookInfoQuery.getPageSize());
            Page<BookInfo> bookInfoPage = bookInfoService.page(pageParam, queryWrapper);
            return ResultModel.success("查询成功！",bookInfoPage);
        } catch (Exception e) {
            return ResultModel.error("查询失败！");
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询图书信息")
    public ResultModel findBookById(@PathVariable Integer id){
        BookInfo bookInfo = bookInfoService.getById(id);
        return ResultModel.success(bookInfo);
    }

    @PostMapping("/addBookInfo")
    @ApiOperation("新增图书信息接口")
    public ResultModel addBookInfo(@RequestBody BookInfo bookInfo){
        try {
            log.info(bookInfo.toString());
            bookInfoService.save(bookInfo);
            return ResultModel.success("新增成功！");
        } catch (Exception e) {
            return ResultModel.error("新增图书失败！请稍后重试");
        }
    }

    @PutMapping("/updateBookInfo")
    @ApiOperation("修改图书信息")
    public ResultModel updateBookInfo(@RequestBody BookInfo bookInfo){
        try {
            log.info(bookInfo.toString());
            bookInfoService.updateById(bookInfo);
            return ResultModel.success("修改图书信息成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("修改图书信息失败！请稍后再试！");
        }
    }

    @PutMapping("/changeBookStatus/{bookId}")
    @ApiOperation("更改图书上下架状态")
    public ResultModel changeBookStatus(@PathVariable Integer bookId){
        try {
            BookInfo bookInfo = bookInfoService.getById(bookId);
            bookInfo.setStatus(bookInfo.getStatus()==1?0:1);
            bookInfoService.updateById(bookInfo);
            return ResultModel.success(bookInfo.getStatus()==0?"上架成功":"下架成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！请稍后再试！");
        }
    }

}

