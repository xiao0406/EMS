package com.cscec.common.imir.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 设备利用率
 */
@Data
public class DeviceYield {

    /**
     * 设备编号
     */
    private String deviceCode;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 日期
     */
    private String recordDate;
    /**
     * 运行状态
     */
    private int runStatus;
    /**
     * 运行时长
     */
    private BigDecimal runTime;
    /**
     * 焊接时长
     */
    private BigDecimal weldTime;
    /**
     * 焊丝用量
     */
    private BigDecimal wireWeight;
    /**
     * 气体消耗
     */
    private BigDecimal gasUsage;
    /**
     * 电量调号
     */
    private BigDecimal electricUsage;
}
