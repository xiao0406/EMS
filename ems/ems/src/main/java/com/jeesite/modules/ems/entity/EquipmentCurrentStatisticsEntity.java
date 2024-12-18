package com.jeesite.modules.ems.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@ApiModel(value = "EquipmentCurrentEntity对象", description = "电流Entity")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentCurrentStatisticsEntity {
    /**
     * 最大
     */
    private Double max;
    /**
     *  最大时间
     */
    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date maxTime;
    /**
     * 最小
     */
    private Double min;
    /**
     * 最小时间
     */
    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date minTime;
}
