package com.jeesite.modules.ems.entity;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotBlank;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 在线设备统计Entity
 * @author 范富华
 * @version 2024-05-29
 */
@Table(name="ems_equipment_online_summary", alias="a", label="在线设备统计信息", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="equipment_type", attrName="equipmentType", label="设备类型",queryType = QueryType.EQ),
		@Column(name="online_num", attrName="onlineNum", label="在线数", isUpdateForce=true),
		@Column(name="totalnum", attrName="totalnum", label="总数", isUpdateForce=true),
		@Column(name="company_code", attrName="companyCode", label="公司编码",queryType = QueryType.EQ),
	}, orderBy="a.id DESC"
)
@ApiModel(value = "EmsEquipmentOnlineSummary对象", description = "在线设备统计Entity")
public class EmsEquipmentOnlineSummary extends DataEntity<EmsEquipmentOnlineSummary> {
	
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "设备类型")
	private String equipmentType;
	@ApiModelProperty(value = "在线数")
	private Integer onlineNum;
	@ApiModelProperty(value = "总数")
	private Integer totalnum;
	@ApiModelProperty(value = "公司编码")
	private String companyCode;
	
	public EmsEquipmentOnlineSummary() {
		this(null);
	}
	
	public EmsEquipmentOnlineSummary(String id){
		super(id);
	}
	
	@Size(min=0, max=100, message="设备类型长度不能超过 100 个字符")
	public String getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(String equipmentType) {
		this.equipmentType = equipmentType;
	}

	public Integer getOnlineNum() {
		return onlineNum;
	}

	public void setOnlineNum(Integer onlineNum) {
		this.onlineNum = onlineNum;
	}

	public Integer getTotalnum() {
		return totalnum;
	}

	public void setTotalnum(Integer totalnum) {
		this.totalnum = totalnum;
	}

	@NotBlank(message="公司编码不能为空")
	@Size(min=0, max=64, message="公司编码长度不能超过 64 个字符")
	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	
}