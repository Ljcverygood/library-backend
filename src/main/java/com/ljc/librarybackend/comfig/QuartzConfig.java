package com.ljc.librarybackend.comfig;

import com.ljc.librarybackend.job.BrJob;
import com.ljc.librarybackend.job.ClearJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 定义任务描述和具体的执行时间
 */
@Configuration
public class QuartzConfig {
    //
    @Bean
    public JobDetail jobDetail() {
        //指定任务描述具体的实现类
        return JobBuilder.newJob(BrJob.class)
                // 指定任务的名称
                .withIdentity("BrJob")
                // 任务描述
                .withDescription("任务描述：用于检测读者还书日期")
                // 每次任务执行后进行存储
                .storeDurably()
                .build();
    }

    @Bean
    public JobDetail clearJobDetail(){
        //指定任务描述具体的实现类
        return JobBuilder.newJob(ClearJob.class)
                .withIdentity("ClearJob")
                .withDescription("任务描述：用于清理oss云存储垃圾图片")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger trigger() {
        //创建触发器
        return TriggerBuilder.newTrigger()
                // 绑定工作任务
                .forJob(jobDetail())
                // 每隔 24 小时执行一次 job
                .withSchedule(SimpleScheduleBuilder.repeatHourlyForever(24))
                .build();
    }

    @Bean
    public Trigger trigger1() {
        //创建触发器
        return TriggerBuilder.newTrigger()
                // 绑定工作任务
                .forJob(clearJobDetail())
                // 每隔 30分钟执行一次 job
                .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(30))
                .build();
    }
}
