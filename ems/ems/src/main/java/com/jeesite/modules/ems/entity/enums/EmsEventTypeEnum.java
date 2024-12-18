package com.jeesite.modules.ems.entity.enums;

public enum EmsEventTypeEnum {

    ET_POWER_UP_LIMIT("ET_POWER_UP_LIMIT", "功率超过上限"),
    ET_POWER_LW_LIMIT("ET_POWER_LW_LIMIT", "功率低于下限"),
    ET_CURRENT_UP_LIMIT("ET_CURRENT_UP_LIMIT", "电流超过上限"),
    ET_CURRENT_LW_LIMIT("ET_CURRENT_LW_LIMIT", "电流低于下限"),
    ET_VOLTAGE_UP_LIMIT("ET_VOLTAGE_UP_LIMIT", "电压超过上限"),
    ET_VOLTAGE_LW_LIMIT("ET_VOLTAGE_LW_LIMIT", "电压低于下限"),
    ET_POWER_FACTOR_UP_LIMIT("ET_POWER_FACTOR_UP_LIMIT", "功率因数超过上限"),
    ET_POWER_FACTOR_LW_LIMIT("ET_POWER_FACTOR_LW_LIMIT", "功率因数低于下限"),
    ET_ENERGY_UP_LIMIT("ET_ENERGY_UP_LIMIT", "电量超过上限"),
    ET_TERMINAL_OFF_LINE("ET_TERMINAL_OFF_LINE", "离线告警"),
    ET_LOSE_DATA("ET_LOSE_DATA", "数据丢失"),
    ;

    private String code;
    private String msg;

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    private EmsEventTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
