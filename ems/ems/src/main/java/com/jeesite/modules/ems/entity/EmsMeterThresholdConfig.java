package com.jeesite.modules.ems.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import com.jeesite.common.entity.BaseEntity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import com.jeesite.modules.ems.entity.enums.ThresholdConfigEnum;
import io.swagger.annotations.*;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import lombok.Data;

/**
 * 电表阈值配置表Entity
 * @author 李鹏
 * @version 2023-07-22
 */
@Table(name="ems_meter_threshold_config", alias="a", label="电表阈值配置表信息", columns={
		@Column(name="meter_code", attrName="meterCode", label="电表编号", isPK=true),
		@Column(name="meter_name", attrName="meterName", label="电表名称", queryType=QueryType.LIKE),
		@Column(name="rated_power", attrName="ratedPower", label="额定功率" ),
		@Column(name="upper_warn_limit_power", attrName="upperWarnLimitPower", label="预警上限" ),
		@Column(name="lower_warn_limit_power", attrName="lowerWarnLimitPower", label="预警下限" ),
		@Column(name="rated_current", attrName="ratedCurrent", label="额定电流" ),
		@Column(name="upper_warn_limit_current", attrName="upperWarnLimitCurrent", label="预警上限" ),
		@Column(name="lower_warn_limit_current", attrName="lowerWarnLimitCurrent", label="预警下限" ),
		@Column(name="rated_voltage", attrName="ratedVoltage", label="额定电压" ),
		@Column(name="upper_warn_limit_voltage", attrName="upperWarnLimitVoltage", label="预警上限" ),
		@Column(name="lower_warn_limit_voltage", attrName="lowerWarnLimitVoltage", label="预警下限" ),
		@Column(name="rated_power_factor", attrName="ratedPowerFactor", label="参见功率因数" ),
		@Column(name="upper_warn_limit_power_factor", attrName="upperWarnLimitPowerFactor", label="预警上限" ),
		@Column(name="lower_warn_limit_power_factor", attrName="lowerWarnLimitPowerFactor", label="预警下限" ),
		@Column(name="calculate_frequency", attrName="calculateFrequency", label="统计频率"),
		@Column(name="is_execute_weekend", attrName="isExecuteWeekend", label="统计周期--是否执行周六日"),
		@Column(name="upper_warn_limit_energy", attrName="upperWarnLimitEnergy", label="预警上限" ),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", queryType=QueryType.LIKE),
		@Column(name="company_code", attrName="companyCode", label="公司编码"),
		@Column(name="company_name", attrName="companyName", label="公司名称", queryType=QueryType.LIKE),
		@Column(includeEntity=BaseEntity.class),
	}
)
@ApiModel(value = "EmsMeterThresholdConfig对象", description = "电表阈值配置表Entity")
@Data
public class EmsMeterThresholdConfig extends DataEntity<EmsMeterThresholdConfig> {
	
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "电表编号")
	private String meterCode;
	@ApiModelProperty(value = "电表名称")
	private String meterName;
	@ApiModelProperty(value = "额定功率")
	private Double ratedPower;
	@ApiModelProperty(value = "预警上限")
	private Double upperWarnLimitPower;
	@ApiModelProperty(value = "预警下限")
	private Double lowerWarnLimitPower;
	@ApiModelProperty(value = "额定电流")
	private Double ratedCurrent;
	@ApiModelProperty(value = "预警上限")
	private Double upperWarnLimitCurrent;
	@ApiModelProperty(value = "预警下限")
	private Double lowerWarnLimitCurrent;
	@ApiModelProperty(value = "额定电压")
	private Double ratedVoltage;
	@ApiModelProperty(value = "预警上限")
	private Double upperWarnLimitVoltage;
	@ApiModelProperty(value = "预警下限")
	private Double lowerWarnLimitVoltage;
	@ApiModelProperty(value = "参见功率因数")
	private Double ratedPowerFactor;
	@ApiModelProperty(value = "预警上限")
	private Double upperWarnLimitPowerFactor;
	@ApiModelProperty(value = "预警下限")
	private Double lowerWarnLimitPowerFactor;
	@ApiModelProperty(value = "统计频率")
	private String calculateFrequency;
	@ApiModelProperty(value = "统计周期--是否执行周六日")
	private String isExecuteWeekend;
	@ApiModelProperty(value = "预警上限")
	private Double upperWarnLimitEnergy;
	@ApiModelProperty(value = "公司编码")
	private String companyCode;
	@ApiModelProperty(value = "公司名称")
	private String companyName;

	@ExcelFields({
			@ExcelField(title="电表名称", attrName = "meterName", align = ExcelField.Align.CENTER, sort = 10),
			@ExcelField(title="额定功率", attrName="ratedPower", align = ExcelField.Align.CENTER, sort=20),
			@ExcelField(title="功率预警上限", attrName="upperWarnLimitPower", align = ExcelField.Align.CENTER, sort=30),
			@ExcelField(title="功率预警下限", attrName="lowerWarnLimitPower", align = ExcelField.Align.CENTER, sort=40),
			@ExcelField(title="额定电流", attrName="ratedCurrent", align = ExcelField.Align.CENTER, sort=50),
			@ExcelField(title="电流预警上限", attrName="upperWarnLimitCurrent", align = ExcelField.Align.CENTER, sort=60),
			@ExcelField(title="电流预警下限", attrName="lowerWarnLimitCurrent", align = ExcelField.Align.CENTER, sort=70),
			@ExcelField(title="额定电压", attrName="ratedVoltage", align = ExcelField.Align.CENTER, sort=80),
			@ExcelField(title="电压预警上限", attrName="upperWarnLimitVoltage", align = ExcelField.Align.CENTER, sort=90),
			@ExcelField(title="电压预警下限", attrName="lowerWarnLimitVoltage", align = ExcelField.Align.CENTER, sort=100),
			@ExcelField(title="参见功率因数", attrName="ratedPowerFactor", align = ExcelField.Align.CENTER, sort=110),
			@ExcelField(title="功率因数预警上限", attrName="upperWarnLimitPowerFactor", align = ExcelField.Align.CENTER, sort=120),
			@ExcelField(title="功率因数预警下限", attrName="lowerWarnLimitPowerFactor", align = ExcelField.Align.CENTER, sort=130),
			@ExcelField(title="电量统计频率", attrName="calculateFrequency", align = ExcelField.Align.CENTER, sort=140),
			@ExcelField(title="电量统计周期（是否执行周六日）", attrName="isExecuteWeekend", align = ExcelField.Align.CENTER, sort=150, dictType="sys_yes_no"),
			@ExcelField(title="电量预警上限", attrName="upperWarnLimitEnergy", align = ExcelField.Align.CENTER, sort=160),
	})
	public EmsMeterThresholdConfig() {
		this(null);
	}
	
	public EmsMeterThresholdConfig(String id){
		super(id);
	}
	
	public String getMeterCode() {
		return meterCode;
	}

	public void setMeterCode(String meterCode) {
		this.meterCode = meterCode;
	}
	
	@NotBlank(message="电表名称不能为空")
	@Size(min=0, max=100, message="电表名称长度不能超过 100 个字符")
	public String getMeterName() {
		return meterName;
	}

	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}
	
	public Double getRatedPower() {
		return ratedPower;
	}

	public void setRatedPower(Double ratedPower) {
		this.ratedPower = ratedPower;
	}
	
	public Double getUpperWarnLimitPower() {
		return upperWarnLimitPower;
	}

	public void setUpperWarnLimitPower(Double upperWarnLimitPower) {
		this.upperWarnLimitPower = upperWarnLimitPower;
	}
	
	public Double getLowerWarnLimitPower() {
		return lowerWarnLimitPower;
	}

	public void setLowerWarnLimitPower(Double lowerWarnLimitPower) {
		this.lowerWarnLimitPower = lowerWarnLimitPower;
	}
	
	public Double getRatedCurrent() {
		return ratedCurrent;
	}

	public void setRatedCurrent(Double ratedCurrent) {
		this.ratedCurrent = ratedCurrent;
	}
	
	public Double getUpperWarnLimitCurrent() {
		return upperWarnLimitCurrent;
	}

	public void setUpperWarnLimitCurrent(Double upperWarnLimitCurrent) {
		this.upperWarnLimitCurrent = upperWarnLimitCurrent;
	}
	
	public Double getLowerWarnLimitCurrent() {
		return lowerWarnLimitCurrent;
	}

	public void setLowerWarnLimitCurrent(Double lowerWarnLimitCurrent) {
		this.lowerWarnLimitCurrent = lowerWarnLimitCurrent;
	}
	
	public Double getRatedVoltage() {
		return ratedVoltage;
	}

	public void setRatedVoltage(Double ratedVoltage) {
		this.ratedVoltage = ratedVoltage;
	}
	
	public Double getUpperWarnLimitVoltage() {
		return upperWarnLimitVoltage;
	}

	public void setUpperWarnLimitVoltage(Double upperWarnLimitVoltage) {
		this.upperWarnLimitVoltage = upperWarnLimitVoltage;
	}
	
	public Double getLowerWarnLimitVoltage() {
		return lowerWarnLimitVoltage;
	}

	public void setLowerWarnLimitVoltage(Double lowerWarnLimitVoltage) {
		this.lowerWarnLimitVoltage = lowerWarnLimitVoltage;
	}
	
	public Double getRatedPowerFactor() {
		return ratedPowerFactor;
	}

	public void setRatedPowerFactor(Double ratedPowerFactor) {
		this.ratedPowerFactor = ratedPowerFactor;
	}
	
	public Double getUpperWarnLimitPowerFactor() {
		return upperWarnLimitPowerFactor;
	}

	public void setUpperWarnLimitPowerFactor(Double upperWarnLimitPowerFactor) {
		this.upperWarnLimitPowerFactor = upperWarnLimitPowerFactor;
	}
	
	public Double getLowerWarnLimitPowerFactor() {
		return lowerWarnLimitPowerFactor;
	}

	public void setLowerWarnLimitPowerFactor(Double lowerWarnLimitPowerFactor) {
		this.lowerWarnLimitPowerFactor = lowerWarnLimitPowerFactor;
	}
	
	@Size(min=0, max=20, message="统计频率长度不能超过 20 个字符")
	public String getCalculateFrequency() {
		return calculateFrequency;
	}

	public void setCalculateFrequency(String calculateFrequency) {
		this.calculateFrequency = calculateFrequency;
	}
	
	@Size(min=0, max=1, message="统计周期--是否执行周六日长度不能超过 1 个字符")
	public String getIsExecuteWeekend() {
		return isExecuteWeekend;
	}

	public void setIsExecuteWeekend(String isExecuteWeekend) {
		this.isExecuteWeekend = isExecuteWeekend;
	}
	
	public Double getUpperWarnLimitEnergy() {
		return upperWarnLimitEnergy;
	}

	public void setUpperWarnLimitEnergy(Double upperWarnLimitEnergy) {
		this.upperWarnLimitEnergy = upperWarnLimitEnergy;
	}
	
	@Size(min=0, max=100, message="公司编码长度不能超过 100 个字符")
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
	
}