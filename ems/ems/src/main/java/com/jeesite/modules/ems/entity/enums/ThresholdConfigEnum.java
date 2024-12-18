package com.jeesite.modules.ems.entity.enums;

/**
 * 电表阈值配置类型
 */
public enum ThresholdConfigEnum {
    TC_Power("TC_Power", "功率"),
    TC_Voltage("TC_Voltage", "电压"),
    TC_Current("TC_Current", "电流"),
    TC_PowerFactor("TC_PowerFactor", "功率因数"),
    TC_Energy("TC_Energy", "电量");

    private String code;
    private String msg;

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    private ThresholdConfigEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
