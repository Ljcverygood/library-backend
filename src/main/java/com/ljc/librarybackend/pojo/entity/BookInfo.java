package com.ljc.librarybackend.pojo.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
@ApiModel(value="BookInfo对象", description="")
public class BookInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "图书id")
      @TableId(value = "book_id", type = IdType.AUTO)
    private Long bookId;

    @ApiModelProperty(value = "图书名")
    private String name;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "出版社")
    private String publish;

    @ApiModelProperty(value = "ISBN")
    @TableField("ISBN")
    private String isbn;

    @ApiModelProperty(value = "图书简介")
    private String introduction;

    @ApiModelProperty(value = "语言")
    private String language;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "发布日期")
    private LocalDate pubDate;

    @ApiModelProperty(value = "分类号")
    private Integer classId;

    @ApiModelProperty(value = "库存数量")
    private Integer number;

    @ApiModelProperty(value = "0:正常	1：删除")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "0:已上架  1：已下架")
    private Integer status;


}
