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
public class EmsAlarmEventExport extends EmsAlarmEvent {

	private static final long serialVersionUID = 1L;

	private int sortNum;


	@ExcelFields({
			@ExcelField(title="序号", attrName = "sortNum", align = ExcelField.Align.CENTER, sort = 10),
			@ExcelField(title="事件名称", attrName="eventTypeMsg", align = ExcelField.Align.CENTER, sort=20),
			@ExcelField(title="发生时间", attrName="dataDateTime", align = ExcelField.Align.CENTER, sort=30, width = 7000,dataFormat="yyyy-MM-dd hh:MM:ss"),
			@ExcelField(title="报警内容", attrName="eventContent", align = ExcelField.Align.CENTER, sort=40, width = 16400),
	})
	public EmsAlarmEventExport() {
		this(null);
	}

	public EmsAlarmEventExport(String id){
		super(id);
	}


	public int getSortNum() {
		return sortNum;
	}

	public void setSortNum(int sortNum) {
		this.sortNum = sortNum;
	}
}