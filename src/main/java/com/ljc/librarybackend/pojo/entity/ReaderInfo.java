package com.ljc.librarybackend.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
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
@ApiModel(value="ReaderInfo对象", description="")
public class ReaderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "读者号")
      @TableId(value = "reader_id", type = IdType.AUTO)
    private Long readerId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "性别 0：女   1：男")
    private Integer sex;

    @ApiModelProperty(value = "生日")
    private LocalDate birth;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "0：正常 1：禁用")
    private Integer status;

    @ApiModelProperty(value = "是否首次登录  0:是 1：否")
    private Integer isFirstLogin;


}
