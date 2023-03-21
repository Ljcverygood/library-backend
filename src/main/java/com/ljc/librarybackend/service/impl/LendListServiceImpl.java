package com.ljc.librarybackend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljc.librarybackend.pojo.entity.Comment;
import com.ljc.librarybackend.pojo.entity.LendList;
import com.ljc.librarybackend.mapper.LendListMapper;
import com.ljc.librarybackend.service.LendListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljc.librarybackend.utils.LendInfoQuery;
import com.ljc.librarybackend.utils.ReaderLendInfoQuery;
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
 * @since 2023-03-13
 */
@Service
public class LendListServiceImpl extends ServiceImpl<LendListMapper, LendList> implements LendListService {

    //读者借还列表
    @Override
    public IPage<LendList> pqgeQuery(ReaderLendInfoQuery readerLendInfoQuery) {
        IPage<LendList> lendListIPage=baseMapper.pageQuery(new Page<>(readerLendInfoQuery.getCurrentPage(),readerLendInfoQuery.getPageSize()),readerLendInfoQuery.getReaderId(),readerLendInfoQuery.getStatus());
        return lendListIPage;
    }
    //管理员借还列表
    @Override
    public IPage<LendList> pageQueryInfo(LendInfoQuery lendInfoQuery) {
        IPage<LendList> lendListIPage=baseMapper.pageQueryInfo(new Page<LendList>(lendInfoQuery.getCurrentPage(),lendInfoQuery.getPageSize()),lendInfoQuery.getLookType(),lendInfoQuery.getReaderId(),lendInfoQuery.getBookId());
        return lendListIPage;
    }

    @Override
    public List<LendList> mapQuery(LocalDate firstday) {
        List<LendList> lendLists=baseMapper.mapQuery(firstday);
        return lendLists;
    }
}
