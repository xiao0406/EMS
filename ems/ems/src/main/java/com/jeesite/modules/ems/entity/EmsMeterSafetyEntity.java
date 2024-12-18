package com.jeesite.modules.ems.entity;

import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.modules.ems.entity.enums.EnergyGroupEnum;
import com.jeesite.modules.ems.entity.enums.MeterStatusFieldEnum;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "EmsMeterSafetyEntity对象", description = "安全/故障Entity")
@Data
public class EmsMeterSafetyEntity<T> extends DataEntity<EmsMeterSafetyEntity<T>> {
    /**
     * 设备编码
     */
    private String deviceId;
    /**
     * 查询时间
     */
    private String qryDate;
    /**
     * 查询数据类别
     */
    private MeterStatusFieldEnum meterStatusField;
    /**
     * 返回数据
     */
    private T result;
}
