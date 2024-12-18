package com.jeesite.modules.dcs.sx.entity;

import java.time.LocalDate;
import javax.validation.constraints.Size;

import com.jeesite.common.entity.DataEntity;
import io.swagger.annotations.*;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 坡口切割单元生产数据记录表Entity
 * @author ds
 * @version 2023-06-29
 */
@Table(name="dcs_sx_gc_prod", alias="a", label="坡口切割单元生产数据记录表信息", columns={
        @Column(name="id", attrName="id", label="主键", isPK=true),
        @Column(name="device_code", attrName="deviceCode", label="设备编号"),
        @Column(name="record_date", attrName="recordDate", label="记录日期"),
        @Column(name="device_name", attrName="deviceName", label="设备名称", queryType=QueryType.LIKE),
        @Column(name="yield_day", attrName="yieldDay", label="白班产量", isUpdateForce=true),
        @Column(name="yield_night", attrName="yieldNight", label="夜班产量", isUpdateForce=true),
        @Column(name="run_time_day", attrName="runTimeDay", label="白班运行时间", isUpdateForce=true),
        @Column(name="run_time_night", attrName="runTimeNight", label="夜班运行时间", isUpdateForce=true),
        @Column(name="utilization_day", attrName="utilizationDay", label="白班稼动率", isUpdateForce=true),
        @Column(name="utilization_night", attrName="utilizationNight", label="夜班稼动率", isUpdateForce=true),
        @Column(name="meters_day", attrName="metersDay", label="白班切割米数", isUpdateForce=true),
        @Column(name="meters_night", attrName="metersNight", label="夜班切割米数", isUpdateForce=true),
        @Column(name="defect_rate_day", attrName="defectRateDay", label="白班不良率", isUpdateForce=true),
        @Column(name="defect_rate_night", attrName="defectRateNight", label="夜班不良率", isUpdateForce=true),
        @Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
        @Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
        @Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
        @Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
}, orderBy="a.update_date DESC"
)
@ApiModel(value = "SxGcProd对象", description = "坡口切割单元生产数据记录表Entity")
public class SxGcProd extends DataEntity<SxGcProd> {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty(value = "设备编号")
    private String deviceCode;
    @ApiModelProperty(value = "记录日期")
    private LocalDate recordDate;
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    @ApiModelProperty(value = "白班产量")
    private Double yieldDay;
    @ApiModelProperty(value = "夜班产量")
    private Double yieldNight;
    @ApiModelProperty(value = "白班运行时间")
    private Double runTimeDay;
    @ApiModelProperty(value = "夜班运行时间")
    private Double runTimeNight;
    @ApiModelProperty(value = "白班稼动率")
    private Double utilizationDay;
    @ApiModelProperty(value = "夜班稼动率")
    private Double utilizationNight;
    @ApiModelProperty(value = "白班切割米数")
    private Double metersDay;
    @ApiModelProperty(value = "夜班切割米数")
    private Double metersNight;
    @ApiModelProperty(value = "白班不良率")
    private Double defectRateDay;
    @ApiModelProperty(value = "夜班不良率")
    private Double defectRateNight;

    public SxGcProd() {
        this(null);
    }

    public SxGcProd(String id){
        super(id);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    @Size(min=0, max=100, message="设备名称长度不能超过 100 个字符")
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Double getYieldDay() {
        return yieldDay;
    }

    public void setYieldDay(Double yieldDay) {
        this.yieldDay = yieldDay;
    }

    public Double getYieldNight() {
        return yieldNight;
    }

    public void setYieldNight(Double yieldNight) {
        this.yieldNight = yieldNight;
    }

    public Double getRunTimeDay() {
        return runTimeDay;
    }

    public void setRunTimeDay(Double runTimeDay) {
        this.runTimeDay = runTimeDay;
    }

    public Double getRunTimeNight() {
        return runTimeNight;
    }

    public void setRunTimeNight(Double runTimeNight) {
        this.runTimeNight = runTimeNight;
    }

    public Double getUtilizationDay() {
        return utilizationDay;
    }

    public void setUtilizationDay(Double utilizationDay) {
        this.utilizationDay = utilizationDay;
    }

    public Double getUtilizationNight() {
        return utilizationNight;
    }

    public void setUtilizationNight(Double utilizationNight) {
        this.utilizationNight = utilizationNight;
    }

    public Double getMetersDay() {
        return metersDay;
    }

    public void setMetersDay(Double metersDay) {
        this.metersDay = metersDay;
    }

    public Double getMetersNight() {
        return metersNight;
    }

    public void setMetersNight(Double metersNight) {
        this.metersNight = metersNight;
    }

    public Double getDefectRateDay() {
        return defectRateDay;
    }

    public void setDefectRateDay(Double defectRateDay) {
        this.defectRateDay = defectRateDay;
    }

    public Double getDefectRateNight() {
        return defectRateNight;
    }

    public void setDefectRateNight(Double defectRateNight) {
        this.defectRateNight = defectRateNight;
    }

}