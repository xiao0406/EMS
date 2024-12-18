package com.cscec.common.imir.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 设备利用率
 */
@Data
public class DeviceRatio {

    /**
     * 设备编号
     */
    private String deviceCode;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 运行时长
     */
    private BigDecimal runTime;
    /**
     * 焊接时长
     */
    private BigDecimal weldTime;
    /**
     * 设备利用率
     */
    private BigDecimal utilizationRatio;
}
