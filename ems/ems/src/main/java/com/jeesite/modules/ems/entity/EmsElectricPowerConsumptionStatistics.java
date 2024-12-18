package com.jeesite.modules.ems.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.BaseEntity;

import com.jeesite.common.entity.DataEntity;
import io.swagger.annotations.*;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 电耗表数据统计表Entity
 * @author 李鹏
 * @version 2023-06-19
 */
@Table(name="ems_electric_power_consumption_statistics", alias="a", label="电耗表数据统计表信息", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="device_id", attrName="deviceId", label="设备ID"),
		@Column(name="device_name", attrName="deviceName", label="设备名称", queryType=QueryType.LIKE),
		@Column(name="data_date_key", attrName="dataDateKey", label="数据时间键值，字符串， 2023-03"),
		@Column(name="data_type", attrName="dataType", label="数据类型", comment="数据类型：15分钟、小时、日、月、年"),
		@Column(name="positive_active_energy", attrName="positiveActiveEnergy", label="正向有功电能", isUpdateForce=true),
		@Column(name="reverse_active_power", attrName="reverseActivePower", label="反向有功电能", isUpdateForce=true),
		@Column(name="positive_reactive_energy", attrName="positiveReactiveEnergy", label="正向无功电能", isUpdateForce=true),
		@Column(name="reverse_reactive_power", attrName="reverseReactivePower", label="反向无功电能", isUpdateForce=true),
		@Column(name="a_phase_voltage", attrName="aphaseVoltage", label="A相电压", isUpdateForce=true),
		@Column(name="b_phase_voltage", attrName="bphaseVoltage", label="B相电压", isUpdateForce=true),
		@Column(name="c_phase_voltage", attrName="cphaseVoltage", label="C相电压", isUpdateForce=true),
		@Column(name="voltage_unbalance_degree", attrName="voltageUnbalanceDegree", label="三相电压不平衡度", isUpdateForce=true),
		@Column(name="a_phase_current", attrName="aphaseCurrent", label="A相电流", isUpdateForce=true),
		@Column(name="b_phase_current", attrName="bphaseCurrent", label="B相电流", isUpdateForce=true),
		@Column(name="c_phase_current", attrName="cphaseCurrent", label="C相电流", isUpdateForce=true),
		@Column(name="current_unbalance_degree", attrName="currentUnbalanceDegree", label="三相电流不平衡度", isUpdateForce=true),
		@Column(name="total_active_power", attrName="totalActivePower", label="总有功功率", isUpdateForce=true),
		@Column(name="a_phase_active_power", attrName="aphaseActivePower", label="A相有功功率", isUpdateForce=true),
		@Column(name="b_phase_active_power", attrName="bphaseActivePower", label="B相有功功率", isUpdateForce=true),
		@Column(name="c_phase_active_power", attrName="cphaseActivePower", label="C相有功功率", isUpdateForce=true),
		@Column(name="total_reactive_power", attrName="totalReactivePower", label="总无功功率", isUpdateForce=true),
		@Column(name="a_phase_reactive_power", attrName="aphaseReactivePower", label="A相无功功率", isUpdateForce=true),
		@Column(name="b_phase_reactive_power", attrName="bphaseReactivePower", label="B相无功功率", isUpdateForce=true),
		@Column(name="c_phase_reactive_power", attrName="cphaseReactivePower", label="C相无功功率", isUpdateForce=true),
		@Column(name="total_power_factor", attrName="totalPowerFactor", label="总功率因数", isUpdateForce=true),
		@Column(name="a_phase_power_factor", attrName="aphasePowerFactor", label="A相功率因数", isUpdateForce=true),
		@Column(name="b_phase_power_factor", attrName="bphasePowerFactor", label="B相功率因数", isUpdateForce=true),
		@Column(name="c_phase_power_factor", attrName="cphasePowerFactor", label="C相功率因数", isUpdateForce=true),
		@Column(name="total_apparent_power", attrName="totalApparentPower", label="总视在功率", isUpdateForce=true),
		@Column(name="a_phase_apparent_power", attrName="aphaseApparentPower", label="A相视在功率", isUpdateForce=true),
		@Column(name="b_phase_apparent_power", attrName="bphaseApparentPower", label="B相视在功率", isUpdateForce=true),
		@Column(name="c_phase_apparent_power", attrName="cphaseApparentPower", label="C相视在功率", isUpdateForce=true),
		@Column(name="mqtt_ts", attrName="mqttTs", label="设备上传时间", isUpdateForce=true),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", queryType=QueryType.LIKE),
		@Column(name="company_code", attrName="companyCode", label="公司编码"),
		@Column(name="company_name", attrName="companyName", label="公司名称", queryType=QueryType.LIKE),
		@Column(includeEntity=BaseEntity.class),
	}, orderBy="a.data_date_key"
)
@Builder
@AllArgsConstructor
@Data
@ApiModel(value = "EmsElectricPowerConsumptionStatistics对象", description = "电耗表数据统计表Entity")
public class EmsElectricPowerConsumptionStatistics extends DataEntity<EmsElectricPowerConsumptionStatistics> {
	
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "设备ID")
	private String deviceId;
	@ApiModelProperty(value = "设备名称")
	private String deviceName;
	@ApiModelProperty(value = "数据时间键值，字符串， 2023-03")
	private String dataDateKey;
	@ApiModelProperty(value = "数据类型：15分钟、小时、日、月、年")
	private String dataType;
	@ApiModelProperty(value = "正向有功电能")
	private Double positiveActiveEnergy;
	@ApiModelProperty(value = "反向有功电能")
	private Double reverseActivePower;
	@ApiModelProperty(value = "正向无功电能")
	private Double positiveReactiveEnergy;
	@ApiModelProperty(value = "反向无功电能")
	private Double reverseReactivePower;
	@ApiModelProperty(value = "A相电压")
	private Double aphaseVoltage;
	@ApiModelProperty(value = "B相电压")
	private Double bphaseVoltage;
	@ApiModelProperty(value = "C相电压")
	private Double cphaseVoltage;
	@ApiModelProperty(value = "三相电压不平衡度")
	private Double voltageUnbalanceDegree;
	@ApiModelProperty(value = "A相电流")
	private Double aphaseCurrent;
	@ApiModelProperty(value = "B相电流")
	private Double bphaseCurrent;
	@ApiModelProperty(value = "C相电流")
	private Double cphaseCurrent;
	@ApiModelProperty(value = "三相电流不平衡度")
	private Double currentUnbalanceDegree;
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
	@ApiModelProperty(value = "总功率因数")
	private Double totalPowerFactor;
	@ApiModelProperty(value = "A相功率因数")
	private Double aphasePowerFactor;
	@ApiModelProperty(value = "B相功率因数")
	private Double bphasePowerFactor;
	@ApiModelProperty(value = "C相功率因数")
	private Double cphasePowerFactor;
	@ApiModelProperty(value = "总视在功率")
	private Double totalApparentPower;
	@ApiModelProperty(value = "A相视在功率")
	private Double aphaseApparentPower;
	@ApiModelProperty(value = "B相视在功率")
	private Double bphaseApparentPower;
	@ApiModelProperty(value = "C相视在功率")
	private Double cphaseApparentPower;
	@ApiModelProperty(value = "设备上传时间")
	private Date mqttTs;
	@ApiModelProperty(value = "公司编码")
	private String companyCode;
	@ApiModelProperty(value = "公司名称")
	private String companyName;
	@ApiModelProperty(value = "查询开始时间")
	private String qryStartTime;
	@ApiModelProperty(value = "查询结束时间")
	private String qryEndTime;
	@ApiModelProperty(value = "区域编码")
	private String areaCode;
	@ApiModelProperty(value = "区域计量列表")
	private List<String> areaMarkList;

	public EmsElectricPowerConsumptionStatistics() {
		this(null);
	}
	
	public EmsElectricPowerConsumptionStatistics(String id){
		super(id);
	}

	public String getQryStartTime() {
		return qryStartTime;
	}

	public void setQryStartTime(String qryStartTime) {
		this.qryStartTime = qryStartTime;
	}

	public String getQryEndTime() {
		return qryEndTime;
	}

	public void setQryEndTime(String qryEndTime) {
		this.qryEndTime = qryEndTime;
	}

	@NotBlank(message="设备ID不能为空")
	@Size(min=0, max=32, message="设备ID长度不能超过 32 个字符")
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	@NotBlank(message="设备名称不能为空")
	@Size(min=0, max=100, message="设备名称长度不能超过 100 个字符")
	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	@NotBlank(message="数据时间键值，字符串， 2023-03不能为空")
	@Size(min=0, max=20, message="数据时间键值，字符串， 2023-03长度不能超过 20 个字符")
	public String getDataDateKey() {
		return dataDateKey;
	}

	public void setDataDateKey(String dataDateKey) {
		this.dataDateKey = dataDateKey;
	}
	
	@NotBlank(message="数据类型不能为空")
	@Size(min=0, max=10, message="数据类型长度不能超过 10 个字符")
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public Double getPositiveActiveEnergy() {
		return positiveActiveEnergy;
	}

	public void setPositiveActiveEnergy(Double positiveActiveEnergy) {
		this.positiveActiveEnergy = positiveActiveEnergy;
	}
	
	public Double getReverseActivePower() {
		return reverseActivePower;
	}

	public void setReverseActivePower(Double reverseActivePower) {
		this.reverseActivePower = reverseActivePower;
	}
	
	public Double getPositiveReactiveEnergy() {
		return positiveReactiveEnergy;
	}

	public void setPositiveReactiveEnergy(Double positiveReactiveEnergy) {
		this.positiveReactiveEnergy = positiveReactiveEnergy;
	}
	
	public Double getReverseReactivePower() {
		return reverseReactivePower;
	}

	public void setReverseReactivePower(Double reverseReactivePower) {
		this.reverseReactivePower = reverseReactivePower;
	}
	
	public Double getAphaseVoltage() {
		return aphaseVoltage;
	}

	public void setAphaseVoltage(Double aphaseVoltage) {
		this.aphaseVoltage = aphaseVoltage;
	}
	
	public Double getBphaseVoltage() {
		return bphaseVoltage;
	}

	public void setBphaseVoltage(Double bphaseVoltage) {
		this.bphaseVoltage = bphaseVoltage;
	}
	
	public Double getCphaseVoltage() {
		return cphaseVoltage;
	}

	public void setCphaseVoltage(Double cphaseVoltage) {
		this.cphaseVoltage = cphaseVoltage;
	}
	
	public Double getVoltageUnbalanceDegree() {
		return voltageUnbalanceDegree;
	}

	public void setVoltageUnbalanceDegree(Double voltageUnbalanceDegree) {
		this.voltageUnbalanceDegree = voltageUnbalanceDegree;
	}
	
	public Double getAphaseCurrent() {
		return aphaseCurrent;
	}

	public void setAphaseCurrent(Double aphaseCurrent) {
		this.aphaseCurrent = aphaseCurrent;
	}
	
	public Double getBphaseCurrent() {
		return bphaseCurrent;
	}

	public void setBphaseCurrent(Double bphaseCurrent) {
		this.bphaseCurrent = bphaseCurrent;
	}
	
	public Double getCphaseCurrent() {
		return cphaseCurrent;
	}

	public void setCphaseCurrent(Double cphaseCurrent) {
		this.cphaseCurrent = cphaseCurrent;
	}
	
	public Double getCurrentUnbalanceDegree() {
		return currentUnbalanceDegree;
	}

	public void setCurrentUnbalanceDegree(Double currentUnbalanceDegree) {
		this.currentUnbalanceDegree = currentUnbalanceDegree;
	}
	
	public Double getTotalActivePower() {
		return totalActivePower;
	}

	public void setTotalActivePower(Double totalActivePower) {
		this.totalActivePower = totalActivePower;
	}
	
	public Double getAphaseActivePower() {
		return aphaseActivePower;
	}

	public void setAphaseActivePower(Double aphaseActivePower) {
		this.aphaseActivePower = aphaseActivePower;
	}
	
	public Double getBphaseActivePower() {
		return bphaseActivePower;
	}

	public void setBphaseActivePower(Double bphaseActivePower) {
		this.bphaseActivePower = bphaseActivePower;
	}
	
	public Double getCphaseActivePower() {
		return cphaseActivePower;
	}

	public void setCphaseActivePower(Double cphaseActivePower) {
		this.cphaseActivePower = cphaseActivePower;
	}
	
	public Double getTotalReactivePower() {
		return totalReactivePower;
	}

	public void setTotalReactivePower(Double totalReactivePower) {
		this.totalReactivePower = totalReactivePower;
	}
	
	public Double getAphaseReactivePower() {
		return aphaseReactivePower;
	}

	public void setAphaseReactivePower(Double aphaseReactivePower) {
		this.aphaseReactivePower = aphaseReactivePower;
	}
	
	public Double getBphaseReactivePower() {
		return bphaseReactivePower;
	}

	public void setBphaseReactivePower(Double bphaseReactivePower) {
		this.bphaseReactivePower = bphaseReactivePower;
	}
	
	public Double getCphaseReactivePower() {
		return cphaseReactivePower;
	}

	public void setCphaseReactivePower(Double cphaseReactivePower) {
		this.cphaseReactivePower = cphaseReactivePower;
	}
	
	public Double getTotalPowerFactor() {
		return totalPowerFactor;
	}

	public void setTotalPowerFactor(Double totalPowerFactor) {
		this.totalPowerFactor = totalPowerFactor;
	}
	
	public Double getAphasePowerFactor() {
		return aphasePowerFactor;
	}

	public void setAphasePowerFactor(Double aphasePowerFactor) {
		this.aphasePowerFactor = aphasePowerFactor;
	}
	
	public Double getBphasePowerFactor() {
		return bphasePowerFactor;
	}

	public void setBphasePowerFactor(Double bphasePowerFactor) {
		this.bphasePowerFactor = bphasePowerFactor;
	}
	
	public Double getCphasePowerFactor() {
		return cphasePowerFactor;
	}

	public void setCphasePowerFactor(Double cphasePowerFactor) {
		this.cphasePowerFactor = cphasePowerFactor;
	}
	
	public Double getTotalApparentPower() {
		return totalApparentPower;
	}

	public void setTotalApparentPower(Double totalApparentPower) {
		this.totalApparentPower = totalApparentPower;
	}
	
	public Double getAphaseApparentPower() {
		return aphaseApparentPower;
	}

	public void setAphaseApparentPower(Double aphaseApparentPower) {
		this.aphaseApparentPower = aphaseApparentPower;
	}
	
	public Double getBphaseApparentPower() {
		return bphaseApparentPower;
	}

	public void setBphaseApparentPower(Double bphaseApparentPower) {
		this.bphaseApparentPower = bphaseApparentPower;
	}
	
	public Double getCphaseApparentPower() {
		return cphaseApparentPower;
	}

	public void setCphaseApparentPower(Double cphaseApparentPower) {
		this.cphaseApparentPower = cphaseApparentPower;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getMqttTs() {
		return mqttTs;
	}

	public void setMqttTs(Date mqttTs) {
		this.mqttTs = mqttTs;
	}
}