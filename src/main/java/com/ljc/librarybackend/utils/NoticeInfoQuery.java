package com.ljc.librarybackend.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("公告管理查询")
public class NoticeInfoQuery {
    @ApiModelProperty("当前页")
    private Integer currentPage;
    @ApiModelProperty("每页显示记录数")
    private Integer pageSize;
    @ApiModelProperty("公告主题")
    private String topic;

}
