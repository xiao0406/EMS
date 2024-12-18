package com.jeesite.modules.ems.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Size;

/**
 * 耗电量排行关联表Entity
 * @author 范富华
 * @version 2024-05-15
 */
@Table(name="ems_ranking_summary_info", alias="a", label="耗电量排行关联表信息", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="ranking_summary_id", attrName="rankingSummaryId", label="排行id",queryType = QueryType.EQ),
		@Column(name="label", attrName="label", label="设备名称"),
		@Column(name="value", attrName="value", label="耗电量"),
		@Column(name="sort", attrName="sort", label="排序"),
	}, orderBy="a.id ASC"
)
@ApiModel(value = "EmsRankingSummaryInfo对象", description = "耗电量排行关联表Entity")
public class EmsRankingSummaryInfo extends DataEntity<EmsRankingSummaryInfo> {
	
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "排行id")
	private String rankingSummaryId;
	@ApiModelProperty(value = "设备名称")
	private String label;
	@ApiModelProperty(value = "耗电量")
	private String value;

	@ApiModelProperty(value = "排序")
	private String sort;

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public EmsRankingSummaryInfo() {
		this(null);
	}
	
	public EmsRankingSummaryInfo(String id){
		super(id);
	}
	
	@Size(min=0, max=32, message="排行id长度不能超过 32 个字符")
	public String getRankingSummaryId() {
		return rankingSummaryId;
	}

	public void setRankingSummaryId(String rankingSummaryId) {
		this.rankingSummaryId = rankingSummaryId;
	}
	
	@Size(min=0, max=200, message="设备名称长度不能超过 200 个字符")
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	@Size(min=0, max=200, message="耗电量长度不能超过 200 个字符")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}