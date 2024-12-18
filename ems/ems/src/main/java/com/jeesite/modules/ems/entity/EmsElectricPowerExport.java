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
 * 电耗表有功无功功率导出Entity
 *
 * @author 吴鹏
 * @version 2023-07-22
 */
@Builder
@AllArgsConstructor
@Data
public class EmsElectricPowerExport extends DataEntity<EmsElectricPowerExport> {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "数据时间， 2023-03-24 22:00:12")
    private Date dataDateTime;
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    @ApiModelProperty(value = "总有功功率")
    private Double totalActivePower;
    @ApiModelProperty(value = "A相有功功率")
    private Double aphaseActivePower;
    @ApiModelProperty(value = "B相有功功率")
    private Double bphaseActivePower;
    @ApiModelProperty(value = "C相有功功率")
    private Double cphaseActivePower;
    @ApiModelProperty(value = "总无功功率")
    private Double totalReactivePower;
    @ApiModelProperty(value = "A相无功功率")
    private Double aphaseReactivePower;
    @ApiModelProperty(value = "B相无功功率")
    private Double bphaseReactivePower;
    @ApiModelProperty(value = "C相无功功率")
    private Double cphaseReactivePower;

    @ExcelFields({
            @ExcelField(title="时间", attrName = "dataDateTime", align = ExcelField.Align.CENTER, sort = 10, dataFormat="yyyy-MM-dd hh:MM"),
            @ExcelField(title="设备名称", attrName="deviceName", align = ExcelField.Align.CENTER, sort=20),
            @ExcelField(title="总有功功率（kW）", attrName="totalActivePower", align= ExcelField.Align.CENTER, sort=30),
            @ExcelField(title="A相有功功率", attrName="aphaseActivePower", align= ExcelField.Align.CENTER, sort=40),
            @ExcelField(title="B相有功功率", attrName="bphaseActivePower", align= ExcelField.Align.CENTER, sort=40),
            @ExcelField(title="C相有功功率", attrName="cphaseActivePower", align= ExcelField.Align.CENTER, sort=40),
            @ExcelField(title="总无功功率（kW）", attrName="totalReactivePower", align= ExcelField.Align.CENTER, sort=40),
            @ExcelField(title="A相无功功率", attrName="aphaseReactivePower", align= ExcelField.Align.CENTER, sort=40),
            @ExcelField(title="B相无功功率", attrName="bphaseReactivePower", align= ExcelField.Align.CENTER, sort=40),
            @ExcelField(title="C相无功功率", attrName="cphaseReactivePower", align= ExcelField.Align.CENTER, sort=40),
    })
    public EmsElectricPowerExport() {
        this(null);
    }
    public EmsElectricPowerExport(String id){
        super(id);
    }
}