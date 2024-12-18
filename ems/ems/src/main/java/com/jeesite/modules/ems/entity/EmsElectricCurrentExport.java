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
 * 设备电流参数导出entity
 *
 * @author 吴鹏
 * @version 2023-07-22
 */
@Builder
@AllArgsConstructor
@Data
public class EmsElectricCurrentExport extends DataEntity<EmsElectricCurrentExport> {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "数据时间， 2023-03-24 22:00:12")
    private Date dataDateTime;
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    @ApiModelProperty(value = "A相电流")
    private Double aphaseCurrent;
    @ApiModelProperty(value = "B相电流")
    private Double bphaseCurrent;
    @ApiModelProperty(value = "C相电流")
    private Double cphaseCurrent;
    @ApiModelProperty(value = "三相电流不平衡度")
    private Double currentUnbalanceDegree;

    @ExcelFields({
            @ExcelField(title="时间", attrName = "dataDateTime", align = ExcelField.Align.CENTER, sort = 10, dataFormat="yyyy-MM-dd hh:MM"),
            @ExcelField(title="设备名称", attrName="deviceName", align = ExcelField.Align.CENTER, sort=20),
            @ExcelField(title="A相电流", attrName="aphaseCurrent", align= ExcelField.Align.CENTER, sort=30),
            @ExcelField(title="B相电流", attrName="bphaseCurrent", align= ExcelField.Align.CENTER, sort=40),
            @ExcelField(title="C相电流", attrName="cphaseCurrent", align= ExcelField.Align.CENTER, sort=40),
            @ExcelField(title="三相电流不平衡度", attrName="currentUnbalanceDegree", align= ExcelField.Align.CENTER, sort=40),
    })
    public EmsElectricCurrentExport() {
        this(null);
    }
    public EmsElectricCurrentExport(String id){
        super(id);
    }
}