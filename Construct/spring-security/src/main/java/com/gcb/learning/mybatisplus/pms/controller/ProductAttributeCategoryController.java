package com.gcb.learning.mybatisplus.pms.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 产品属性分类表，属性分为规格（sku）和参数(spu)，规格就是购买某件商品再具体选择比如大小颜色之类的还有库存等属性也是具体到某一个规格的而不是某商品 根据规格决定商品具体价格 属性就是一些要展示的公共特征 该商品的材质产地做工这些  前端控制器
 * </p>
 *
 * @author gcb
 * @since 2020-02-02
 */
@RestController
@RequestMapping("/pms/product-attribute-category")
public class ProductAttributeCategoryController {

}
