package com.jeesite.modules.ems.entity;


import com.jeesite.common.entity.DataEntity;
import io.swagger.annotations.*;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * 终端电表绑定表Entity
 * @author 李鹏
 * @version 2023-06-14
 */
@Table(name="ems_terminal_meter", alias="a", label="终端电表绑定表信息", columns={
		@Column(name="terminal_code", attrName="terminalCode", label="终端编码", isPK=true),
		@Column(name="meter_code", attrName="meterCode", label="电表编码", isPK=true),
	}, orderBy="a.terminal_code DESC, a.meter_code DESC"
)
@ApiModel(value = "EmsTerminalMeter对象", description = "终端电表绑定表Entity")
public class EmsTerminalMeter extends DataEntity<EmsTerminalMeter> {
	
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "终端编码")
	private String terminalCode;
	@ApiModelProperty(value = "电表编码")
	private String meterCode;
	
	public EmsTerminalMeter() {
		this(null, null);
	}
	
	public EmsTerminalMeter(String terminalCode, String meterCode){
		this.terminalCode = terminalCode;
		this.meterCode = meterCode;
	}
	
	public String getTerminalCode() {
		return terminalCode;
	}

	public void setTerminalCode(String terminalId) {
		this.terminalCode = terminalId;
	}
	
	public String getMeterCode() {
		return meterCode;
	}

	public void setMeterCode(String meterCode) {
		this.meterCode = meterCode;
	}
	
}