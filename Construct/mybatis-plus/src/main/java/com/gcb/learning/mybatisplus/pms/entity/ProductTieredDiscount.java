package com.gcb.learning.mybatisplus.pms.entity;

import java.math.BigDecimal;

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
 * 产品阶梯价格表(只针对同商品)
 * </p>
 *
 * @author gcb
 * @since 2020-02-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pms_product_tiered_discount")
@ApiModel(value = "ProductTieredDiscount对象", description = "产品阶梯价格表(只针对同商品)")
public class ProductTieredDiscount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("product_id")
    private Long productId;

    @ApiModelProperty(value = "满足的商品数量")
    @TableField("count")
    private Integer count;

    @ApiModelProperty(value = "折扣率")
    @TableField("discount")
    private BigDecimal discount;

    @ApiModelProperty(value = "或者直接设置可以打折的价格")
    @TableField("price")
    private BigDecimal price;


}
