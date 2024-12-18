package com.jeesite.modules.ems.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 *  设备单日利用率统计entity
 */
@ApiModel(value = "EmsDailyStatisticsEntity对象", description = "设备单日利用率统计entity")
@Data
public class EmsDailyStatisticsEntity {

    /**
     * 电表ID
     */
    private String deviceId;

    /**
     * 总时常
     */
    private String totalTime;

    /**
     * 停机时常
     */
    private String shutDownTime;

    /**
     * 停机率
     */
    private String shutDownRate;

    /**
     * 空载时常
     */
    private String unloadedTime;

    /**
     * 空载率
     */
    private String unloadedRate;

    /**
     * 运行时常
     */
    private String operationTime;

    /**
     * 运行率
     */
    private String operationRate;


}
