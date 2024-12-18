package com.jeesite.modules.ems.entity.enums;

/**
 * 能源管理 分组查询类型
 */
public enum EnergyGroupEnum {
    GROUP_Device("GROUP_Device", "按设备"),
    GROUP_Area("GROUP_Area", "按区域");

    private String code;
    private String msg;

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    private EnergyGroupEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
