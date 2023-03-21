package com.ljc.librarybackend.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author ljc
 * @since 2023-03-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ReaderCard对象", description="")
public class ReaderCard implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "读者id")
      @TableId(value = "reader_id", type = IdType.AUTO)
    private Long readerId;

    @ApiModelProperty(value = "读者用户名")
    private String username;

    @ApiModelProperty(value = "读者密码")
    private String password;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "0:正常  1：禁用")
    @TableLogic
    private Integer isDeleted;


}
