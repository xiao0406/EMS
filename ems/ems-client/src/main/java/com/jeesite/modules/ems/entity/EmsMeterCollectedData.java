package com.jeesite.modules.ems.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Time;
import java.util.Date;
import java.util.List;

import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;
import com.jeesite.common.entity.BaseEntity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 电表采集数据Entity
 * @author 李鹏
 * @version 2023-06-08
 */
@Table(name="ems_meter_collected_data", alias="a", label="电表采集数据信息", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="device_id", attrName="deviceId", label="设备ID"),
		@Column(name="device_name", attrName="deviceName", label="设备名称", queryType=QueryType.LIKE),
		@Column(name="data_date_time", attrName="dataDateTime", label="数据时间， 2023-03-24 22", comment="数据时间， 2023-03-24 22:00:12"),
		@Column(name="data_date", attrName="dataDate", label="数据日期， 2023-03-24"),
		@Column(name="data_time", attrName="dataTime", label="数据时间， 22", comment="数据时间， 22:00:12"),
		@Column(name="data_type", attrName="dataType", label="数据类型", comment="数据类型：15分钟、小时、日、月、年"),
		@Column(name="positive_active_energy", attrName="positiveActiveEnergy", label="正向有功电能", isUpdateForce=true),
		@Column(name="reverse_active_energy", attrName="reverseActiveEnergy", label="反向有功电能", isUpdateForce=true),
		@Column(name="positive_reactive_energy", attrName="positiveReactiveEnergy", label="正向无功电能", isUpdateForce=true),
		@Column(name="reverse_reactive_energy", attrName="reverseReactiveEnergy", label="反向无功电能", isUpdateForce=true),
		@Column(name="a_phase_v", attrName="aphaseV", label="A相电压", isUpdateForce=true),
		@Column(name="b_phase_v", attrName="bphaseV", label="B相电压", isUpdateForce=true),
		@Column(name="c_phase_v", attrName="cphaseV", label="C相电压", isUpdateForce=true),
		@Column(name="a_phase_a", attrName="aphaseA", label="A相电流", isUpdateForce=true),
		@Column(name="b_phase_a", attrName="bphaseA", label="B相电流", isUpdateForce=true),
		@Column(name="c_phase_a", attrName="cphaseA", label="C相电流", isUpdateForce=true),
		@Column(name="total_ap", attrName="totalAp", label="总有功功率", isUpdateForce=true),
		@Column(name="a_phase_ap", attrName="aphaseAp", label="A相有功功率", isUpdateForce=true),
		@Column(name="b_phase_ap", attrName="bphaseAp", label="B相有功功率", isUpdateForce=true),
		@Column(name="c_phase_ap", attrName="cphaseAp", label="C相有功功率", isUpdateForce=true),
		@Column(name="total_rp", attrName="totalRp", label="总无功功率", isUpdateForce=true),
		@Column(name="a_phase_rp", attrName="aphaseRp", label="A相无功功率", isUpdateForce=true),
		@Column(name="b_phase_rp", attrName="bphaseRp", label="B相无功功率", isUpdateForce=true),
		@Column(name="c_phase_rp", attrName="cphaseRp", label="C相无功功率", isUpdateForce=true),
		@Column(name="total_pf", attrName="totalPf", label="总功率因数", isUpdateForce=true),
		@Column(name="a_phase_pf", attrName="aphasePf", label="A相功率因数", isUpdateForce=true),
		@Column(name="b_phase_pf", attrName="bphasePf", label="B相功率因数", isUpdateForce=true),
		@Column(name="c_phase_pf", attrName="cphasePf", label="C相功率因数", isUpdateForce=true),
		@Column(name="mqtt_ts", attrName="mqttTs", label="设备上传时间", isUpdateForce=true),
		@Column(name="not_valid", attrName="notValid", label="状态", comment="状态（0：正常；1：数据丢失；）"),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", queryType=QueryType.LIKE),
		@Column(name="company_code", attrName="companyCode", label="公司编码"),
		@Column(name="company_name", attrName="companyName", label="公司名称", queryType=QueryType.LIKE),
		@Column(includeEntity=BaseEntity.class),
	}, orderBy="a.data_date_time"
)
@Builder
@AllArgsConstructor
@Data
public class EmsMeterCollectedData extends DataEntity<EmsMeterCollectedData> {
	
	private static final long serialVersionUID = 1L;
	private String deviceId;		// 设备ID
	private String deviceName;		// 设备名称
	private Date dataDateTime;		// 数据时间， 2023-03-24 22:00:12
	private Date dataDate;		// 数据日期， 2023-03-24
	private Time dataTime;		// 数据时间， 22:00:12
	private String dataType;		// 数据类型：15分钟、小时、日、月、年
	private Double positiveActiveEnergy;		// 正向有功电能
	private Double reverseActiveEnergy;		// 反向有功电能
	private Double positiveReactiveEnergy;		// 正向无功电能
	private Double reverseReactiveEnergy;		// 反向无功电能
	private Double aphaseV;		// A相电压
	private Double bphaseV;		// B相电压
	private Double cphaseV;		// C相电压
	private Double aphaseA;		// A相电流
	private Double bphaseA;		// B相电流
	private Double cphaseA;		// C相电流
	private Double totalAp;		// 总有功功率
	private Double aphaseAp;		// A相有功功率
	private Double bphaseAp;		// B相有功功率
	private Double cphaseAp;		// C相有功功率
	private Double totalRp;		// 总无功功率
	private Double aphaseRp;		// A相无功功率
	private Double bphaseRp;		// B相无功功率
	private Double cphaseRp;		// C相无功功率
	private Double totalPf;		// 总功率因数
	private Double aphasePf;		// A相功率因数
	private Double bphasePf;		// B相功率因数
	private Double cphasePf;		// C相功率因数
	private Date mqttTs;		// 设备上传时间
	private String notValid;		// 状态（0：正常；1：数据丢失；）
	@ApiModelProperty(value = "公司编码")
	private String companyCode;
	@ApiModelProperty(value = "公司名称")
	private String companyName;
	private Date dataDateTimeStart;
	private Date dataDateTimeEnd;
	private Double qt;//综合倍率

	public EmsMeterCollectedData() {
		this(null);
	}
	
	public EmsMeterCollectedData(String id){
		super(id);
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
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="数据时间， 2023-03-24 22不能为空")
	public Date getDataDateTime() {
		return dataDateTime;
	}

	public void setDataDateTime(Date dataDateTime) {
		this.dataDateTime = dataDateTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="数据日期， 2023-03-24不能为空")
	public Date getDataDate() {
		return dataDate;
	}

	public void setDataDate(Date dataDate) {
		this.dataDate = dataDate;
	}
	
	@NotBlank(message="数据时间， 22不能为空")
	public Time getDataTime() {
		return dataTime;
	}

	public void setDataTime(Time dataTime) {
		this.dataTime = dataTime;
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
	
	public Double getReverseActiveEnergy() {
		return reverseActiveEnergy;
	}

	public void setReverseActiveEnergy(Double reverseActiveEnergy) {
		this.reverseActiveEnergy = reverseActiveEnergy;
	}
	
	public Double getPositiveReactiveEnergy() {
		return positiveReactiveEnergy;
	}

	public void setPositiveReactiveEnergy(Double positiveReactiveEnergy) {
		this.positiveReactiveEnergy = positiveReactiveEnergy;
	}
	
	public Double getReverseReactiveEnergy() {
		return reverseReactiveEnergy;
	}

	public void setReverseReactiveEnergy(Double reverseReactiveEnergy) {
		this.reverseReactiveEnergy = reverseReactiveEnergy;
	}
	
	public Double getAphaseV() {
		return aphaseV;
	}

	public void setAphaseV(Double aphaseV) {
		this.aphaseV = aphaseV;
	}
	
	public Double getBphaseV() {
		return bphaseV;
	}

	public void setBphaseV(Double bphaseV) {
		this.bphaseV = bphaseV;
	}
	
	public Double getCphaseV() {
		return cphaseV;
	}

	public void setCphaseV(Double cphaseV) {
		this.cphaseV = cphaseV;
	}
	
	public Double getAphaseA() {
		return aphaseA;
	}

	public void setAphaseA(Double aphaseA) {
		this.aphaseA = aphaseA;
	}
	
	public Double getBphaseA() {
		return bphaseA;
	}

	public void setBphaseA(Double bphaseA) {
		this.bphaseA = bphaseA;
	}
	
	public Double getCphaseA() {
		return cphaseA;
	}

	public void setCphaseA(Double cphaseA) {
		this.cphaseA = cphaseA;
	}
	
	public Double getTotalAp() {
		return totalAp;
	}

	public void setTotalAp(Double totalAp) {
		this.totalAp = totalAp;
	}
	
	public Double getAphaseAp() {
		return aphaseAp;
	}

	public void setAphaseAp(Double aphaseAp) {
		this.aphaseAp = aphaseAp;
	}
	
	public Double getBphaseAp() {
		return bphaseAp;
	}

	public void setBphaseAp(Double bphaseAp) {
		this.bphaseAp = bphaseAp;
	}
	
	public Double getCphaseAp() {
		return cphaseAp;
	}

	public void setCphaseAp(Double cphaseAp) {
		this.cphaseAp = cphaseAp;
	}
	
	public Double getTotalRp() {
		return totalRp;
	}

	public void setTotalRp(Double totalRp) {
		this.totalRp = totalRp;
	}
	
	public Double getAphaseRp() {
		return aphaseRp;
	}

	public void setAphaseRp(Double aphaseRp) {
		this.aphaseRp = aphaseRp;
	}
	
	public Double getBphaseRp() {
		return bphaseRp;
	}

	public void setBphaseRp(Double bphaseRp) {
		this.bphaseRp = bphaseRp;
	}
	
	public Double getCphaseRp() {
		return cphaseRp;
	}

	public void setCphaseRp(Double cphaseRp) {
		this.cphaseRp = cphaseRp;
	}
	
	public Double getTotalPf() {
		return totalPf;
	}

	public void setTotalPf(Double totalPf) {
		this.totalPf = totalPf;
	}
	
	public Double getAphasePf() {
		return aphasePf;
	}

	public void setAphasePf(Double aphasePf) {
		this.aphasePf = aphasePf;
	}
	
	public Double getBphasePf() {
		return bphasePf;
	}

	public void setBphasePf(Double bphasePf) {
		this.bphasePf = bphasePf;
	}
	
	public Double getCphasePf() {
		return cphasePf;
	}

	public void setCphasePf(Double cphasePf) {
		this.cphasePf = cphasePf;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getMqttTs() {
		return mqttTs;
	}

	public void setMqttTs(Date mqttTs) {
		this.mqttTs = mqttTs;
	}
	
	@NotBlank(message="状态不能为空")
	@Size(min=0, max=1, message="状态长度不能超过 1 个字符")
	public String getNotValid() {
		return notValid;
	}

	public void setNotValid(String notValid) {
		this.notValid = notValid;
	}
	
}