package com.gcb.learning.mybatisplus.pms.controller;


import com.gcb.learning.mybatisplus.pms.common.CommonResult;
import com.gcb.learning.mybatisplus.pms.common.PageInfoVo;
import com.gcb.learning.mybatisplus.pms.entity.ProductBrand;
import com.gcb.learning.mybatisplus.pms.service.ProductBrandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品的品牌表 前端控制器
 * </p>
 *
 * @author gcb
 * @since 2020-02-02
 */
@RestController
@RequestMapping("/pms/product-brand")
public class ProductBrandController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductBrandController.class);

    @Autowired
    private ProductBrandService demoService;

    @RequestMapping(value = "listAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getBrandList() {
        return new CommonResult().success(demoService.listAllBrand());
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult createBrand(@RequestBody ProductBrand pmsBrand) {
        CommonResult commonResult;
        int count = demoService.createBrand(pmsBrand);
        if (count == 1) {
            commonResult = new CommonResult().success(pmsBrand);
            LOGGER.debug("createBrand success:{}", pmsBrand);
        } else {
            commonResult = new CommonResult().failed();
            LOGGER.debug("createBrand failed:{}", pmsBrand);
        }
        return commonResult;
    }


    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateBrand(@PathVariable("id") Long id, @RequestBody ProductBrand pmsBrandDto, BindingResult result) {
        CommonResult commonResult;
        int count = demoService.updateBrand(id, pmsBrandDto);
        if (count == 1) {
            commonResult = new CommonResult().success(pmsBrandDto);
            LOGGER.debug("updateBrand success:{}", pmsBrandDto);
        } else {
            commonResult = new CommonResult().failed();
            LOGGER.debug("updateBrand failed:{}", pmsBrandDto);
        }
        return commonResult;
    }


    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult deleteBrand(@PathVariable("id") Long id) {
        int count = demoService.deleteBrand(id);
        if (count == 1) {
            LOGGER.debug("deleteBrand success :id={}", id);
            return new CommonResult().success(null);
        } else {
            LOGGER.debug("deleteBrand failed :id={}", id);
            return new CommonResult().failed();
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult listBrand(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "3") Integer pageSize) {
        PageInfoVo brandList = demoService.listBrand(pageNum, pageSize);
        return new CommonResult().success(brandList);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult brand(@PathVariable("id") Long id) {
        return new CommonResult().success(demoService.getBrand(id));
    }

}
