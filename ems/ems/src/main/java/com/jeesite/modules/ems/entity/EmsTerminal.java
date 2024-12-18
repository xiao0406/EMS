package com.jeesite.modules.ems.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.BaseEntity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.modules.sys.entity.Employee;
import io.swagger.annotations.*;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import lombok.Data;

/**
 * 终端表Entity
 * @author 李鹏
 * @version 2023-06-14
 */
@Table(name="ems_terminal", alias="a", label="终端表信息", columns={
		@Column(name="terminal_code", attrName="terminalCode", label="终端编码", isPK=true),
		@Column(name="terminal_name", attrName="terminalName", label="终端名称", queryType=QueryType.LIKE),
		@Column(name="terminal_type", attrName="terminalType", label="终端类型"),
		@Column(name="brand_model", attrName="brandModel", label="品牌型号"),
		@Column(name="terminal_sim", attrName="terminalSim", label="SIM卡号", queryType=QueryType.LIKE),
		@Column(name="uplink", attrName="uplink", label="上行通讯"),
		@Column(name="downlink", attrName="downlink", label="下行通讯"),
		@Column(name="mobile_operator", attrName="mobileOperator", label="运营商"),
		@Column(name="mobile_type", attrName="mobileType", label="套餐类型"),
		@Column(name="mobile_expenses", attrName="mobileExpenses", label="套餐资费", isUpdateForce=true),
		@Column(name="start_date", attrName="startDate", label="开通时间", isUpdateForce=true),
		@Column(name="end_date", attrName="endDate", label="到期时间", isUpdateForce=true),
		@Column(includeEntity=DataEntity.class),
		@Column(name = "company_code", attrName = "companyCode", label = "公司编码"),
		@Column(name = "company_name", attrName = "companyName", label = "公司名称", queryType = QueryType.LIKE),
		@Column(includeEntity=BaseEntity.class),
	}, orderBy="a.update_date DESC"
)
@Data
@ApiModel(value = "EmsTerminal对象", description = "终端表Entity")
public class EmsTerminal extends DataEntity<EmsTerminal> {
	
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "终端编码")
	private String terminalCode;
	@ApiModelProperty(value = "终端名称")
	private String terminalName;
	@ApiModelProperty(value = "终端类型")
	private String terminalType;
	@ApiModelProperty(value = "品牌型号")
	private String brandModel;
	@ApiModelProperty(value = "SIM卡号")
	private String terminalSim;
	@ApiModelProperty(value = "上行通讯")
	private String uplink;
	@ApiModelProperty(value = "下行通讯")
	private String downlink;
	@ApiModelProperty(value = "运营商")
	private String mobileOperator;
	@ApiModelProperty(value = "套餐类型")
	private String mobileType;
	@ApiModelProperty(value = "套餐资费")
	private Double mobileExpenses;
	@ApiModelProperty(value = "开通时间")
	private Date startDate;
	@ApiModelProperty(value = "到期时间")
	private Date endDate;
	@ApiModelProperty(value = "公司编码")
	private String companyCode;
	@ApiModelProperty(value = "公司名称")
	private String companyName;
	@ApiModelProperty(value = "电表编号列表")
	private List<String> meterCodeList;

	@ApiModelProperty(value = "电表列表")
	private List<EmsMeter> meterList;

	@ApiModelProperty(value = "是否离线")
	private String isOffLine;
	
	public EmsTerminal() {
		this(null);
	}
	
	public EmsTerminal(String id){
		super(id);
	}

	public List<EmsMeter> getMeterList() {
		return meterList;
	}

	public void setMeterList(List<EmsMeter> meterList) {
		this.meterList = meterList;
	}

	public List<String> getMeterCodeList() {
		return meterCodeList;
	}

	public void setMeterCodeList(List<String> meterCodeList) {
		this.meterCodeList = meterCodeList;
	}

	public String getTerminalCode() {
		return terminalCode;
	}

	public void setTerminalCode(String terminalCode) {
		this.terminalCode = terminalCode;
	}
	
	@NotBlank(message="终端名称不能为空")
	@Size(min=0, max=100, message="终端名称长度不能超过 100 个字符")
	public String getTerminalName() {
		return terminalName;
	}

	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}
	
	@NotBlank(message="终端类型不能为空")
	@Size(min=0, max=32, message="终端类型长度不能超过 32 个字符")
	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}
	
	@Size(min=0, max=32, message="品牌型号长度不能超过 32 个字符")
	public String getBrandModel() {
		return brandModel;
	}

	public void setBrandModel(String brandModel) {
		this.brandModel = brandModel;
	}
	
	@Size(min=0, max=50, message="SIM卡号长度不能超过 50 个字符")
	public String getTerminalSim() {
		return terminalSim;
	}

	public void setTerminalSim(String terminalSim) {
		this.terminalSim = terminalSim;
	}
	
	@Size(min=0, max=32, message="上行通讯长度不能超过 32 个字符")
	public String getUplink() {
		return uplink;
	}

	public void setUplink(String uplink) {
		this.uplink = uplink;
	}
	
	@Size(min=0, max=32, message="下行通讯长度不能超过 32 个字符")
	public String getDownlink() {
		return downlink;
	}

	public void setDownlink(String downlink) {
		this.downlink = downlink;
	}
	
	@Size(min=0, max=100, message="运营商长度不能超过 100 个字符")
	public String getMobileOperator() {
		return mobileOperator;
	}

	public void setMobileOperator(String mobileOperator) {
		this.mobileOperator = mobileOperator;
	}
	
	@Size(min=0, max=100, message="套餐类型长度不能超过 100 个字符")
	public String getMobileType() {
		return mobileType;
	}

	public void setMobileType(String mobileType) {
		this.mobileType = mobileType;
	}
	
	public Double getMobileExpenses() {
		return mobileExpenses;
	}

	public void setMobileExpenses(Double mobileExpenses) {
		this.mobileExpenses = mobileExpenses;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}