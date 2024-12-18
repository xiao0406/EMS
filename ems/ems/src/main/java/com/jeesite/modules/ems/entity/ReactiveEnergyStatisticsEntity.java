package com.jeesite.modules.ems.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReactiveEnergyStatisticsEntity {
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
}
