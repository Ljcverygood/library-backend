package com.ljc.librarybackend.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ljc.librarybackend.pojo.entity.Admin;
import com.ljc.librarybackend.service.AdminService;
import com.ljc.librarybackend.utils.JwtUtils;
import com.ljc.librarybackend.utils.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ljc
 * @since 2023-02-25
 */
@RestController
@RequestMapping("/admin")
@Api("管理员页面")
@CrossOrigin
@Slf4j
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/getAdmin")
    @ApiOperation("获取当前登录用户")
    public ResultModel getAdmin(HttpServletRequest request){
        try {
            String token = request.getHeader("token");
            String username = JwtUtils.getClaimByName(token,"username");
            return ResultModel.success("成功",username);
        } catch (Exception e) {
            return ResultModel.error("获取登录信息失败！");
        }
    }

    @ApiOperation("登录接口")
    @PostMapping("/login")
    public ResultModel login(@RequestBody Admin admin){
        try {
            if(admin.getUsername() == null){
                return ResultModel.error();
            }
            LambdaQueryWrapper<Admin> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(Admin::getUsername,admin.getUsername());
            queryWrapper.eq(Admin::getPassword,admin.getPassword());
            Admin admin1 = adminService.getOne(queryWrapper);
            if(admin1 != null) {

                String token = JwtUtils.createToken(admin1.getAdminId(), admin1.getUsername());
                log.info("token:"+token);
                Map resultMap = new HashMap();
                resultMap.put("id", admin1.getAdminId());
                resultMap.put("token", token);
                return ResultModel.success(resultMap);
            }else {
                return ResultModel.error("用户名密码错误");
            }
        } catch (Exception e) {
            return ResultModel.error("系统繁忙！");
        }
    }

    @GetMapping("/logout")
    @ApiOperation("退出登录")
    public ResultModel logout(HttpServletRequest request){
        try {
            request.getSession().removeAttribute("id");
            request.getSession().removeAttribute("username");
            return ResultModel.success("退出登录成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("退出登录失败，请稍后重试！");
        }
    }


}

