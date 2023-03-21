package com.ljc.librarybackend.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljc.librarybackend.pojo.entity.*;
import com.ljc.librarybackend.service.*;
import com.ljc.librarybackend.utils.BookInfoQuery;
import com.ljc.librarybackend.utils.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
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
@RequestMapping("/reader/bookInfo")
@Api("读者图书界面")
public class BookInfoController {


    @Autowired
    private BookInfoService bookInfoService;

    @Autowired
    private LendListService lendListService;

    @Autowired
    private ClassInfoService classInfoService;

    @Autowired
    private CommentService commentService;


    @PostMapping("/pageQuery")
    @ApiOperation("分页查询")
    public ResultModel pageQuery(@RequestBody BookInfoQuery bookInfoQuery, HttpServletRequest request) {
        try {
            LambdaQueryWrapper<BookInfo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(bookInfoQuery.getName() != null, BookInfo::getName, bookInfoQuery.getName());
            queryWrapper.like(bookInfoQuery.getAuthor() != null, BookInfo::getAuthor, bookInfoQuery.getAuthor());
            queryWrapper.eq(bookInfoQuery.getClassId() != null, BookInfo::getClassId, bookInfoQuery.getClassId());
            Page<BookInfo> pageParam = new Page<>(bookInfoQuery.getCurrentPage(), bookInfoQuery.getPageSize());
            Page<BookInfo> bookInfoPage = bookInfoService.page(pageParam, queryWrapper);
            return ResultModel.success("查询成功！", bookInfoPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("查询失败！");
        }

    }

    @PostMapping("/borrow/{bookId}")
    @ApiOperation("借阅图书接口")
    public ResultModel borrow(@PathVariable Integer bookId, HttpServletRequest request) {
        try {
            Integer id = (Integer) request.getSession().getAttribute("id");
            //首先查询是否有逾期图书
            LambdaQueryWrapper<LendList> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(LendList::getStatus, 2);
            queryWrapper.eq(LendList::getReaderId, id);
            List<LendList> lendLists = lendListService.list(queryWrapper);
            if (lendLists.size() > 0) {
                return ResultModel.error("对不起，您有逾期图书未归还，请尽快归还图书！已停止借阅图书功能！");
            }
            //查看本月是否已达最大借书限制5本
            LambdaQueryWrapper<LendList> queryWrapper1 = new LambdaQueryWrapper<>();
            LocalDate date = LocalDate.now();
            LocalDate firstDay = date.with(TemporalAdjusters.firstDayOfMonth());
            queryWrapper1.ge(LendList::getLendDate, firstDay);
            queryWrapper1.ge(LendList::getReaderId, id);
            List<LendList> lendLists1 = lendListService.list(queryWrapper1);
            if (lendLists1.size() >= 5) {
                return ResultModel.error("对不起，您本月已达最大借书数量！");
            }

            LendList lendList = new LendList();
            lendList.setBookId(Long.valueOf(bookId));
            lendList.setReaderId(Long.valueOf(id));
            lendList.setLendDate(date);
            lendList.setExpirationDate(date.plusMonths(1));
            lendListService.save(lendList);

            //图书库存-1
            BookInfo bookInfo = bookInfoService.getById(bookId);
            bookInfo.setNumber(bookInfo.getNumber()-1);
            bookInfoService.updateById(bookInfo);
            return ResultModel.success("您已成功借阅此书，请到达图书馆取走图书，归还期限为30日内，请您注意时间，祝您阅读愉快！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！请稍后再试");
        }
    }



    @GetMapping("/getBookByid/{bookId}")
    @ApiOperation("根据图书id查询图书")
    public ResultModel getBookByid(@PathVariable Long bookId){
        try {
            BookInfo bookInfo = bookInfoService.getById(bookId);
            return ResultModel.success("查询成功！",bookInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("查询图书失败！");
        }
    }

    @GetMapping("/getBookByName/{name}")
    @ApiOperation("根据图书名查询图书")
    public ResultModel getBookByName(@PathVariable String name){
        try {
            LambdaQueryWrapper<BookInfo> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(BookInfo::getName,name);
            BookInfo bookInfo = bookInfoService.getOne(queryWrapper);
            return ResultModel.success("查询成功！",bookInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("查询图书失败！");
        }
    }

    @GetMapping("/getClassName/{bookId}")
    @ApiOperation("根据图书id获取分类名")
    public ResultModel getClassName(@PathVariable Long bookId) {
        try {
            BookInfo bookInfo = bookInfoService.getById(bookId);
            ClassInfo classInfo = classInfoService.getById(bookInfo.getClassId());
            return ResultModel.success("获取分类名成功！",classInfo.getClassName());
        } catch (Exception e) {
            return ResultModel.error("获取分类信息失败！");
        }
    }

    @GetMapping("/getMapData/{type}")
    @ApiOperation("获取热点图书信息")
    public ResultModel getMapData(@PathVariable String type){
        LocalDate localDate = LocalDate.now();
        //本月第一天
        LocalDate firstday = LocalDate.of(localDate.getYear(), localDate.getMonthValue(), 1);
        try {
            //类型1，按照借阅次数最多查找
            if(type.equals("1")){
                List<LendList> lendLists=lendListService.mapQuery(firstday);
                List name=new ArrayList<>();
                List data=new ArrayList<>();

                for (LendList lendList : lendLists) {
                    name.add(lendList.getName());
                    data.add(lendList.getLendCount());
                }
                HashMap map=new HashMap<>();
                map.put("name",JSON.toJSON(name));
                map.put("data", JSON.toJSON(data));

                return ResultModel.success("查询成功！",map);
            }else {
                //按照最多评论数查找
                List<Comment> commentList=commentService.mapQuery(firstday);

                List name=new ArrayList<>();
                List data=new ArrayList<>();

                for (Comment comment : commentList) {
                    name.add(comment.getName());
                    data.add(comment.getCommentCount());
                }
                HashMap map=new HashMap<>();
                map.put("name",JSON.toJSON(name));
                map.put("data", JSON.toJSON(data));

                return ResultModel.success("查询成功！",map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！");
        }

    }
}

