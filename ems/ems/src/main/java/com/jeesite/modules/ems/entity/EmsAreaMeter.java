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
 * 区域电表绑定Entity
 * @author 李鹏
 * @version 2023-06-13
 */
@Table(name="ems_area_meter", alias="a", label="区域电表绑定信息", columns={
		@Column(name="area_code", attrName="areaCode", label="区域编码", isPK=true),
		@Column(name="meter_code", attrName="meterCode", label="电表编码", isPK=true),
		@Column(name="electricity_mark", attrName="electricityMark", label="计量标识;在区域能耗统计时，用于判断是否统计"),
	}
)
@ApiModel(value = "EmsAreaMeter对象", description = "区域电表绑定Entity")
public class EmsAreaMeter extends DataEntity<EmsAreaMeter> {
	
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "区域编码")
	private String areaCode;
	@ApiModelProperty(value = "电表编码")
	private String meterCode;
	@ApiModelProperty(value = "计量标识;在区域能耗统计时，用于判断是否统计")
	private String electricityMark;
	
	public EmsAreaMeter() {
		this(null, null);
	}
	
	public EmsAreaMeter(String areaCode, String meterCode){
		this.areaCode = areaCode;
		this.meterCode = meterCode;
	}
	
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	public String getMeterCode() {
		return meterCode;
	}

	public void setMeterCode(String meterCode) {
		this.meterCode = meterCode;
	}
	
	@NotBlank(message="计量标识;在区域能耗统计时，用于判断是否统计不能为空")
	@Size(min=0, max=1, message="计量标识;在区域能耗统计时，用于判断是否统计长度不能超过 1 个字符")
	public String getElectricityMark() {
		return electricityMark;
	}

	public void setElectricityMark(String electricityMark) {
		this.electricityMark = electricityMark;
	}
	
}