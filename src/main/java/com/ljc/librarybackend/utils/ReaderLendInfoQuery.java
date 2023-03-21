package com.ljc.librarybackend.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("读者借还查询模型")
public class ReaderLendInfoQuery {
    @ApiModelProperty("当前页")
    private Integer currentPage;
    @ApiModelProperty("每页显示记录数")
    private Integer pageSize;
    @ApiModelProperty("状态")
    private Integer status;
    @ApiModelProperty("用户id")
    private Integer readerId;
}
