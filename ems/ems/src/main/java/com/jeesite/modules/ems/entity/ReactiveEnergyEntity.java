package com.jeesite.modules.ems.entity;

import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import lombok.Data;

@Data
public class ReactiveEnergyEntity {
    /**
     * 日期
     */
    private String dateTime;
    /**
     * 类型信息
     */
    private String typeInfo;
    /**
     * 正向无功总电量
     */
    private Double positiveReactiveEnergy;
    /**
     * 反向无功总电量
     */
    private Double reverseReactivePower;

    @ExcelFields({
            @ExcelField(title="日期", attrName="dateTime", align= ExcelField.Align.CENTER, sort=10),
            @ExcelField(title="区域信息/设备名称", attrName="typeInfo", align = ExcelField.Align.CENTER, sort=20),
            @ExcelField(title="正向无功总电量", attrName="positiveReactiveEnergy", align= ExcelField.Align.CENTER, sort=30),
            @ExcelField(title="反向无功总电量", attrName="reverseReactivePower", align= ExcelField.Align.CENTER, sort=40),
    })
    public ReactiveEnergyEntity() {
    }
}
