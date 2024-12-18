package com.jeesite.modules.ems.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnergyManageStatisticsEntity {
    /**
     * 正向有功总电量
     */
    private Double positiveActiveEnergy;
    /**
     * 最大值
     */
    private Double positiveActiveEnergyMax;
    /**
     * 最小值
     */
    private Double positiveActiveEnergyMin;
    /**
     * 峰谷差
     */
    private Double positiveActiveEnergyDiff;
    /**
     * 平均电能
     */
    private Double positiveActiveEnergyAverage;
    /**
     * 反向有功总电量
     */
    private Double reverseActivePower;
    /**
     * 最大值
     */
    private Double reverseActivePowerMax;
    /**
     * 最小值
     */
    private Double reverseActivePowerMin;
    /**
     * 峰谷差
     */
    private Double reverseActivePowerDiff;
    /**
     * 平均电能
     */
    private Double reverseActivePowerAverage;
    /**
     * 正向无功总电量
     */
    private Double positiveReactiveEnergy;
    /**
     * 最大值
     */
    private Double positiveReactiveEnergyMax;
    /**
     * 最小值
     */
    private Double positiveReactiveEnergyMin;
    /**
     * 峰谷差
     */
    private Double positiveReactiveEnergyDiff;
    /**
     * 平均电能
     */
    private Double positiveReactiveEnergyAverage;
    /**
     * 反向无功总电量
     */
    private Double reverseReactivePower;
    /**
     * 最大值
     */
    private Double reverseReactivePowerMax;
    /**
     * 最小值
     */
    private Double reverseReactivePowerMin;
    /**
     * 峰谷差
     */
    private Double reverseReactivePowerDiff;
    /**
     * 平均电能
     */
    private Double reverseReactivePowerAverage;
}
