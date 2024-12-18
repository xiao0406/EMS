package com.jeesite.modules.ems.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 *  设备单日利用率统计列表Entity
 */
@ApiModel(value = "EmsDailyStatisticsListEntity对象", description = "设备单日利用率统计列表Entity")
@Data
public class EmsDailyStatisticsListEntity {

    /**
     *  日期
     */
    private String dataDate;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 总时常
     */
    private Double totalTime;

    /**
     * 停机时常
     */
    private Double shutDownTime;

    /**
     * 空载时常
     */
    private Double unloadedTime;

    /**
     * 运行时常
     */
    private Double operationTime;

    /**
     * 总用电
     */
    private Double positiveActiveEnergy;
}
