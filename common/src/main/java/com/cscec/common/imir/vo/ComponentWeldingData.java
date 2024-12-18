package com.cscec.common.imir.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ComponentWeldingData {

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 白班运行时长
     */
    private BigDecimal dayRunTime;

    /**
     * 夜班运行时长
     */
    private BigDecimal nightRunTime;

    /**
     * 白班焊接数量
     */
    private BigDecimal dayCapacity;

    /**
     * 夜班焊接数量
     */
    private BigDecimal nightCapacity;

    /**
     * 白班稼动率
     */
    private BigDecimal dayUtilizationRate;

    /**
     * 夜班稼动率
     */
    private BigDecimal nightUtilizationRate;

    /**
     * 焊丝使用长度
     */
    private BigDecimal wireLength;

    /**
     * 焊丝重量
     */
    private BigDecimal wireWeight;

    /**
     * 时间戳
     */
    private String ts;
}