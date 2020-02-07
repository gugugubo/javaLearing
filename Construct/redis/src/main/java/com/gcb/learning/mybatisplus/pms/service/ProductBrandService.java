package com.gcb.learning.mybatisplus.pms.service;

import com.gcb.learning.mybatisplus.pms.common.PageInfoVo;
import com.gcb.learning.mybatisplus.pms.entity.ProductBrand;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品的品牌表 服务类
 * </p>
 *
 * @author gcb
 * @since 2020-02-02
 */
public interface ProductBrandService extends IService<ProductBrand> {

    List<ProductBrand> listAllBrand();

    int createBrand(ProductBrand pmsBrand);

    int updateBrand(Long id, ProductBrand pmsBrandDto);

    int deleteBrand(Long id);

    PageInfoVo listBrand(Integer pageNum, Integer pageSize);

    Object getBrand(Long id);
}
