package com.ljc.librarybackend.service;

import com.ljc.librarybackend.pojo.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ljc.librarybackend.utils.ResultModel;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ljc
 * @since 2023-03-06
 */
public interface CommentService extends IService<Comment> {

    ResultModel pageQuery(Integer currentPage, Integer pageSize,Integer queryParam);

    List<Comment> findAllComment(Long bookId);


    ResultModel pageQueryForReader(Integer currentPage, Integer pageSize, String username);

    List<Comment> mapQuery(LocalDate firstday);
}
