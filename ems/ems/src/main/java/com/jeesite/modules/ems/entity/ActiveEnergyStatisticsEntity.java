package com.jeesite.modules.ems.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActiveEnergyStatisticsEntity {
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
}
