package com.ljc.librarybackend.utils;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("读者查询实体")
public class ReaderInfoQuery {
    @ApiModelProperty("当前页")
    private Integer currentPage;
    @ApiModelProperty("每页显示记录数")
    private Integer pageSize;
    @ApiModelProperty("读者号")
    private String readerId;
    @ApiModelProperty("姓名")
    private String username;

}
