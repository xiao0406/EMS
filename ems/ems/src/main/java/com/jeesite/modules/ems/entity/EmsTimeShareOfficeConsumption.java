package com.jeesite.modules.ems.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;
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
 * 峰平谷部门电耗表数据基础表Entity
 * @author 李鹏
 * @version 2023-06-28
 */
@Table(name="ems_time_share_office_consumption", alias="a", label="峰平谷部门电耗表数据基础表信息", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="office_code", attrName="officeCode", label="机构编码"),
		@Column(name="office_name", attrName="officeName", label="机构名称", queryType=QueryType.LIKE),
		@Column(name="device_id", attrName="deviceId", label="设备ID"),
		@Column(name="device_name", attrName="deviceName", label="设备名称", queryType=QueryType.LIKE),
		@Column(name="data_date", attrName="dataDate", label="数据日期， 2023-03-24"),
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
	}, orderBy="a.update_date DESC"
)
@Builder
@AllArgsConstructor
@Data
@ApiModel(value = "EmsTimeShareOfficeConsumption对象", description = "峰平谷部门电耗表数据基础表Entity")
public class EmsTimeShareOfficeConsumption extends DataEntity<EmsTimeShareOfficeConsumption> {
	
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "机构编码")
	private String officeCode;
	@ApiModelProperty(value = "机构名称")
	private String officeName;
	@ApiModelProperty(value = "设备ID")
	private String deviceId;
	@ApiModelProperty(value = "设备名称")
	private String deviceName;
	@ApiModelProperty(value = "数据日期， 2023-03-24")
	private Date dataDate;
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

	private Date dataDateStart;
	private Date dataDateEnd;

	public EmsTimeShareOfficeConsumption() {
		this(null);
	}
	
	public EmsTimeShareOfficeConsumption(String id){
		super(id);
	}

	public Date getDataDateStart() {
		return dataDateStart;
	}

	public void setDataDateStart(Date dataDateStart) {
		this.dataDateStart = dataDateStart;
	}

	public Date getDataDateEnd() {
		return dataDateEnd;
	}

	public void setDataDateEnd(Date dataDateEnd) {
		this.dataDateEnd = dataDateEnd;
	}

	@NotBlank(message="机构编码不能为空")
	@Size(min=0, max=32, message="机构编码长度不能超过 32 个字符")
	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}
	
	@NotBlank(message="机构名称不能为空")
	@Size(min=0, max=100, message="机构名称长度不能超过 100 个字符")
	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
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
	@NotNull(message="数据日期， 2023-03-24不能为空")
	public Date getDataDate() {
		return dataDate;
	}

	public void setDataDate(Date dataDate) {
		this.dataDate = dataDate;
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