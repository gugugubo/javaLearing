package com.gcb.learning.mybatisplus.pms.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
public class PageInfoVo implements Serializable {

    @ApiModelProperty("总记录数")
    private Long total;

    @ApiModelProperty("总页数")
    private Long totalpage;

    @ApiModelProperty("每页的数据条数")
    private Long pageSize;

    @ApiModelProperty("当前页码")
    private Long pageNum;

    @ApiModelProperty("当前页的数据")
    private List<? extends Object> list;

    public static PageInfoVo getVo(IPage iPage, Long pageSize) {
        return new PageInfoVo(iPage.getTotal(), iPage.getPages(), pageSize, iPage.getCurrent(), iPage.getRecords());

    }
}
