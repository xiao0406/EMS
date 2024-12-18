package com.jeesite.modules.ems.entity;

import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import lombok.Data;

@Data
public class DeviceWorkEfficiencyEntity {
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 时间
     */
    private String dateKey;
    /**
     * 产量
     */
    private Double yield;
    /**
     * 用电量
     */
    private Double energy;
    /**
     * 工效
     */
    private Double efficiency;

    @ExcelFields({
            @ExcelField(title="设备名称", attrName="deviceName", align = ExcelField.Align.CENTER, sort=20),
            @ExcelField(title="月份", attrName="dateKey", align= ExcelField.Align.CENTER, sort=30),
            @ExcelField(title="产量（吨）", attrName="yield", align= ExcelField.Align.CENTER, sort=40),
            @ExcelField(title="用电量（kwh）", attrName="energy", align= ExcelField.Align.CENTER, sort=50),
            @ExcelField(title="工效（kwh/吨）", attrName="efficiency", align= ExcelField.Align.CENTER, sort=60),
    })
    public DeviceWorkEfficiencyEntity() {
    }
}
