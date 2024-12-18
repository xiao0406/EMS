package com.jeesite.modules.ems.entity;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import com.jeesite.common.entity.BaseEntity;

import com.jeesite.common.entity.DataEntity;
import io.swagger.annotations.*;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import lombok.Data;

/**
 * 电表产量表Entity
 * @author 李鹏
 * @version 2023-06-15
 */
@Table(name="ems_meter_yield", alias="a", label="电表产量表信息", columns={
		@Column(name="meter_code", attrName="meterCode", label="电表编号", isPK=true),
		@Column(name="meter_name", attrName="meterName", label="电表名称", queryType=QueryType.LIKE),
		@Column(name="data_month", attrName="dataMonth", label="数据月份", isPK=true),
		@Column(name="yield", attrName="yield", label="产量"),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", queryType=QueryType.LIKE),
		@Column(name = "company_code", attrName = "companyCode", label = "公司编码"),
		@Column(name = "company_name", attrName = "companyName", label = "公司名称", queryType = QueryType.LIKE),
		@Column(includeEntity=BaseEntity.class),
	}, orderBy="a.update_date DESC"
)
@Data
@ApiModel(value = "EmsMeterYield对象", description = "电表产量表Entity")
public class EmsMeterYield extends DataEntity<EmsMeterYield> {
	
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "电表编号")
	private String meterCode;
	@ApiModelProperty(value = "电表名称")
	private String meterName;
	@ApiModelProperty(value = "数据月份")
	private String dataMonth;
	@ApiModelProperty(value = "产量")
	private Double yield;
	@ApiModelProperty(value = "公司编码")
	private String companyCode;
	@ApiModelProperty(value = "公司名称")
	private String companyName;

	public EmsMeterYield() {
		this(null, null);
	}
	
	public EmsMeterYield(String meterCode, String dataMonth){
		this.meterCode = meterCode;
		this.dataMonth = dataMonth;
	}
	
	public String getMeterCode() {
		return meterCode;
	}

	public void setMeterCode(String meterCode) {
		this.meterCode = meterCode;
	}
	
	@Size(min=0, max=100, message="电表名称长度不能超过 100 个字符")
	public String getMeterName() {
		return meterName;
	}

	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}
	
	public String getDataMonth() {
		return dataMonth;
	}

	public void setDataMonth(String dataMonth) {
		this.dataMonth = dataMonth;
	}
	
	@NotNull(message="产量不能为空")
	public Double getYield() {
		return yield;
	}

	public void setYield(Double yield) {
		this.yield = yield;
	}
	
}