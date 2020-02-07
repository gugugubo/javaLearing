package com.gcb.learning.mybatisplus.pms.service.impl;

import com.gcb.learning.mybatisplus.pms.entity.Product;
import com.gcb.learning.mybatisplus.pms.mapper.ProductMapper;
import com.gcb.learning.mybatisplus.pms.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author gcb
 * @since 2020-02-02
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}
