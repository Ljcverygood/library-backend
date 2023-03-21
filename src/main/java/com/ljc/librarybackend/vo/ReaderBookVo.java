package com.ljc.librarybackend.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ljc.librarybackend.pojo.entity.BookInfo;
import com.ljc.librarybackend.pojo.entity.LendList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("读者图书页面")
public class ReaderBookVo extends BookInfo {

    @TableField(exist = false)
    @ApiModelProperty("借还列表")
    private List<LendList> lendLists;
}
