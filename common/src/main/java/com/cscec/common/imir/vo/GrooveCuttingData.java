package com.cscec.common.imir.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GrooveCuttingData {

    /**
     * 设备号
     */
    private String deviceNumber;

    /**
     * 日期
     */
    private LocalDate date;

    /**
     * 白班产量
     */
    private Double yieldDay;

    /**
     * 夜班产量
     */
    private Double yieldNight;

    /**
     * 白班切割米数
     */
    private Double metersDay;

    /**
     * 夜班切割米数
     */
    private Double metersNight;

    /**
     * 白班运行时间
     */
    private Double runTimeDay;

    /**
     * 夜班运行时间
     */
    private Double runTimeNight;

    /**
     * 白班不良率
     */
    private Double defectRateDay;

    /**
     * 夜班不良率
     */
    private Double defectRateNight;

    /**
     * 白班稼动率
     */
    private Double utilizationDay;

    /**
     * 夜班稼动率
     */
    private Double utilizationNight;
}
