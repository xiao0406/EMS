package com.jeesite.modules.ems.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

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
import lombok.Data;

/**
 * 区域电耗表数据基础表Entity
 * @author 李鹏
 * @version 2023-07-06
 */
@Table(name="ems_electric_power_area_consumption", alias="a", label="区域电耗表数据基础表信息", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="area_code", attrName="areaCode", label="区域编码"),
		@Column(name="area_name", attrName="areaName", label="区域名称", queryType=QueryType.LIKE),
		@Column(name="data_date_time", attrName="dataDateTime", label="数据时间， 2023-03-24 22", comment="数据时间， 2023-03-24 22:00:12"),
		@Column(name="data_date", attrName="dataDate", label="数据日期， 2023-03-24"),
		@Column(name="data_time", attrName="dataTime", label="数据时间， 22", comment="数据时间， 22:00:12"),
		@Column(name="data_type", attrName="dataType", label="数据类型", comment="数据类型：15分钟、小时、日、月、年"),
		@Column(name="positive_active_energy", attrName="positiveActiveEnergy", label="正向有功电能", isUpdateForce=true),
		@Column(name="reverse_active_power", attrName="reverseActivePower", label="反向有功电能", isUpdateForce=true),
		@Column(name="positive_reactive_energy", attrName="positiveReactiveEnergy", label="正向无功电能", isUpdateForce=true),
		@Column(name="reverse_reactive_power", attrName="reverseReactivePower", label="反向无功电能", isUpdateForce=true),
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
@Data
@ApiModel(value = "EmsElectricPowerAreaConsumption对象", description = "区域电耗表数据基础表Entity")
public class EmsElectricPowerAreaConsumption extends DataEntity<EmsElectricPowerAreaConsumption> {
	
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "区域编码")
	private String areaCode;
	@ApiModelProperty(value = "区域名称")
	private String areaName;
	@ApiModelProperty(value = "数据时间， 2023-03-24 22:00:12")
	private Date dataDateTime;
	@ApiModelProperty(value = "数据日期， 2023-03-24")
	private Date dataDate;
	@ApiModelProperty(value = "数据时间， 22:00:12")
	private String dataTime;
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
	@ApiModelProperty(value = "公司编码")
	private String companyCode;
	@ApiModelProperty(value = "公司名称")
	private String companyName;
	@ApiModelProperty(value = "查询开始时间")
	private Date qryStartTime;
	@ApiModelProperty(value = "查询结束时间")
	private Date qryEndTime;

	public EmsElectricPowerAreaConsumption() {
		this(null);
	}
	
	public EmsElectricPowerAreaConsumption(String id){
		super(id);
	}
	
	@NotBlank(message="区域编码不能为空")
	@Size(min=0, max=32, message="区域编码长度不能超过 32 个字符")
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	@NotBlank(message="区域名称不能为空")
	@Size(min=0, max=100, message="区域名称长度不能超过 100 个字符")
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
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
	public String getDataTime() {
		return dataTime;
	}

	public void setDataTime(String dataTime) {
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
	
}