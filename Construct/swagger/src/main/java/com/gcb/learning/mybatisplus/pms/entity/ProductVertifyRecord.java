package com.gcb.learning.mybatisplus.pms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

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
 * 商品审核记录
 * </p>
 *
 * @author gcb
 * @since 2020-02-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pms_product_vertify_record")
@ApiModel(value = "ProductVertifyRecord对象", description = "商品审核记录")
public class ProductVertifyRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "商品的id")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty(value = "申请审核时间")
    @TableField("create_time")
    private Date createTime;

//    @ApiModelProperty(value = "审核人")
//    @TableField("vertify_man")
//    private String vertifyMan;
//
//    @ApiModelProperty(value = "0->未通过；2->已通过
//
//
//    @TableField("status")
//    private Integer status;
//
//    @ApiModelProperty(value = "反馈详情")
//    @TableField("detail")
//    private String detail;


}
