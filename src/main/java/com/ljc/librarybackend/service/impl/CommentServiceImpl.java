package com.ljc.librarybackend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljc.librarybackend.pojo.entity.Comment;
import com.ljc.librarybackend.mapper.CommentMapper;
import com.ljc.librarybackend.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljc.librarybackend.utils.ResultModel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ljc
 * @since 2023-03-06
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {


    @Override
    public ResultModel pageQuery(Integer currentPage, Integer pageSize,Integer queryParam) {

        IPage<Comment> commentIPage = baseMapper.pageQuery(new Page<Comment>(currentPage, pageSize),queryParam);
        return ResultModel.success("查询成功！",commentIPage);
    }

    @Override
    public List<Comment> findAllComment(Long bookId) {

        return baseMapper.findAllComment(bookId);
    }

    @Override
    public ResultModel pageQueryForReader(Integer currentPage, Integer pageSize, String username) {
        try {
            IPage<Comment> commentIPage = baseMapper.pageQueryForReader(new Page<Comment>(currentPage, pageSize),username);
            return ResultModel.success("查询成功！",commentIPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙");
        }
    }

    @Override
    public List<Comment> mapQuery(LocalDate firstday) {
        List<Comment> commentList=baseMapper.mapQuery(firstday);
        return commentList;
    }


}
