package com.gcb.learning.mybatisplus.pms.mapper;

import com.gcb.learning.mybatisplus.pms.entity.ProductCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 产品的三级分类 Mapper 接口
 * </p>
 *
 * @author gcb
 * @since 2020-02-02
 */
@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {

}
