package com.gcb.learning.mybatisplus.pms.mapper;

import com.gcb.learning.mybatisplus.pms.entity.ProductSkuStock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * sku的库存 Mapper 接口
 * </p>
 *
 * @author gcb
 * @since 2020-02-02
 */
@Mapper
public interface ProductSkuStockMapper extends BaseMapper<ProductSkuStock> {

}
