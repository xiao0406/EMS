package com.jeesite.modules.dcs.base.entity;

import javax.validation.constraints.Size;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import io.swagger.annotations.*;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 设备报警表Entity
 * @author ds
 * @version 2023-06-29
 */
@Table(name="dcs_device_alarm", alias="a", label="设备报警表信息", columns={
        @Column(name="id", attrName="id", label="id", isPK=true),
        @Column(name="device_code", attrName="deviceCode", label="设备代码"),
        @Column(name="device_name", attrName="deviceName", label="设备名称", queryType=QueryType.LIKE),
        @Column(name="alarm_code", attrName="alarmCode", label="报警代码"),
        @Column(name="start_time", attrName="startTime", label="开始时间", isUpdateForce=true),
        @Column(name="end_time", attrName="endTime", label="结束时间", isUpdateForce=true),
        @Column(name="elapsed_time", attrName="elapsedTime", label="持续时间"),
        @Column(name="remarks", attrName="remarks", label="备注信息", queryType=QueryType.LIKE),
        @Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
        @Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
        @Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
        @Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
}, orderBy="a.update_date DESC"
)
@ApiModel(value = "DeviceAlarm对象", description = "设备报警表Entity")
public class DeviceAlarm extends DataEntity<DeviceAlarm> {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "设备代码")
    private String deviceCode;
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    @ApiModelProperty(value = "报警代码")
    private String alarmCode;
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    @ApiModelProperty(value = "持续时间")
    private String elapsedTime;

    public DeviceAlarm() {
        this(null);
    }

    public DeviceAlarm(String id){
        super(id);
    }

    @Size(min=0, max=64, message="设备代码长度不能超过 64 个字符")
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

    @Size(min=0, max=64, message="报警代码长度不能超过 64 个字符")
    public String getAlarmCode() {
        return alarmCode;
    }

    public void setAlarmCode(String alarmCode) {
        this.alarmCode = alarmCode;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Size(min=0, max=100, message="持续时间长度不能超过 100 个字符")
    public String getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

}