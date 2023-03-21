package com.ljc.librarybackend.service.impl;

import com.ljc.librarybackend.pojo.entity.Admin;
import com.ljc.librarybackend.mapper.AdminMapper;
import com.ljc.librarybackend.service.AdminService;
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
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

}
