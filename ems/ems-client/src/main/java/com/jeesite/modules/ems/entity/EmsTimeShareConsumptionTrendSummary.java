package com.jeesite.modules.ems.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Size;

/**
 * 尖峰平谷用电趋势汇总Entity
 * @author 范富华
 * @version 2024-05-17
 */
@Table(name="ems_time_share_consumption_trend_summary", alias="a", label="尖峰平谷用电趋势汇总信息", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", queryType=QueryType.LIKE),
		@Column(name="company_code", attrName="companyCode", label="公司编码"),
		@Column(name="company_name", attrName="companyName", label="公司名称", queryType=QueryType.LIKE),
		@Column(name="x_axis", attrName="xaxis", label="x轴数据"),
		@Column(name="y_axis", attrName="yaxis", label="y轴数据"),
		@Column(name="lable", attrName="lable", label="分类"),
	}, orderBy="a.update_date DESC"
)
@ApiModel(value = "EmsTimeShareConsumptionTrendSummary对象", description = "尖峰平谷用电趋势汇总Entity")
public class EmsTimeShareConsumptionTrendSummary extends DataEntity<EmsTimeShareConsumptionTrendSummary> {
	
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "公司编码")
	private String companyCode;
	@ApiModelProperty(value = "公司名称")
	private String companyName;
	@ApiModelProperty(value = "x轴数据")
	private String xaxis;
	@ApiModelProperty(value = "y轴数据")
	private String yaxis;
	@ApiModelProperty(value = "分类")
	private String lable;
	
	public EmsTimeShareConsumptionTrendSummary() {
		this(null);
	}
	
	public EmsTimeShareConsumptionTrendSummary(String id){
		super(id);
	}
	
	@Size(min=0, max=64, message="公司编码长度不能超过 64 个字符")
	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	
	@Size(min=0, max=100, message="公司名称长度不能超过 100 个字符")
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	@Size(min=0, max=2000, message="x轴数据长度不能超过 2000 个字符")
	public String getXaxis() {
		return xaxis;
	}

	public void setXaxis(String xaxis) {
		this.xaxis = xaxis;
	}
	
	@Size(min=0, max=2000, message="y轴数据长度不能超过 2000 个字符")
	public String getYaxis() {
		return yaxis;
	}

	public void setYaxis(String yaxis) {
		this.yaxis = yaxis;
	}
	
	@Size(min=0, max=50, message="分类长度不能超过 50 个字符")
	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}
	
}