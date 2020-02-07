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
 * 存储商品参数(spu)属性信息的表
 * </p>
 *
 * @author gcb
 * @since 2020-02-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pms_product_param_attribute_value")
@ApiModel(value = "ProductParamAttributeValue对象", description = "存储商品参数(spu)属性信息的表")
public class ProductParamAttributeValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "商品id")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty(value = "属性id")
    @TableField("product_attribute_id")
    private Long productAttributeId;

    @ApiModelProperty(value = "参数属性（spu）的值")
    @TableField("value")
    private String value;


}
