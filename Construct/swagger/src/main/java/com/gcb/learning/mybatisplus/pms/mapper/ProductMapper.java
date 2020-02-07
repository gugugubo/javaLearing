package com.gcb.learning.mybatisplus.pms.mapper;

import com.gcb.learning.mybatisplus.pms.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 商品信息 Mapper 接口
 * </p>
 *
 * @author gcb
 * @since 2020-02-02
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

}
