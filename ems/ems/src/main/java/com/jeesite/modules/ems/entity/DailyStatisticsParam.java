package com.jeesite.modules.ems.entity;

import com.jeesite.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel(value = "单日设备利用率入参", description = "单日设备利用率入参Entity")
@Data
public class DailyStatisticsParam extends BaseEntity {
    /**
     * 查询日期
     */
    @NotNull(message = "查询日期不能为空")
    private Date date;

    /**
     * 电表ID
     */
    @NotNull(message = "电表ID不能为空")
    private String deviceId;
}
