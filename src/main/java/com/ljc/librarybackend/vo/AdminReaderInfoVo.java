package com.ljc.librarybackend.vo;


import com.ljc.librarybackend.pojo.entity.ReaderInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("读者详情模型")
public class AdminReaderInfoVo extends ReaderInfo {
    @ApiModelProperty(value = "读者密码")
    private String password;

    @ApiModelProperty(value = "邮箱")
    private String email;
}
