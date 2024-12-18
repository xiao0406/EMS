package com.jeesite.modules.ems.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 *  设备有功和无功功率
 */
@ApiModel(value = "EmsDevicePowerEntity对象", description = "设备有功无功功率entity")
@Data
public class EmsDevicePowerEntity {

    /**
     * 实时刻度
     */
    private String dataTime;

    /**
     * 有功实时
     */
    private Double currentData;

    /**
     * 有功最大
     */
    private Double max;

    /**
     * 有功最小
     */
    private Double min;

    /**
     * 有功峰谷差
     */
    private Double diff;

    /**
     * 无功实时
     */
    private Double currentDataRe;

    /**
     * 无功最大
     */
    private Double maxRe;

    /**
     * 无功最小
     */
    private Double minRe;

    /**
     * 无功峰谷差
     */
    private Double diffRe;


}
