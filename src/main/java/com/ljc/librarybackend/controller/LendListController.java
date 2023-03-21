package com.ljc.librarybackend.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ljc.librarybackend.pojo.entity.LendList;
import com.ljc.librarybackend.service.LendListService;
import com.ljc.librarybackend.utils.ReaderLendInfoQuery;
import com.ljc.librarybackend.utils.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ljc
 * @since 2023-03-06
 */
@RestController
@RequestMapping("/reader/lendList")
@Api("借还")
public class LendListController {


    @Autowired
    private LendListService lendListService;

    @PostMapping("/getLendList")
    @ApiOperation("获取当前用户的借书情况")
    public ResultModel getLendList(HttpServletRequest request) {
        try {
            Integer id = (Integer) request.getSession().getAttribute("id");
            LambdaQueryWrapper<LendList> lendListLambdaQueryWrapper = new LambdaQueryWrapper<>();
            lendListLambdaQueryWrapper.eq(LendList::getReaderId, id);
            List<LendList> lendList = lendListService.list(lendListLambdaQueryWrapper);
            List<LendList> lendLists = lendList.stream().filter(lendList1 -> lendList1.getBackDate() == null).collect(Collectors.toList());
            ArrayList<Long> lendBooks = new ArrayList<>();
            for (LendList list : lendLists) {
                lendBooks.add(list.getBookId());
            }
            return ResultModel.success("查询成功！", lendBooks);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！");
        }
    }

    @PostMapping("/isBr/{bookId}")
    @ApiOperation("查看本书是否已借")
    public ResultModel isBr(@PathVariable Integer bookId, HttpServletRequest request) {
        try {
            Integer id = (Integer) request.getSession().getAttribute("id");
            LambdaQueryWrapper<LendList> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(LendList::getReaderId, id);
            queryWrapper.eq(LendList::getBookId, bookId);
            List<LendList> list = lendListService.list(queryWrapper);
            if (list.size() > 0) {
                return ResultModel.success("已借", 1);
            } else {
                return ResultModel.success("没借过", 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！");
        }
    }

    @PostMapping("/getLendInfo")
    @ApiOperation("获取读者借还列表")
    public ResultModel getLendInfo(@RequestBody ReaderLendInfoQuery readerLendInfoQuery, HttpServletRequest request) {
        Integer id = (Integer) request.getSession().getAttribute("id");
        readerLendInfoQuery.setReaderId(id);
        IPage<LendList> lendListIPage = lendListService.pqgeQuery(readerLendInfoQuery);
        return ResultModel.success("查询成功！",lendListIPage);
    }

    @PutMapping("/renew/{serNum}")
    @ApiOperation("续借接口")
    public ResultModel renew(@PathVariable Integer serNum){
        try {
            LendList lendList = lendListService.getById(serNum);
            //更改该借阅图书的状态
            lendList.setStatus(3);
            //将还书日期增加一个月
            lendList.setExpirationDate(lendList.getExpirationDate().plusMonths(1));
            lendListService.updateById(lendList);
            return ResultModel.success("续借成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！");
        }
    }
}

