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
import lombok.Data;

/**
 * 区域峰平谷电耗表数据基础表Entity
 * @author 李鹏
 * @version 2023-07-25
 */
@Table(name="ems_time_share_area_power_consumption", alias="a", label="区域峰平谷电耗表数据基础表信息", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="area_code", attrName="areaCode", label="区域编码"),
		@Column(name="area_name", attrName="areaName", label="区域名称", queryType=QueryType.LIKE),
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
		@Column(name="company_code", attrName="companyCode", label="公司编码"),
		@Column(name="company_name", attrName="companyName", label="公司名称", queryType=QueryType.LIKE),
		@Column(includeEntity=BaseEntity.class),
	}, orderBy="a.data_date DESC"
)
@Data
@ApiModel(value = "EmsTimeShareAreaPowerConsumption对象", description = "区域峰平谷电耗表数据基础表Entity")
public class EmsTimeShareAreaPowerConsumption extends DataEntity<EmsTimeShareAreaPowerConsumption> {
	
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "区域编码")
	private String areaCode;
	@ApiModelProperty(value = "区域名称")
	private String areaName;
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

	public EmsTimeShareAreaPowerConsumption() {
		this(null);
	}
	
	public EmsTimeShareAreaPowerConsumption(String id){
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
	
	@NotBlank(message="公司编码不能为空")
	@Size(min=0, max=100, message="公司编码长度不能超过 100 个字符")
	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	
	@NotBlank(message="公司名称不能为空")
	@Size(min=0, max=100, message="公司名称长度不能超过 100 个字符")
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
}