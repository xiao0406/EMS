package com.jeesite.modules.dcs.sx.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SxGcProdDTO {

    /**
     * 设备号
     */
    private String deviceNumber;

    /**
     * 日期
     */
    private LocalDate date;

    /**
     * 白班运行时间
     */
    private Double runTimeDay;

    /**
     * 夜班运行时间
     */
    private Double runTimeNight;

    /**
     * 白班重量
     */
    private Double yieldDay;

    /**
     * 夜班重量
     */
    private Double yieldNight;

    /**
     * 白班稼动率
     */
    private Double utilizationDay;

    /**
     * 夜班稼动率
     */
    private Double utilizationNight;

    /**
     * 白班有效米数
     */
    private Double metersDay;

    /**
     * 夜班有效米数
     */
    private Double metersNight;

    /**
     * 白班不良率
     */
    private Double defectRateDay;

    /**
     * 夜班不良率
     */
    private Double defectRateNight;

}
