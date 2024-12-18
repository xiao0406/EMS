package com.jeesite.modules.ems.entity;

import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.modules.ems.entity.enums.EnergyGroupEnum;
import lombok.Data;

/**
 * 设备峰平谷用电查询实体
 */
@Data
public class TimeSharePowerQueryEntity extends DataEntity<TimeSharePowerQueryEntity> {
    /**
     * 查询维度
     */
    private TemporalGranularityEnum temporalGranularity;
    /**
     * 区域编码
     */
    private String areaCode;
    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 查询开始时间
     */
    private String qryStartTime;
    /**
     * 查询结束时间
     */
    private String qryEndTime;
    /**
     * 查询分组类型
     */
    private EnergyGroupEnum energyGroup;
}
