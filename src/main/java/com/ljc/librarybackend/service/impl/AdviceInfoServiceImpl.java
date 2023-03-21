package com.ljc.librarybackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljc.librarybackend.mapper.AdviceInfoMapper;
import com.ljc.librarybackend.mapper.BookInfoMapper;
import com.ljc.librarybackend.pojo.entity.AdviceInfo;
import com.ljc.librarybackend.pojo.entity.BookInfo;
import com.ljc.librarybackend.service.AdviceInfoService;
import com.ljc.librarybackend.service.BookInfoService;
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
public class AdviceInfoServiceImpl extends ServiceImpl<AdviceInfoMapper, AdviceInfo> implements AdviceInfoService {

}
