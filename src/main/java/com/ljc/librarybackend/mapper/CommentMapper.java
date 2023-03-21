package com.ljc.librarybackend.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljc.librarybackend.pojo.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ljc
 * @since 2023-03-13
 */
public interface CommentMapper extends BaseMapper<Comment> {

    IPage<Comment> pageQuery(Page<Comment> commentPage, Integer queryParam);


    List<Comment> findAllComment(Long bookId);

    IPage<Comment> pageQueryForReader(Page<Comment> commentPage, String username);

    List<Comment> mapQuery(LocalDate firstday);
}
