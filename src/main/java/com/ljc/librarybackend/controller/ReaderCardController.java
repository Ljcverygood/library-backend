package com.ljc.librarybackend.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ljc.librarybackend.dto.EditPasswordDto;
import com.ljc.librarybackend.dto.FindPasswordDto;
import com.ljc.librarybackend.dto.RegisterDto;
import com.ljc.librarybackend.pojo.entity.ReaderCard;
import com.ljc.librarybackend.pojo.entity.ReaderInfo;
import com.ljc.librarybackend.service.ReaderCardService;
import com.ljc.librarybackend.service.ReaderInfoService;
import com.ljc.librarybackend.utils.JwtUtils;
import com.ljc.librarybackend.utils.MailUtils;
import com.ljc.librarybackend.utils.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ljc
 * @since 2023-02-27
 */
@RestController
@RequestMapping("/readerCard")
@CrossOrigin
@Api("读者信息卡")
@Slf4j
@Transactional
public class ReaderCardController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ReaderCardService readerCardService;

    @Autowired
   private ReaderInfoService readerInfoService;

    @GetMapping("/getRegisterCaptcha")
    @ApiOperation("获取注册验证码")
    public ResultModel getRegisterCaptcha(@RequestParam String email){
        LambdaQueryWrapper<ReaderCard> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ReaderCard::getEmail,email);
        ReaderCard readerCard = readerCardService.getOne(queryWrapper);
        //判断邮箱是否已被注册
        if(readerCard==null){
            String code = String.valueOf((int)((Math.random() * 9 + 1) * 100000));
            log.info("code:"+code);
            redisTemplate.opsForValue().set(email,code,1000 * 60 * 5, TimeUnit.MILLISECONDS);
            MailUtils.sendMail(email,"您的注册验证码为："+code,"注册邮件");
            return ResultModel.success("已成功发送到您的邮箱！5分钟后失效");
        }else {
            return ResultModel.error("邮箱已被注册");
        }
    }
    @GetMapping("/getFindPasswordCaptcha")
    @ApiOperation("获取登录验证码")
    public ResultModel getFindPasswordCaptcha(@RequestParam String email){
        LambdaQueryWrapper<ReaderCard> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ReaderCard::getEmail,email);
        ReaderCard readerCard = readerCardService.getOne(queryWrapper);
        //判断邮箱是否已被注册
        if(readerCard!=null){
            String code = String.valueOf((int)((Math.random() * 9 + 1) * 100000));
            log.info("code:"+code);
            redisTemplate.opsForValue().set(email,code,1000 * 60 * 5, TimeUnit.MILLISECONDS);
            MailUtils.sendMail(email,"您的找回验证码为："+code,"找回密码邮件");
            return ResultModel.success("已成功发送到您的邮箱！5分钟后失效");
        }else {
            return ResultModel.error("您输入的邮箱没有注册！");
        }
    }



    @PostMapping("/register")
    @ApiOperation("注册功能")
    @Transactional
    public ResultModel register(@RequestBody RegisterDto registerDto){

        LambdaQueryWrapper<ReaderCard> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ReaderCard::getUsername,registerDto.getUsername());
        ReaderCard readerCard = readerCardService.getOne(queryWrapper);
        try {
            //先判断用户名是否已存在
            if(readerCard==null){
                //从redis中取的验证码
                String captcha = (String) redisTemplate.opsForValue().get(registerDto.getEmail());
                log.info(registerDto.getCaptcha());
                if(registerDto.getCaptcha().equals(captcha)){
                    ReaderCard readerCard1=new ReaderCard();
                    ReaderInfo readerInfo=new ReaderInfo();
                    readerInfo.setUsername(registerDto.getUsername());
                    BeanUtils.copyProperties(registerDto,readerCard1);
                    readerCard1.setIsDeleted(0);
                    //插入读者卡表
                    readerCardService.save(readerCard1);
                    //插入读者信息表
                    readerInfoService.save(readerInfo);
                    return ResultModel.success("注册成功！请返回登录");
                }else {
                    return ResultModel.error("对不起，您输入的验证码不正确或已过期");
                }
            }else {
                return ResultModel.error("对不起，您输入的用户名已存在！");
            }
        } catch (Exception e) {
            return ResultModel.error("系统出错，请稍后尝试！");
        }
    }

    @PostMapping("/login")
    @ApiOperation("读者登录")
    public ResultModel login(@RequestBody ReaderCard readerCard){
        try {
            LambdaQueryWrapper<ReaderCard> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(ReaderCard::getUsername,readerCard.getUsername());
            queryWrapper.eq(ReaderCard::getPassword,readerCard.getPassword());
            ReaderCard readerCard1 = readerCardService.getOne(queryWrapper);
            ReaderInfo readerInfo = readerInfoService.getById(readerCard1.getReaderId());
            if(readerInfo.getStatus()==1){
                return ResultModel.error("用户已被禁用！请联系管理员！");
            }
            if(readerCard1!=null){
                String token = JwtUtils.createToken(readerCard1.getReaderId(), readerCard1.getUsername());
                log.info("token:"+token);
                Map resultMap = new HashMap();
                resultMap.put("id", readerCard1.getReaderId());
                resultMap.put("token", token);
                return ResultModel.success(resultMap);
            }else {
                return ResultModel.error("用户名密码错误");
            }
        } catch (Exception e) {
            return ResultModel.error("系统繁忙！");
        }
    }

    @GetMapping("/getReader")
    @ApiOperation("获取登录人员信息")
    public ResultModel getReader(HttpServletRequest request){
        try {
            Integer readerId = (Integer) request.getSession().getAttribute("id");
            ReaderInfo readerInfo = readerInfoService.getOne(new LambdaQueryWrapper<ReaderInfo>().eq(ReaderInfo::getReaderId, readerId));
            return ResultModel.success("成功",readerInfo);
        } catch (Exception e) {
            return ResultModel.error("获取登录信息失败！");
        }
    }

    @PutMapping("/editPassword")
    @ApiOperation("修改密码")
    public ResultModel editPassword(@RequestBody EditPasswordDto editPasswordDto,HttpServletRequest request){
        try {
            Integer readerId = (Integer) request.getSession().getAttribute("id");
            ReaderCard readerCard = readerCardService.getById(readerId);
            if(readerCard.getPassword().equals(editPasswordDto.getOldPassword())){
                readerCard.setPassword(editPasswordDto.getNewPassword());
                readerCardService.updateById(readerCard);
                return ResultModel.success("修改成功！请重新登录！");
            }else {
                return ResultModel.success("对不起，您输入的原密码不正确！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙");
        }
    }

    @PostMapping("/findPassword")
    @ApiOperation("找回密码")
    public ResultModel findPassword(@RequestBody FindPasswordDto findPasswordDto){
        try {
            //从redis中取的验证码
            String captcha = (String) redisTemplate.opsForValue().get(findPasswordDto.getEmail());
            if(findPasswordDto.getCaptcha().equals(captcha)){
                LambdaQueryWrapper<ReaderCard> queryWrapper=new LambdaQueryWrapper<>();
                queryWrapper.eq(ReaderCard::getEmail,findPasswordDto.getEmail());
                ReaderCard readerCard = readerCardService.getOne(queryWrapper);
                readerCard.setPassword(findPasswordDto.getPassword());
                readerCardService.updateById(readerCard);
                return ResultModel.success("您的密码找回成功！");
            }else {
                return ResultModel.error("您输入的验证码不正确或已过期");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！");
        }

    }



}

