package com.gcb.learning.mybatisplus.pms.mapper;

import com.gcb.learning.mybatisplus.pms.entity.ProductFullDiscount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 产品满减表(只针对同商品) Mapper 接口
 * </p>
 *
 * @author gcb
 * @since 2020-02-02
 */
@Mapper
public interface ProductFullDiscountMapper extends BaseMapper<ProductFullDiscount> {

}
