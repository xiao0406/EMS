package com.cscec.common.imir.dto.state;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WeldDeviceStatus {

    /**
     * 运行模式，0：手动；1：自动
     */
    private Integer runMode;

    /**
     * 锁定 是、否
     */
    private String lock;

    /**
     * 预置电流
     */
    private BigDecimal presetElectrical = BigDecimal.ZERO;

    /**
     * 预置电流
     */
    private BigDecimal presetVoltage = BigDecimal.ZERO;

    /**
     * 焊接电流
     */
    private BigDecimal electrical = BigDecimal.ZERO;

    /**
     * 焊接电压
     */
    private BigDecimal voltage = BigDecimal.ZERO;

    /**
     * 材质
     */
    private String wireType;

    /**
     * 焊丝直径
     */
    private BigDecimal wireDiameter;

    /**
     * 气体类型
     */
    private String gasType;

    /**
     * 焊接模式
     */
    private String weldMode;

    /**
     * 焊接控制
     */
    private String weldControl;
}
