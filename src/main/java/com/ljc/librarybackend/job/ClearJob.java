package com.ljc.librarybackend.job;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.ljc.librarybackend.utils.OssProperties;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Set;
@Slf4j
public class ClearJob extends QuartzJobBean {

    @Autowired
    private RedisTemplate redisTemplate;

    //定时清理oss云存储的垃圾图片
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //将数据库中存储的头像与oss云中的头像进行比较，得出差值，就是存储云上的垃圾头像
        Set set = redisTemplate.opsForSet().difference("imageForRedis", "imageForDB");


        if(set != null){
            OSS ossClient = new OSSClientBuilder().build(
                    OssProperties.ENDPOINT,
                    OssProperties.KEY_ID,
                    OssProperties.KEY_SECRET);
            for (Object fileName : set) {
                String fileNameForString = fileName.toString();
                //获取oss云存储文件名
                String fileName1=fileNameForString.substring(fileNameForString.lastIndexOf("/"));
                log.info(fileName1);
                //删除云端垃圾
                ossClient.deleteObject(OssProperties.BUCKET_NAME,"avatar"+fileName1);
                //删除redis的垃圾头像
                redisTemplate.opsForSet().remove("imageForRedis",fileName);

            }
        }

    }
}
