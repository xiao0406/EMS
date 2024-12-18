package com.jeesite.modules.ems.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jeesite.common.json.format.Keep2Format;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "电表示数报表对象", description = "电表示数报表对象")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeterPendulumDisplayEntity {

    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 正向有功电能
     */
    private String positiveActiveEnergyDisplayStart;
    /**
     * 正向有功电能
     */
    private String positiveActiveEnergyDisplayEnd;
    /**
     * 综合倍率
     */
    @JsonSerialize(using = Keep2Format.class)
    private Double qt;
    /**
     * 差值
     */
    private String positiveActiveEnergyDisplayDiffValue;
    /**
     * 耗电量
     */
    private String positiveActiveEnergyConsumption;
}
