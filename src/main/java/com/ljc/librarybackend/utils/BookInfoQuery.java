package com.ljc.librarybackend.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("图书查询对象")
public class BookInfoQuery {
    @ApiModelProperty("当前页")
    private Integer currentPage;
    @ApiModelProperty("每页显示记录数")
    private Integer pageSize;
    @ApiModelProperty("书名")
    private String name;
    @ApiModelProperty("作者")
    private String author;
    @ApiModelProperty("分类号")
    private Integer classId;
}
