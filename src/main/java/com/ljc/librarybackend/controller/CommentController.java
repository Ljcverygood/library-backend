package com.ljc.librarybackend.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ljc.librarybackend.pojo.entity.Comment;
import com.ljc.librarybackend.service.CommentService;
import com.ljc.librarybackend.service.ReaderInfoService;
import com.ljc.librarybackend.utils.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ljc
 * @since 2023-03-06
 */
@RestController
@RequestMapping("/comment")
@Slf4j
@Api("评论")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private ReaderInfoService readerInfoService;

    @PostMapping("/getComment/{currentPage}/{pageSize}")
    @ApiOperation("获取评论列表")
    public ResultModel pageQuery(@PathVariable Integer currentPage,@PathVariable Integer pageSize,HttpServletRequest request){
        try {
            //获取当前登录读者id
            Integer readerId = (Integer) request.getSession().getAttribute("id");
            String username = readerInfoService.getById(readerId).getUsername();
            return commentService.pageQueryForReader(currentPage,pageSize,username);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！请稍后重试");
        }
    }

    @PostMapping("/getComment/{bookId}")
    @ApiOperation("获取评论列表图书号")
    public ResultModel getComment(@PathVariable Long bookId){

        try {
            List<Comment> commentList = commentService.findAllComment(bookId);
            List<Comment> rootComments = commentList.stream().filter(comment -> comment.getPid() == null).collect(Collectors.toList());
            for (Comment rootComment : rootComments) {
                rootComment.setChildren(commentList.stream().filter(comment -> rootComment.getId().equals(comment.getPid())).collect(Collectors.toList()));
            }
            return ResultModel.success("查询成功！",rootComments);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("查询失败！");
        }
    }

    @PostMapping("/getSubComment/{id}")
    @ApiOperation("根据评论id获取子评论")
    public ResultModel getSubComment(@PathVariable Long id){
        try {
            LambdaQueryWrapper<Comment> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(Comment::getPid,id);
            List<Comment> subCommentList = commentService.list(queryWrapper);
            return ResultModel.success("查询成功！",subCommentList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！");
        }

    }

    @PostMapping("/submitComment/{bookId}")
    @ApiOperation("发表评论接口")
    public ResultModel submitComment(@PathVariable Integer bookId, @RequestBody String comment, HttpServletRequest request){
        try {
            Integer readerId = (Integer) request.getSession().getAttribute("id");
            Comment comment1=new Comment();
            comment1.setReaderId(Long.valueOf(readerId));
            comment1.setBookId(bookId);
            comment1.setContent(comment);
            commentService.save(comment1);
            return ResultModel.success("发表评论成功！待管理员审核！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！");
        }
    }

    @PostMapping("/saveReply")
    @ApiOperation("保存回复评论")
    public ResultModel saveReply(@RequestBody Comment comment,HttpServletRequest request){
        try {
            Integer readerId = (Integer) request.getSession().getAttribute("id");
            comment.setReaderId(Long.valueOf(readerId));
            commentService.save(comment);
            return ResultModel.success("回复成功！待管理员审核！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！");
        }
    }


}

