package com.ljc.librarybackend.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ljc.librarybackend.exception.CustomException;
import com.ljc.librarybackend.pojo.entity.BookInfo;
import com.ljc.librarybackend.pojo.entity.LendList;
import com.ljc.librarybackend.pojo.entity.ReaderCard;
import com.ljc.librarybackend.service.BookInfoService;
import com.ljc.librarybackend.service.LendListService;
import com.ljc.librarybackend.service.ReaderCardService;
import com.ljc.librarybackend.utils.MailUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务
 */
@Slf4j
public class BrJob extends QuartzJobBean {

    @Autowired
    private LendListService lendListService;

    @Autowired
    private ReaderCardService readerCardService;

    @Autowired
    private BookInfoService bookInfoService;
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try{
            LambdaQueryWrapper<LendList> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(LendList::getStatus,0);
            List<LendList> lendLists = lendListService.list(queryWrapper);
            for (LendList lendList : lendLists) {
                //规划期限的倒数第5天
                LocalDate lendDate = lendList.getExpirationDate().minusDays(5);
                //借阅书籍30天
                LocalDate lastDay = lendDate.plusDays(5);
                if(lendDate.isBefore(LocalDateTime.now().toLocalDate()) && lastDay.isAfter(LocalDateTime.now().toLocalDate())){
                    ReaderCard readerCard = readerCardService.getById(lendList.getReaderId());
                    BookInfo bookInfo = bookInfoService.getById(lendList.getBookId());
                    log.info(readerCard.getEmail());
                    MailUtils.sendMail(readerCard.getEmail()
                            ,"同学您好！您于"+lendList.getLendDate()+"借的:"+bookInfo.getName()+"即将到达还书日期，请您注意还书时间。谢谢！","还书提醒");
                    Thread.currentThread().sleep(60000);
                } else if (lastDay.isBefore(LocalDateTime.now().toLocalDate())&&lendList.getStatus()==0) {
                    ReaderCard readerCard = readerCardService.getById(lendList.getReaderId());
                    BookInfo bookInfo = bookInfoService.getById(lendList.getBookId());
                    //将借书状态设为逾期
                    lendList.setStatus(2);
                    lendListService.updateById(lendList);
                    MailUtils.sendMail(readerCard.getEmail().trim()
                            ,"同学您好！您于"+lendList.getLendDate()+"借的:"+bookInfo.getName()+"已经到达还书最后日期，请您尽快还书，以免造成不必要的麻烦！","还书逾期提醒");

                    Thread.currentThread().sleep(60000);
                }

            }
        }catch (Exception e){
            throw new CustomException("任务执行失败！");
        }
    }
}
