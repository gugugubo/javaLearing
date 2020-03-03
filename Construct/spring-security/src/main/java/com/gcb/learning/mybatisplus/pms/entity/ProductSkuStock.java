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
 * sku的库存
 * </p>
 *
 * @author gcb
 * @since 2020-02-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pms_product_sku_stock")
@ApiModel(value = "ProductSkuStock对象", description = "sku的库存")
public class ProductSkuStock implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "店铺id")
    @TableField("seller_id")
    private String sellerId;

    @TableField("product_id")
    private Long productId;

    @ApiModelProperty(value = "sku编码")
    @TableField("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "价格")
    @TableField("price")
    private BigDecimal price;

    @ApiModelProperty(value = "库存")
    @TableField("stock")
    private Integer stock;

    @ApiModelProperty(value = "预警库存")
    @TableField("low_stock")
    private Integer lowStock;

    @ApiModelProperty(value = "销售属性（sku），使用json格式保存销售属性的信息")
    @TableField("sku_value")
    private String skuValue;

    @ApiModelProperty(value = "展示图片")
    @TableField("pic")
    private String pic;

    @ApiModelProperty(value = "销量")
    @TableField("sale")
    private Integer sale;

    @ApiModelProperty(value = "单品促销价格")
    @TableField("promotion_price")
    private BigDecimal promotionPrice;

    @ApiModelProperty(value = "锁定库存，在有用户下单时进行库存的锁定，用于处理并发中")
    @TableField("lock_stock")
    private Integer lockStock;


}
