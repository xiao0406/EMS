package com.jeesite.modules.ems.entity.enums;

/**
 * 能源管理 设备状态
 */
public enum DeviceStatusEnum {
    STATUS_STOP("1", "停机"),
    STATUS_NO_LOAD("2", "空载"),
    STATUS_RUNNING("3", "运行"),
    STATUS_UNKNOWN("4", "缺少状态计算数据，状态未知");


    private String code;
    private String msg;

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    private DeviceStatusEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
