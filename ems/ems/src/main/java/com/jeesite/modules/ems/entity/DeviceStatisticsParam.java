package com.jeesite.modules.ems.entity;

import com.jeesite.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel(value = "单日设备利用率入参", description = "单日设备利用率入参Entity")
@Data
public class DeviceStatisticsParam extends BaseEntity {

    /**
     * 查询开始日期
     */
    private Date start;


    /**
     * 查询结束日期
     */
    private Date end;


    /**
     * 单日查询日期
     */
    private Date date;

    /**
     * 电表ID
     */
    @NotNull(message = "电表ID不能为空")
    private String deviceId;

    /**
     * 查询维度 时间点 VD_DayTime  日 VD_Day  月 VD_Month 电能 Power 电压 Voltage  电流 Current
     */
    private String temporalGranularity;

}
