package com.jeesite.modules.ems.entity;

import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.entity.DataEntity;
import lombok.Data;

@Data
public class DeviceWorkEfficiencyQryEntity extends DataEntity<DeviceWorkEfficiencyQryEntity> {
    /**
     * 查询维度
     */
    private TemporalGranularityEnum temporalGranularity;
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
}
