package com.gcb.learning.mybatisplus.pms.service.impl;

import com.gcb.learning.mybatisplus.pms.entity.ProductTieredDiscount;
import com.gcb.learning.mybatisplus.pms.mapper.ProductTieredDiscountMapper;
import com.gcb.learning.mybatisplus.pms.service.ProductTieredDiscountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品阶梯价格表(只针对同商品) 服务实现类
 * </p>
 *
 * @author gcb
 * @since 2020-02-02
 */
@Service
public class ProductTieredDiscountServiceImpl extends ServiceImpl<ProductTieredDiscountMapper, ProductTieredDiscount> implements ProductTieredDiscountService {

}
