package com.jeesite.modules.ems.entity;

import lombok.Data;

/**
 * 设备峰平谷用电综合查询返回实体
 */
@Data
public class TimeShareStatisticsEntity {
    /**
     * 总用电量
     */
    private Double totalEnergy;
    /**
     * 尖时刻用电量
     */
    private Double cuspTimeEnergy;
    /**
     * 尖时刻用占比
     */
    private Double cuspTimeEnergyPercent;
    /**
     * 峰时刻用电量
     */
    private Double peakTimeEnergy;
    /**
     * 峰时刻用占比
     */
    private Double peakTimeEnergyPercent;
    /**
     * 平时刻用电量
     */
    private Double fairTimeEnergy;
    /**
     * 平时刻用占比
     */
    private Double fairTimeEnergyPercent;
    /**
     * 谷时刻用电量
     */
    private Double valleyTimeEnergy;
    /**
     * 谷时刻用占比
     */
    private Double valleyTimeEnergyPercent;
}
