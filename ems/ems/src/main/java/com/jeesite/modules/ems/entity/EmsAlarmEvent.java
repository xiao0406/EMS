package com.jeesite.modules.ems.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.BaseEntity;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 报警事件记录表Entity
 * @author 李鹏
 * @version 2023-07-22
 */
@Table(name="ems_alarm_event", alias="a", label="报警事件记录表信息", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="data_date_time", attrName="dataDateTime", label="数据时间， 2023-03-24 22", comment="数据时间， 2023-03-24 22:00:12"),
		@Column(name="data_date", attrName="dataDate", label="数据日期， 2023-03-24"),
		@Column(name="data_time", attrName="dataTime", label="数据时间， 22", comment="数据时间， 22:00:12"),
		@Column(name="data_type", attrName="dataType", label="数据类型", comment="数据类型：15分钟、小时、日、月、年"),
		@Column(name="event_type", attrName="eventType", label="事件类型，参考枚举 ems_event_type"),
		@Column(name="event_type_msg", attrName="eventTypeMsg", label="事件类型描述"),
		@Column(name="event_level", attrName="eventLevel", label="事件级别：3", comment="事件级别：3（事故）、2（警告）、1（提醒）、0（通知）"),
		@Column(name="device_id", attrName="deviceId", label="设备编号"),
		@Column(name="device_name", attrName="deviceName", label="设备名称",queryType=QueryType.LIKE),
		@Column(name="event_content", attrName="eventContent", label="事件内容",queryType=QueryType.LIKE),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", queryType=QueryType.LIKE),
		@Column(name="company_code", attrName="companyCode", label="公司编码", queryType=QueryType.EQ),
		@Column(name="company_name", attrName="companyName", label="公司名称", queryType=QueryType.LIKE),
		@Column(includeEntity=BaseEntity.class),
	}, orderBy="a.update_date DESC"
)
@Data
@ApiModel(value = "EmsAlarmEvent对象", description = "报警事件记录表Entity")
public class EmsAlarmEvent extends DataEntity<EmsAlarmEvent> {
	
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "数据时间， 2023-03-24 22:00:12")
	private Date dataDateTime;
	@ApiModelProperty(value = "数据日期， 2023-03-24")
	private Date dataDate;
	@ApiModelProperty(value = "数据时间， 22:00:12")
	private String dataTime;
	@ApiModelProperty(value = "数据类型：15分钟、小时、日、月、年")
	private String dataType;
	@ApiModelProperty(value = "事件类型，参考枚举 ems_event_type")
	private String eventType;
	@ApiModelProperty(value = "事件类型描述")
	private String eventTypeMsg;
	@ApiModelProperty(value = "事件级别：3（事故）、2（警告）、1（提醒）、0（通知）")
	private String eventLevel;
	@ApiModelProperty(value = "设备编号")
	private String deviceId;
	@ApiModelProperty(value = "设备名称")
	private String deviceName;
	@ApiModelProperty(value = "事件内容")
	private String eventContent;
	@ApiModelProperty(value = "公司编码")
	private String companyCode;
	@ApiModelProperty(value = "公司名称")
	private String companyName;
	@ApiModelProperty(value = "查询开始时间")
	private String qryStartTime;
	@ApiModelProperty(value = "查询结束时间")
	private String qryEndTime;
	public EmsAlarmEvent() {
		this(null);
	}
	
	public EmsAlarmEvent(String id){
		super(id);
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="数据时间， 2023-03-24 22不能为空")
	public Date getDataDateTime() {
		return dataDateTime;
	}

	public void setDataDateTime(Date dataDateTime) {
		this.dataDateTime = dataDateTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="数据日期， 2023-03-24不能为空")
	public Date getDataDate() {
		return dataDate;
	}

	public void setDataDate(Date dataDate) {
		this.dataDate = dataDate;
	}
	
	@NotBlank(message="数据时间， 22不能为空")
	public String getDataTime() {
		return dataTime;
	}

	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}
	
	@NotBlank(message="数据类型不能为空")
	@Size(min=0, max=10, message="数据类型长度不能超过 10 个字符")
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	@NotBlank(message="事件类型，参考枚举 ems_event_type不能为空")
	@Size(min=0, max=10, message="事件类型，参考枚举 ems_event_type长度不能超过 10 个字符")
	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
	@NotBlank(message="事件类型描述不能为空")
	@Size(min=0, max=10, message="事件类型描述长度不能超过 10 个字符")
	public String getEventTypeMsg() {
		return eventTypeMsg;
	}

	public void setEventTypeMsg(String eventTypeMsg) {
		this.eventTypeMsg = eventTypeMsg;
	}
	
	@NotBlank(message="事件级别：3不能为空")
	@Size(min=0, max=10, message="事件级别：3长度不能超过 10 个字符")
	public String getEventLevel() {
		return eventLevel;
	}

	public void setEventLevel(String eventLevel) {
		this.eventLevel = eventLevel;
	}
	
	@NotBlank(message="事件内容不能为空")
	@Size(min=0, max=500, message="事件内容长度不能超过 500 个字符")
	public String getEventContent() {
		return eventContent;
	}

	public void setEventContent(String eventContent) {
		this.eventContent = eventContent;
	}
	
}