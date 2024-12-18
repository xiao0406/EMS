package com.jeesite.modules.ems.entity;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.entity.SheetHead;
import com.jeesite.common.json.format.Keep2Format;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * 工效统计entity
 */
@ApiModel(value = "EmsStatisticsEfficacyEntity对象", description = "工效统计entity")
@Data
public class EmsStatisticsEfficacyEntity {

    /**
     * 电表ID
     */
    private String deviceId;

    /**
     * 电表名称
     */
    private String deviceName;

    /**
     * 数据时间
     */
    private String dataDateKey;

    /**
     * 产量
     */
    @JsonSerialize(using = Keep2Format.class)
    private Double yield;

    /**
     * 用电量
     */
    @JsonSerialize(using = Keep2Format.class)
    private Double electricityConsumption;

    /**
     * 功效
     */
    @JsonSerialize(using = Keep2Format.class)
    private Double workEfficiency;
}
