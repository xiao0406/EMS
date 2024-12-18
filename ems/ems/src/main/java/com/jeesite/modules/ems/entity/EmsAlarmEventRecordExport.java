package com.jeesite.modules.ems.entity;

import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import io.swagger.annotations.ApiModel;

/**
 * 报警事件记录表导出Entity
 * @author 吴鹏
 * @version 2023-08-14
 */
@ApiModel(value = "EmsAlarmEvent对象", description = "报警事件记录表Entity")
public class EmsAlarmEventRecordExport extends EmsAlarmEvent {

	private static final long serialVersionUID = 1L;



		@ExcelFields({
			@ExcelField(title="电表名称", attrName = "deviceName", align = ExcelField.Align.CENTER, sort = 10),
			@ExcelField(title="电表编号", attrName="deviceId", align = ExcelField.Align.CENTER, sort=20),
			@ExcelField(title="事件时间", attrName="dataDateTime", align = ExcelField.Align.CENTER, sort=30,width =7000,dataFormat="yyyy-MM-dd hh:MM:ss"),
			@ExcelField(title="事件类型", attrName="eventTypeMsg", align = ExcelField.Align.CENTER, sort=40),
			@ExcelField(title="事件级别", attrName="eventLevel", dictType="ems_event_level", align = ExcelField.Align.CENTER, sort=41),
			@ExcelField(title="事件内容", attrName="eventContent", align = ExcelField.Align.CENTER, sort=50, width = 16400),
	})
	public EmsAlarmEventRecordExport() {
		this(null);
	}

	public EmsAlarmEventRecordExport(String id){
		super(id);
	}


}