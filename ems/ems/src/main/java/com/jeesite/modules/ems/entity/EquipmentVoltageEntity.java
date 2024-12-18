package com.jeesite.modules.ems.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "EquipmentVoltageEntity对象", description = "电压Entity")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentVoltageEntity {
    /**
     * 额定电压
     */
    private Double ratedVoltage;
    /**
     * A相电压
     */
    private Double aVoltage;
    /**
     * B相电压
     */
    private Double bVoltage;
    /**
     * C相电压
     */
    private Double cVoltage;
}
