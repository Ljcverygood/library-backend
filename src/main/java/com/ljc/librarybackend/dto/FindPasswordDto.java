package com.ljc.librarybackend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("找回密码模型")
public class FindPasswordDto {
    @ApiModelProperty("验证码")
    private String captcha;
    @ApiModelProperty("新密码")
    private String password;
    @ApiModelProperty("邮箱")
    private String email;
}
