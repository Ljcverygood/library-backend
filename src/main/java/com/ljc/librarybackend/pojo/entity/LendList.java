package com.ljc.librarybackend.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

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
@ApiModel(value="LendList对象", description="")
public class LendList implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "流水号")
      @TableId(value = "ser_num", type = IdType.AUTO)
    private Long serNum;

    @ApiModelProperty(value = "图书id")
    private Long bookId;

    @ApiModelProperty(value = "读者id")
    private Long readerId;

    @ApiModelProperty(value = "借取时间")
    private LocalDate lendDate;

    @ApiModelProperty(value = "返还时间")
    private LocalDate backDate;

    @ApiModelProperty(value = "0:正常	1：禁用")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty(value = "0：待还  1：已还  2：逾期 3：续借")
    private Integer status;

    @ApiModelProperty(value = "到期日期")
    private LocalDate expirationDate;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(exist = false)
    @ApiModelProperty("图书名")
    private String name;

    @TableField(exist = false)
    @ApiModelProperty("借阅次数")
    private Integer lendCount;

}
