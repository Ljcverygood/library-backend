package com.ljc.librarybackend.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljc.librarybackend.pojo.entity.LendList;
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
public interface LendListMapper extends BaseMapper<LendList> {

    IPage<LendList> pageQuery(Page<Object> objectPage, Integer readerId, Integer status);

    IPage<LendList> pageQueryInfo(Page<LendList> lendListPage, Integer lookType, Integer readerId, Integer bookId);

    List<LendList> mapQuery(LocalDate firstday);
}
