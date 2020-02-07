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
 * 产品属性分类表，属性分为规格（sku）和参数(spu)，规格就是购买某件商品再具体选择比如大小颜色之类的还有库存等属性也是具体到某一个规格的而不是某商品 根据规格决定商品具体价格 属性就是一些要展示的公共特征 该商品的材质产地做工这些
 * </p>
 *
 * @author gcb
 * @since 2020-02-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pms_product_attribute_category")
@ApiModel(value = "ProductAttributeCategory对象", description = "产品属性分类表，属性分为规格（sku）和参数(spu)，规格就是购买某件商品再具体选择比如大小颜色之类的还有库存等属性也是具体到某一个规格的而不是某商品 根据规格决定商品具体价格 属性就是一些要展示的公共特征 该商品的材质产地做工这些 ")
public class ProductAttributeCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @ApiModelProperty(value = "属性数量")
    @TableField("attribute_count")
    private Integer attributeCount;

    @ApiModelProperty(value = "参数数量")
    @TableField("param_count")
    private Integer paramCount;


}
