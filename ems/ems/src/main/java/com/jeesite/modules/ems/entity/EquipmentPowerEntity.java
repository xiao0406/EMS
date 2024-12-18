package com.jeesite.modules.ems.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "EquipmentPowerEntity对象", description = "功率Entity")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentPowerEntity {
    /**
     * 有功功率
     */
    private Double activePower;
    /**
     * 无功功率
     */
    private Double reactivePower;
    /**
     * 额定功率
     */
    private Double ratedPower;
}
