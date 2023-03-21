package com.ljc.librarybackend.vo;

import com.ljc.librarybackend.pojo.entity.ReaderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReaderInfoVo extends ReaderInfo {
    @ApiModelProperty(value = "邮箱")
    private String email;
}
