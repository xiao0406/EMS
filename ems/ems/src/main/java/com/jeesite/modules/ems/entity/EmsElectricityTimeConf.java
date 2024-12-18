package com.jeesite.modules.ems.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import com.jeesite.common.entity.BaseEntity;

import com.jeesite.common.entity.DataEntity;
import io.swagger.annotations.*;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 用电分时配置表Entity
 * @author 李鹏
 * @version 2023-06-15
 */
@Table(name="ems_electricity_time_conf", alias="a", label="用电分时配置表信息", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="cusp_time_range", attrName="cuspTimeRange", label="尖"),
		@Column(name="peak_time_range", attrName="peakTimeRange", label="峰"),
		@Column(name="fair_time_range", attrName="fairTimeRange", label="平"),
		@Column(name="valley_time_range", attrName="valleyTimeRange", label="谷"),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", queryType=QueryType.LIKE),
		@Column(name="company_code", attrName="companyCode", label="公司编码"),
		@Column(name="company_name", attrName="companyName", label="公司名称", queryType=QueryType.LIKE),
		@Column(includeEntity=BaseEntity.class),
	}, orderBy="a.update_date DESC"
)
@ApiModel(value = "EmsElectricityTimeConf对象", description = "用电分时配置表Entity")
public class EmsElectricityTimeConf extends DataEntity<EmsElectricityTimeConf> {
	
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "尖")
	private String cuspTimeRange;
	@ApiModelProperty(value = "峰")
	private String peakTimeRange;
	@ApiModelProperty(value = "平")
	private String fairTimeRange;
	@ApiModelProperty(value = "谷")
	private String valleyTimeRange;
	@ApiModelProperty(value = "公司编码")
	private String companyCode;
	@ApiModelProperty(value = "公司名称")
	private String companyName;

	public EmsElectricityTimeConf() {
		this(null);
	}
	
	public EmsElectricityTimeConf(String id){
		super(id);
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
	
	@Size(min=0, max=255, message="尖长度不能超过 255 个字符")
	public String getCuspTimeRange() {
		return cuspTimeRange;
	}

	public void setCuspTimeRange(String cuspTimeRange) {
		this.cuspTimeRange = cuspTimeRange;
	}
	
	@Size(min=0, max=255, message="峰长度不能超过 255 个字符")
	public String getPeakTimeRange() {
		return peakTimeRange;
	}

	public void setPeakTimeRange(String peakTimeRange) {
		this.peakTimeRange = peakTimeRange;
	}
	
	@Size(min=0, max=255, message="平长度不能超过 255 个字符")
	public String getFairTimeRange() {
		return fairTimeRange;
	}

	public void setFairTimeRange(String fairTimeRange) {
		this.fairTimeRange = fairTimeRange;
	}
	
	@Size(min=0, max=255, message="谷长度不能超过 255 个字符")
	public String getValleyTimeRange() {
		return valleyTimeRange;
	}

	public void setValleyTimeRange(String valleyTimeRange) {
		this.valleyTimeRange = valleyTimeRange;
	}
	
}