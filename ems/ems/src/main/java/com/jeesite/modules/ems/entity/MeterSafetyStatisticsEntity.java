package com.jeesite.modules.ems.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "MeterSafetyStatisticsEntity对象", description = "安全/故障表头Entity")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeterSafetyStatisticsEntity {

    /**
     * 功率
     */
    private EquipmentPowerEntity power;

    /**
     * 电流
     */
    private EquipmentCurrentEntity current;

    /**
     * 电压
     */
    private EquipmentVoltageEntity voltage;

    /**
     * 功率因数
     */
    private EquipmentPowerFactorEntity powerFactor;
}
