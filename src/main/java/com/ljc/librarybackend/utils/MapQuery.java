package com.ljc.librarybackend.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
@ApiModel("最热图书查询对象")
public class MapQuery {
    @ApiModelProperty("本月开始第一天")
    private LocalDate firstDay;
    @ApiModelProperty("每页显示记录数")
    private Integer pageSize;
    @ApiModelProperty("书名")
    private String name;
    @ApiModelProperty("作者")
    private String author;
    @ApiModelProperty("分类号")
    private Integer classId;
}
