package com.ljc.librarybackend.service.impl;

import com.ljc.librarybackend.pojo.entity.BookInfo;
import com.ljc.librarybackend.mapper.BookInfoMapper;
import com.ljc.librarybackend.service.BookInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ljc
 * @since 2023-03-13
 */
@Service
public class BookInfoServiceImpl extends ServiceImpl<BookInfoMapper, BookInfo> implements BookInfoService {

}
