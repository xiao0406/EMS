package com.jeesite.modules.ems.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "EquipmentCurrentEntity对象", description = "电流Entity")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentCurrentEntity {
    /**
     * 额定电流
     */
    private Double ratedCurrent;
    /**
     *  A相电流
     */
    private Double aCurrent;
    /**
     * B相电流
     */
    private Double bCurrent;
    /**
     * C相电流
     */
    private Double cCurrent;
}
