package com.gcb.learning.mybatisplus.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gcb.learning.mybatisplus.pms.common.PageInfoVo;
import com.gcb.learning.mybatisplus.pms.entity.ProductBrand;
import com.gcb.learning.mybatisplus.pms.mapper.ProductBrandMapper;
import com.gcb.learning.mybatisplus.pms.service.ProductBrandService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品的品牌表 服务实现类
 * </p>
 *
 * @author gcb
 * @since 2020-02-02
 */
@Service
public class ProductBrandServiceImpl extends ServiceImpl<ProductBrandMapper, ProductBrand> implements ProductBrandService {

    @Autowired
    ProductBrandMapper productBrandMapper;

    @Override
    public List<ProductBrand> listAllBrand() {
        List<ProductBrand> list = productBrandMapper.selectList(null);
        return list;
    }

    @Override
    public int createBrand(ProductBrand pmsBrand) {
        return productBrandMapper.insert(pmsBrand);
    }

    @Override
    public int updateBrand(Long id, ProductBrand pmsBrandDto) {
        pmsBrandDto.setId(id);
        return productBrandMapper.updateById(pmsBrandDto);
    }

    @Override
    public int deleteBrand(Long id) {
        return productBrandMapper.deleteById(id);
    }

    @Override
    public PageInfoVo listBrand(Integer pageNum, Integer pageSize) {
//        new QueryWrapper<>()
        IPage<ProductBrand> productBrandIPage = productBrandMapper.selectPage(new Page<ProductBrand>(pageNum, pageSize), null);
        PageInfoVo pageInfoVo = PageInfoVo.getVo(productBrandIPage, pageSize.longValue());
        return pageInfoVo;
    }

    @Override
    public Object getBrand(Long id) {

        return productBrandMapper.selectById(id);
    }


}
