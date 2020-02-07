package com.gcb.learning.mybatisplus.pms.service.impl;

import com.gcb.learning.mybatisplus.pms.entity.ProductFullDiscount;
import com.gcb.learning.mybatisplus.pms.mapper.ProductFullDiscountMapper;
import com.gcb.learning.mybatisplus.pms.service.ProductFullDiscountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品满减表(只针对同商品) 服务实现类
 * </p>
 *
 * @author gcb
 * @since 2020-02-02
 */
@Service
public class ProductFullDiscountServiceImpl extends ServiceImpl<ProductFullDiscountMapper, ProductFullDiscount> implements ProductFullDiscountService {

}
