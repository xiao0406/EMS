package com.jeesite.modules.ems.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import lombok.Data;

@Data
public class TimeSharePowerConsumptionEntity extends DataEntity<TimeSharePowerConsumptionEntity> {
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
    /**
     * 尖时刻（kwh）
     */
    private String cuspTimeEnergy;
    /**
     * 峰时刻（kwh）
     */
    private String peakTimeEnergy;
    /**
     * 平时刻（kwh）
     */
    private String fairTimeEnergy;
    /**
     * 谷时刻（kwh）
     */
    private String valleyTimeEnergy;

    @ExcelFields({
            @ExcelField(title = "时间", attrName = "dateTime", align = ExcelField.Align.CENTER, sort = 10),
            @ExcelField(title = "位置信息", attrName = "typeInfo", align = ExcelField.Align.CENTER, sort = 20),
            @ExcelField(title = "总用电（kwh）", attrName = "totalEnergy", align = ExcelField.Align.CENTER, sort = 30),
            @ExcelField(title = "尖时段（kwh）", attrName = "cuspTimeEnergy", align = ExcelField.Align.CENTER, sort = 40),
            @ExcelField(title = "峰时段（kwh）", attrName = "peakTimeEnergy", align = ExcelField.Align.CENTER, sort = 50),
            @ExcelField(title = "平时段（kwh）", attrName = "fairTimeEnergy", align = ExcelField.Align.CENTER, sort = 60),
            @ExcelField(title = "谷时段（kwh）", attrName = "valleyTimeEnergy", align = ExcelField.Align.CENTER, sort = 70),
    })
    public TimeSharePowerConsumptionEntity() {
    }
}
