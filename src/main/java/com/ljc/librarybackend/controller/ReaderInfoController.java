package com.ljc.librarybackend.controller;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.ljc.librarybackend.pojo.entity.ReaderCard;
import com.ljc.librarybackend.pojo.entity.ReaderInfo;
import com.ljc.librarybackend.service.ReaderCardService;
import com.ljc.librarybackend.service.ReaderInfoService;
import com.ljc.librarybackend.utils.OssProperties;
import com.ljc.librarybackend.utils.ResultModel;
import com.ljc.librarybackend.vo.ReaderInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.UUID;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ljc
 * @since 2023-03-06
 */
@RestController
@RequestMapping("/readerInfo")
public class ReaderInfoController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ReaderInfoService readerInfoService;

    @Autowired
    private ReaderCardService readerCardService;

    @PostMapping("/upload")
    @ApiOperation("上传头像")
    public ResultModel upload(@RequestParam("imgFile") MultipartFile imgFile){
        try {

            InputStream inputStream = imgFile.getInputStream();
            String originalFilename = imgFile.getOriginalFilename();
            int index = originalFilename.lastIndexOf(".");
            String extention=originalFilename.substring(index-1);//获取文件扩展名.jpg
            String fileName= UUID.randomUUID().toString()+extention;//拼接文件名

            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(
                    OssProperties.ENDPOINT,
                    OssProperties.KEY_ID,
                    OssProperties.KEY_SECRET);
            //判断oss实例是否存在：如果不存在则创建，如果存在则获取
            if(!ossClient.doesBucketExist(OssProperties.BUCKET_NAME)){
                //创建bucket
                ossClient.createBucket(OssProperties.BUCKET_NAME);
                //设置oss实例的访问权限：公共读
                ossClient.setBucketAcl(OssProperties.BUCKET_NAME, CannedAccessControlList.PublicRead);
            }

            //文件根路径
            String key = "avatar/" + fileName;


            ObjectMetadata objectMetadata=new ObjectMetadata();
            objectMetadata.setContentType("image/jpg");
            //文件上传至阿里云
            ossClient.putObject(OssProperties.BUCKET_NAME, key, inputStream,objectMetadata);

            // 关闭OSSClient。
            ossClient.shutdown();

            String imageUrl="https://" + OssProperties.BUCKET_NAME + "." + OssProperties.ENDPOINT + "/" + key;
            //将上传到云端的头像存入redis
            redisTemplate.opsForSet().add("imageForRedis",imageUrl);
            return ResultModel.success("上传成功！",imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！请稍后再试！");
        }
    }

    @PostMapping("/refineInfo")
    @ApiOperation("完善个人信息")
    public ResultModel refineInfo(@RequestBody ReaderInfo readerInfo){
        try {
            readerInfo.setIsFirstLogin(1);//设置为1意思是将首次登录改为否
            readerInfoService.updateById(readerInfo);
            //将存入数据库的头像放到redis的set集合中
            redisTemplate.opsForSet().add("imageForDB",readerInfo.getAvatar());
            return ResultModel.success("保存成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！请稍后再试！");
        }
    }

    @GetMapping("/getUserInfo")
    @ApiOperation("获得用户信息")
    public ResultModel getUserInfo(HttpServletRequest request){
        try {
            Integer id = (Integer) request.getSession().getAttribute("id");
            ReaderInfo readerInfo = readerInfoService.getById(id);
            ReaderCard readerCard = readerCardService.getById(id);
            ReaderInfoVo readerInfoVo=new ReaderInfoVo();
            BeanUtils.copyProperties(readerInfo,readerInfoVo);
            BeanUtils.copyProperties(readerCard,readerInfoVo);

            return ResultModel.success("查询成功！",readerInfoVo);
        } catch (BeansException e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！！");
        }
    }
}

