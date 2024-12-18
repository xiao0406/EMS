package com.jeesite.modules.dcs.yh.entity;

import javax.validation.constraints.Size;
import java.time.LocalDate;

import com.jeesite.common.entity.DataEntity;
import io.swagger.annotations.*;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 部件焊接单元生产数据记录表Entity
 * @author ds
 * @version 2023-06-26
 */
@Table(name="dcs_yh_cw_prod", alias="a", label="部件焊接单元生产数据记录表信息", columns={
        @Column(name="id", attrName="id", label="主键", isPK=true),
        @Column(name="device_code", attrName="deviceCode", label="设备编号"),
        @Column(name="record_date", attrName="recordDate", label="记录日期"),
        @Column(name="device_name", attrName="deviceName", label="设备名称", queryType=QueryType.LIKE),
        @Column(name="wire_length", attrName="wireLength", label="焊丝使用长度", isUpdateForce=true),
        @Column(name="wire_weight", attrName="wireWeight", label="焊丝使用重量", isUpdateForce=true),
        @Column(name="day_run_time", attrName="dayRunTime", label="白班运行时间", isUpdateForce=true),
        @Column(name="night_run_time", attrName="nightRunTime", label="夜班运行时间", isUpdateForce=true),
        @Column(name="day_utilization_rate", attrName="dayUtilizationRate", label="白班稼动率", isUpdateForce=true),
        @Column(name="night_utilization_rate", attrName="nightUtilizationRate", label="夜班稼动率", isUpdateForce=true),
        @Column(name="day_capacity", attrName="dayCapacity", label="白班焊接数量", isUpdateForce=true),
        @Column(name="night_capacity", attrName="nightCapacity", label="夜班焊接数量", isUpdateForce=true),
        @Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
        @Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
        @Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
        @Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
}, orderBy="a.update_date DESC"
)
@ApiModel(value = "YhCwProd对象", description = "部件焊接单元生产数据记录表Entity")
public class YhCwProd extends DataEntity<YhCwProd> {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "设备编号")
    private String id;
    @ApiModelProperty(value = "设备编号")
    private String deviceCode;
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    @ApiModelProperty(value = "焊丝使用长度")
    private Double wireLength;
    @ApiModelProperty(value = "焊丝使用重量")
    private Double wireWeight;
    @ApiModelProperty(value = "白班运行时间")
    private Double dayRunTime;
    @ApiModelProperty(value = "夜班运行时间")
    private Double nightRunTime;
    @ApiModelProperty(value = "白班稼动率")
    private Double dayUtilizationRate;
    @ApiModelProperty(value = "夜班稼动率")
    private Double nightUtilizationRate;
    @ApiModelProperty(value = "白班焊接数量")
    private Double dayCapacity;
    @ApiModelProperty(value = "夜班焊接数量")
    private Double nightCapacity;
    @ApiModelProperty(value = "记录日期")
    private LocalDate recordDate;

    public YhCwProd() {
        this(null);
    }

    public YhCwProd(String id){
        super(id);
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    @Size(min=0, max=100, message="设备名称长度不能超过 100 个字符")
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Double getWireLength() {
        return wireLength;
    }

    public void setWireLength(Double wireLength) {
        this.wireLength = wireLength;
    }

    public Double getWireWeight() {
        return wireWeight;
    }

    public void setWireWeight(Double wireWeight) {
        this.wireWeight = wireWeight;
    }

    public Double getDayRunTime() {
        return dayRunTime;
    }

    public void setDayRunTime(Double dayRunTime) {
        this.dayRunTime = dayRunTime;
    }

    public Double getNightRunTime() {
        return nightRunTime;
    }

    public void setNightRunTime(Double nightRunTime) {
        this.nightRunTime = nightRunTime;
    }

    public Double getDayUtilizationRate() {
        return dayUtilizationRate;
    }

    public void setDayUtilizationRate(Double dayUtilizationRate) {
        this.dayUtilizationRate = dayUtilizationRate;
    }

    public Double getNightUtilizationRate() {
        return nightUtilizationRate;
    }

    public void setNightUtilizationRate(Double nightUtilizationRate) {
        this.nightUtilizationRate = nightUtilizationRate;
    }

    public Double getDayCapacity() {
        return dayCapacity;
    }

    public void setDayCapacity(Double dayCapacity) {
        this.dayCapacity = dayCapacity;
    }

    public Double getNightCapacity() {
        return nightCapacity;
    }

    public void setNightCapacity(Double nightCapacity) {
        this.nightCapacity = nightCapacity;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

}