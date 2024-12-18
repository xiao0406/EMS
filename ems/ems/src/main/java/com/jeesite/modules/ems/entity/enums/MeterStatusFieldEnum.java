package com.jeesite.modules.ems.entity.enums;

/**
 * 电表状态参数字段枚举
 */
public enum MeterStatusFieldEnum {
    ACTIVE_POWER("ACTIVE_POWER", "有功功率"),
    REACTIVE_POWER("REACTIVE_POWER", "无功功率"),
    CURRENT("CURRENT", "电流"),
    VOLTAGE("VOLTAGE", "电压"),
    POWER_FACTOR("POWER_FACTOR", "功率因数");

    private String code;
    private String msg;

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    private MeterStatusFieldEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
