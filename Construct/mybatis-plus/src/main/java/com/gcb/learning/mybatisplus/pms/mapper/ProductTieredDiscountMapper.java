package com.gcb.learning.mybatisplus.pms.mapper;

import com.gcb.learning.mybatisplus.pms.entity.ProductTieredDiscount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 产品阶梯价格表(只针对同商品) Mapper 接口
 * </p>
 *
 * @author gcb
 * @since 2020-02-02
 */
@Mapper
public interface ProductTieredDiscountMapper extends BaseMapper<ProductTieredDiscount> {

}
