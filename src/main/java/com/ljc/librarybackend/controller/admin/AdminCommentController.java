package com.ljc.librarybackend.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ljc.librarybackend.pojo.entity.Comment;
import com.ljc.librarybackend.service.CommentService;
import com.ljc.librarybackend.utils.ResultModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ljc
 * @since 2023-03-05
 */
@RestController
@RequestMapping("/admin/comment")
public class AdminCommentController {

    @Autowired
    private CommentService commentService;


    @PostMapping("/getComment/{currentPage}/{pageSize}/{queryParam}")
    @ApiOperation("获取评论列表")
    public ResultModel pageQuery(@PathVariable Integer currentPage,@PathVariable Integer pageSize,@PathVariable Integer queryParam){
        try {
            return commentService.pageQuery(currentPage,pageSize,queryParam);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！请稍后重试");
        }
    }

    @GetMapping("/changeStatus/{id}")
    @ApiOperation("改变评论状态")
    public ResultModel changeStatus(@PathVariable Integer id){
        try {
            Comment comment = commentService.getById(id);

            LambdaQueryWrapper<Comment> queryWrapper=new LambdaQueryWrapper<>();
            //更改子评论状态
            queryWrapper.eq(Comment::getPid,id);
            List<Comment> list = commentService.list(queryWrapper);
            if(list.size()>0){
                for (Comment comment1 : list) {
                    if(comment.getStatus()==0){
                        comment1.setStatus(1);
                    }else {
                        comment1.setStatus(0);
                    }
                }
                commentService.updateBatchById(list);
            }


            comment.setStatus(comment.getStatus()==1?0:1);
            commentService.updateById(comment);
            return ResultModel.success("更改评论状态成功！！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！请稍后重试");
        }
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("根据id删除评论")
    public ResultModel delete(@PathVariable Integer id){
        try {
            //根据id删除评论
            commentService.removeById(id);
            LambdaQueryWrapper<Comment> queryWrapper=new LambdaQueryWrapper<>();
            //删除子评论
            queryWrapper.eq(Comment::getPid,id);
            commentService.remove(queryWrapper);
            return ResultModel.success("删除评论成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return  ResultModel.error("系统繁忙！请稍后重试");
        }

    }

}

