package com.gcb.learning.mybatisplus.pms.service.impl;

import com.gcb.learning.mybatisplus.pms.entity.ProductCategory;
import com.gcb.learning.mybatisplus.pms.mapper.ProductCategoryMapper;
import com.gcb.learning.mybatisplus.pms.service.ProductCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品的三级分类 服务实现类
 * </p>
 *
 * @author gcb
 * @since 2020-02-02
 */
@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

}
