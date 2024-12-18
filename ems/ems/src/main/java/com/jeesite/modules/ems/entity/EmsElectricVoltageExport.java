package com.jeesite.modules.ems.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 设备电压参数导出entity
 *
 * @author 吴鹏
 * @version 2023-07-22
 */
@Builder
@AllArgsConstructor
@Data
public class EmsElectricVoltageExport extends DataEntity<EmsElectricVoltageExport> {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "数据时间， 2023-03-24 22:00:12")
    private Date dataDateTime;
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    @ApiModelProperty(value = "A相电压")
    private Double aphaseVoltage;
    @ApiModelProperty(value = "B相电压")
    private Double bphaseVoltage;
    @ApiModelProperty(value = "C相电压")
    private Double cphaseVoltage;
    @ApiModelProperty(value = "三相电压不平衡度")
    private Double voltageUnbalanceDegree;

    @ExcelFields({
            @ExcelField(title="时间", attrName = "dataDateTime", align = ExcelField.Align.CENTER, sort = 10, dataFormat="yyyy-MM-dd hh:MM"),
            @ExcelField(title="设备名称", attrName="deviceName", align = ExcelField.Align.CENTER, sort=20),
            @ExcelField(title="A相电压", attrName="aphaseVoltage", align= ExcelField.Align.CENTER, sort=30),
            @ExcelField(title="B相电压", attrName="bphaseVoltage", align= ExcelField.Align.CENTER, sort=40),
            @ExcelField(title="C相电压", attrName="cphaseVoltage", align= ExcelField.Align.CENTER, sort=40),
            @ExcelField(title="三相电压不平衡度", attrName="voltageUnbalanceDegree", align= ExcelField.Align.CENTER, sort=40),
    })
    public EmsElectricVoltageExport() {
        this(null);
    }
    public EmsElectricVoltageExport(String id){
        super(id);
    }
}