package com.jeesite.modules.ems.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import com.jeesite.common.entity.BaseEntity;

import com.jeesite.common.entity.DataEntity;
import io.swagger.annotations.*;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 峰平谷电耗表数据统计表Entity
 * @author 李鹏
 * @version 2023-06-20
 */
@Table(name="ems_time_share_power_consumption_statistics", alias="a", label="峰平谷电耗表数据统计表信息", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="device_id", attrName="deviceId", label="设备ID"),
		@Column(name="device_name", attrName="deviceName", label="设备名称", queryType=QueryType.LIKE),
		@Column(name="data_date_key", attrName="dataDateKey", label="数据时间键值，字符串， 2023-03"),
		@Column(name="data_type", attrName="dataType", label="数据类型", comment="数据类型：15分钟、小时、日、月、年"),
		@Column(name="total_energy", attrName="totalEnergy", label="总", isUpdateForce=true),
		@Column(name="cusp_time_energy", attrName="cuspTimeEnergy", label="尖", isUpdateForce=true),
		@Column(name="peak_time_energy", attrName="peakTimeEnergy", label="峰", isUpdateForce=true),
		@Column(name="fair_time_energy", attrName="fairTimeEnergy", label="平", isUpdateForce=true),
		@Column(name="valley_time_energy", attrName="valleyTimeEnergy", label="谷", isUpdateForce=true),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", queryType=QueryType.LIKE),
		@Column(name = "company_code", attrName = "companyCode", label = "公司编码"),
		@Column(name = "company_name", attrName = "companyName", label = "公司名称", queryType = QueryType.LIKE),
		@Column(includeEntity=BaseEntity.class),
	}, orderBy="a.data_date_key"
)
@Builder
@AllArgsConstructor
@Data
@ApiModel(value = "EmsTimeSharePowerConsumptionStatistics对象", description = "峰平谷电耗表数据统计表Entity")
public class EmsTimeSharePowerConsumptionStatistics extends DataEntity<EmsTimeSharePowerConsumptionStatistics> {
	
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "设备ID")
	private String deviceId;
	@ApiModelProperty(value = "设备名称")
	private String deviceName;
	@ApiModelProperty(value = "数据时间键值，字符串， 2023-03")
	private String dataDateKey;
	@ApiModelProperty(value = "数据类型：15分钟、小时、日、月、年")
	private String dataType;
	@ApiModelProperty(value = "总")
	private Double totalEnergy;
	@ApiModelProperty(value = "尖")
	private Double cuspTimeEnergy;
	@ApiModelProperty(value = "峰")
	private Double peakTimeEnergy;
	@ApiModelProperty(value = "平")
	private Double fairTimeEnergy;
	@ApiModelProperty(value = "谷")
	private Double valleyTimeEnergy;
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
	private List<String> meterMarkList;

	public EmsTimeSharePowerConsumptionStatistics() {
		this(null);
	}
	
	public EmsTimeSharePowerConsumptionStatistics(String id){
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
	
	public Double getTotalEnergy() {
		return totalEnergy;
	}

	public void setTotalEnergy(Double totalEnergy) {
		this.totalEnergy = totalEnergy;
	}
	
	public Double getCuspTimeEnergy() {
		return cuspTimeEnergy;
	}

	public void setCuspTimeEnergy(Double cuspTimeEnergy) {
		this.cuspTimeEnergy = cuspTimeEnergy;
	}
	
	public Double getPeakTimeEnergy() {
		return peakTimeEnergy;
	}

	public void setPeakTimeEnergy(Double peakTimeEnergy) {
		this.peakTimeEnergy = peakTimeEnergy;
	}
	
	public Double getFairTimeEnergy() {
		return fairTimeEnergy;
	}

	public void setFairTimeEnergy(Double fairTimeEnergy) {
		this.fairTimeEnergy = fairTimeEnergy;
	}
	
	public Double getValleyTimeEnergy() {
		return valleyTimeEnergy;
	}

	public void setValleyTimeEnergy(Double valleyTimeEnergy) {
		this.valleyTimeEnergy = valleyTimeEnergy;
	}
	
}