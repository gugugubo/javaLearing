package com.gcb.learning.mybatisplus.pms.mapper;

import com.gcb.learning.mybatisplus.pms.entity.ProductAttributeCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 产品属性分类表，属性分为规格（sku）和参数(spu)，规格就是购买某件商品再具体选择比如大小颜色之类的还有库存等属性也是具体到某一个规格的而不是某商品 根据规格决定商品具体价格 属性就是一些要展示的公共特征 该商品的材质产地做工这些  Mapper 接口
 * </p>
 *
 * @author gcb
 * @since 2020-02-02
 */
//@Mapper
public interface ProductAttributeCategoryMapper extends BaseMapper<ProductAttributeCategory> {

}
