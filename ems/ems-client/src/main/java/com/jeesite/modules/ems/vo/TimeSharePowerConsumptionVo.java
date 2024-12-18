package com.jeesite.modules.ems.vo;

import lombok.Data;

@Data
public class TimeSharePowerConsumptionVo {

    /**
     * 日期
     */
    private String dateTime;
    /**
     * 类型信息
     */
    private String typeInfo;
    /**
     * 总用电（kwh）
     */
    private Double totalEnergy;

}
