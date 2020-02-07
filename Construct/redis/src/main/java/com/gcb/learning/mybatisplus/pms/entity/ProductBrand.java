package com.gcb.learning.mybatisplus.pms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品的品牌表
 * </p>
 *
 * @author gcb
 * @since 2020-02-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pms_product_brand")
@ApiModel(value = "ProductBrand对象", description = "商品的品牌表")
public class ProductBrand implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "品牌的名字")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "品牌首字母")
    @TableField("first_letter")
    private String firstLetter;

    @ApiModelProperty(value = "排序")
    @TableField("sort")
    private Integer sort;

    @ApiModelProperty(value = "展示状态")
    @TableField("show_status")
    private Integer showStatus;

    @ApiModelProperty(value = "品牌logo")
    @TableField("logo")
    private String logo;

    @ApiModelProperty(value = "品牌介绍")
    @TableField("brand_story")
    private String brandStory;


}
