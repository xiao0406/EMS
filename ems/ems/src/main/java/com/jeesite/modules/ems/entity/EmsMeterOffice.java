package com.jeesite.modules.ems.entity;

import javax.validation.constraints.NotNull;

import com.jeesite.common.entity.DataEntity;
import io.swagger.annotations.*;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 机构电表用电占比配置表Entity
 * @author 李鹏
 * @version 2023-06-14
 */
@Table(name="ems_meter_office", alias="a", label="机构电表用电占比配置表信息", columns={
		@Column(name="meter_code", attrName="meterCode", label="电表编码", isPK=true),
		@Column(name="office_code", attrName="officeCode", label="部门编码", isPK=true),
		@Column(name="power_ratio", attrName="powerRatio", label="用电占比"),
	}, orderBy="a.office_code DESC, a.meter_code DESC"
)
@ApiModel(value = "EmsMeterOffice对象", description = "机构电表用电占比配置表Entity")
public class EmsMeterOffice extends DataEntity<EmsMeterOffice> {
	
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "电表编码")
	private String meterCode;
	@ApiModelProperty(value = "部门编码")
	private String officeCode;
	@ApiModelProperty(value = "部门名称")
	private String officeName;
	@ApiModelProperty(value = "用电占比")
	private Double powerRatio;
	
	public EmsMeterOffice() {
		this(null, null);
	}
	
	public EmsMeterOffice(String officeCode, String meterCode){
		this.officeCode = officeCode;
		this.meterCode = meterCode;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}
	
	public String getMeterCode() {
		return meterCode;
	}

	public void setMeterCode(String meterCode) {
		this.meterCode = meterCode;
	}
	
	@NotNull(message="用电占比不能为空")
	public Double getPowerRatio() {
		return powerRatio;
	}

	public void setPowerRatio(Double powerRatio) {
		this.powerRatio = powerRatio;
	}
	
}