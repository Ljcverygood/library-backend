package com.ljc.librarybackend.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@ApiModel(value="Comment对象", description="")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评论id")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "读者id")
    private Long readerId;

    @ApiModelProperty(value = "书籍id")
    private Integer bookId;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "父级评论id")
    private Long pid;

    @ApiModelProperty(value = "回复的对象")
    private String target;

    @ApiModelProperty(value = "0:未审批	1：已审批")
    private Integer status;

    @ApiModelProperty(value = "0:正常   1：删除")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(exist = false)
    @ApiModelProperty("头像")
    private  String avatar;

    @TableField(exist = false)
    @ApiModelProperty("读者名")
    private String username;

    @TableField(exist = false)
    @ApiModelProperty("图书名")
    private String name;

    @TableField(exist = false)
    @ApiModelProperty("评论次数")
    private Integer commentCount;

    @TableField(exist = false)
    @ApiModelProperty("子评论")
    private List<Comment> children;

}
