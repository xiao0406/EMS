package com.jeesite.modules.ems.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用电量排行Entity
 * @author 范富华
 * @version 2024-05-14
 */
@Table(name="ems_consumption_ranking_summary", alias="a", label="用电量排行信息", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", queryType=QueryType.LIKE),
		@Column(name="company_code", attrName="companyCode", label="公司编码"),
		@Column(name="company_name", attrName="companyName", label="公司名称", queryType=QueryType.LIKE),
		@Column(name="temporalgranularity", attrName="temporalgranularity", label="报表查询维度"),
		@Column(name="ranking_information", attrName="rankingInformation", label="排名信息"),
	}, orderBy="a.update_date DESC"
)
@ApiModel(value = "EmsConsumptionRankingSummary对象", description = "用电量排行Entity")
public class EmsConsumptionRankingSummary extends DataEntity<EmsConsumptionRankingSummary> {
	
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "公司编码")
	private String companyCode;
	@ApiModelProperty(value = "公司名称")
	private String companyName;
	@ApiModelProperty(value = "报表查询维度")
	private String temporalgranularity;
	@ApiModelProperty(value = "排名信息")
	private String rankingInformation;
	
	public EmsConsumptionRankingSummary() {
		this(null);
	}
	
	public EmsConsumptionRankingSummary(String id){
		super(id);
	}
	
	@NotBlank(message="公司编码不能为空")
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
	
	@Size(min=0, max=50, message="报表查询维度长度不能超过 50 个字符")
	public String getTemporalgranularity() {
		return temporalgranularity;
	}

	public void setTemporalgranularity(String temporalgranularity) {
		this.temporalgranularity = temporalgranularity;
	}
	
	public String getRankingInformation() {
		return rankingInformation;
	}

	public void setRankingInformation(String rankingInformation) {
		this.rankingInformation = rankingInformation;
	}
	
}