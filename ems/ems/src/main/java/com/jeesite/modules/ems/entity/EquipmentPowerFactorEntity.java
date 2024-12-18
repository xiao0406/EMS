package com.jeesite.modules.ems.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "EquipmentPowerFactorEntity对象", description = "功率因数Entity")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentPowerFactorEntity {
    /**
     * 参考功率因数
     */
    private Double ratedPowerFactor;
    /**
     * 总功率因数
     */
    private Double totalPowerFactor;
    /**
     * A相功率因数
     */
    private Double aPowerFactor;
    /**
     *B相功率因数
     */
    private Double bPowerFactor;
    /**
     *C相功率因数
     */
    private Double cPowerFactor;
}
