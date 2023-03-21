package com.ljc.librarybackend.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljc.librarybackend.pojo.entity.BookInfo;
import com.ljc.librarybackend.pojo.entity.LendList;
import com.ljc.librarybackend.pojo.entity.ReaderCard;
import com.ljc.librarybackend.service.BookInfoService;
import com.ljc.librarybackend.service.LendListService;
import com.ljc.librarybackend.service.ReaderCardService;
import com.ljc.librarybackend.utils.LendInfoQuery;
import com.ljc.librarybackend.utils.MailUtils;
import com.ljc.librarybackend.utils.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ljc
 * @since 2023-03-05
 */
@RestController
@RequestMapping("/admin/lendList")
@Slf4j
@Api("借还管理")
public class AdminLendListController {

    @Autowired
    private LendListService lendListService;

    @Autowired
    private BookInfoService bookInfoService;

    @Autowired
    private ReaderCardService readerCardService;

    @PostMapping("/pageQuery")
    @ApiOperation("查询借还记录")
    public ResultModel pageQuery(@RequestBody LendInfoQuery lendInfoQuery){

        try {
            IPage<LendList> lendListIPage= lendListService.pageQueryInfo(lendInfoQuery);
            return ResultModel.success("查找成功！",lendListIPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！请稍后重试");
        }
    }

    @DeleteMapping("/deleteById/{serNum}")
    @ApiOperation("删除借书记录")
    public ResultModel deleteById(@PathVariable Integer serNum){
        try {
            lendListService.removeById(serNum);
            return ResultModel.success("删除记录成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！请稍后再试！");
        }
    }

    @PutMapping("/returnBook/{serNum}")
    @ApiOperation("归还图书接口")
    public ResultModel returnBook(@PathVariable Integer serNum) {
        try {
            //更改借书记录状态
            LendList lendList = lendListService.getById(serNum);
            lendList.setBackDate(LocalDate.now());
            lendList.setStatus(1);
            lendListService.updateById(lendList);
            //更新图书库存
            BookInfo bookInfo = bookInfoService.getById(lendList.getBookId());
            bookInfo.setNumber(bookInfo.getNumber()+1);
            bookInfoService.updateById(bookInfo);

            return ResultModel.success("您已成功归还图书！感谢您的阅读");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！请稍后再试");
        }
    }

    @GetMapping("/tipReturn/{serNum}")
    @ApiOperation("提醒归还图书")
    public ResultModel tipReturn(@PathVariable Integer serNum){
        try {
            //根据流水号找到对应借还记录
            LendList lendList = lendListService.getById(serNum);
            //找到读者
            ReaderCard readerCard = readerCardService.getById(lendList.getReaderId());
            //获得图书
            BookInfo bookInfo = bookInfoService.getById(lendList.getBookId());
            //给读者发邮件
            MailUtils.sendMail(readerCard.getEmail(),
                    readerCard.getUsername()+"读者您好，您于"+lendList.getLendDate()+"借的"+bookInfo.getName()+"已于"+lendList.getExpirationDate()+"逾期，请您尽快归还图书！",
                    "图书逾期提醒");
            return ResultModel.success("提醒成功！已发送邮件给读者");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统繁忙！");
        }


    }

}

