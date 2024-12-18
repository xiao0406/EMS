package com.jeesite.modules.ems.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 首页Entity
 */
@ApiModel(value = "HomePageEntity对象", description = "首页Entity")
@Data
public class HomePageEntity<T, K, V> extends CompanyVo{

    /**
     * 模块数据
     */
    private T data;

    /**
     * 报表数据
     */
    private EChart<K, V> eChart;

}
