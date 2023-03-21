package com.ljc.librarybackend.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("借还查询模型")
public class LendInfoQuery {
    @ApiModelProperty("当前页")
    private Integer currentPage;
    @ApiModelProperty("每页显示记录数")
    private Integer pageSize;
    @ApiModelProperty("读者号")
    private Integer readerId;
    @ApiModelProperty("图书号")
    private Integer bookId;
    @ApiModelProperty("借还状态")
    private Integer lookType;
}
