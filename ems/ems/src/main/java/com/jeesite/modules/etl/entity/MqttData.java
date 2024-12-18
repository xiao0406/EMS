package com.jeesite.modules.etl.entity;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * dmp_mqtt_gd_2023Entity
 * @author 李鹏
 * @version 2023-06-06
 */
@Table(name="dmp_mqtt_protocol_data", alias="a", label="MQTT数据采集信息", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="topic", attrName="topic", label="Mqtt主题"),
		@Column(name="meter_code", attrName="deviceCode", label="设备编号"),
		@Column(name="terminal_code", attrName="terminalCode", label="终端编号"),
		@Column(name="payload", attrName="payload", label="有效载荷（数据报文）"),
		@Column(name="ts", attrName="ts", label="发送时间", isUpdateForce=true),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false, isUpdateForce=true),
	}, orderBy="a.ts"
)
@ApiModel(value = "MqttData对象", description = "MqttDataEntity")
@Data
public class MqttData extends DataEntity<MqttData> {
	
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "Mqtt主题")
	private String topic;
	@ApiModelProperty(value = "电表编号")
	private String deviceCode;
	@ApiModelProperty(value = "终端编号")
	private String terminalCode;
	@ApiModelProperty(value = "有效载荷（数据报文）")
	private String payload;
	@ApiModelProperty(value = "发送时间")
	private Date ts;

	private Date tsStart;
	private Date tsEnd;
	private List<String> deviceCodeList;
	
	public MqttData() {
		this(null);
	}
	
	public MqttData(String id){
		super(id);
	}
	
	@Size(min=0, max=50, message="终端编号长度不能超过 50 个字符")
	public String getTerminalCode() {
		return terminalCode;
	}

	public void setTerminalCode(String terminalCode) {
		this.terminalCode = terminalCode;
	}
	
	@Size(min=0, max=12, message="设备编号长度不能超过 12 个字符")
	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}
}