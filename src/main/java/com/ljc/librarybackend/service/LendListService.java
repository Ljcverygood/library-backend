package com.ljc.librarybackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ljc.librarybackend.pojo.entity.LendList;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ljc.librarybackend.utils.LendInfoQuery;
import com.ljc.librarybackend.utils.ReaderLendInfoQuery;
import com.ljc.librarybackend.utils.ResultModel;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ljc
 * @since 2023-03-13
 */
public interface LendListService extends IService<LendList> {

    IPage<LendList> pqgeQuery(ReaderLendInfoQuery readerLendInfoQuery);

    IPage<LendList> pageQueryInfo(LendInfoQuery lendInfoQuery);

    List<LendList> mapQuery(LocalDate firstday);
}
