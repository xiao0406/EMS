package com.jeesite.modules.ems.entity;

import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "EmsAlarmRecordEntity对象", description = "报警记录Entity")
@Data
public class EmsAlarmRecordEntity {
    /**
     * 事件名称
     */
    private String eventTitle;

    /**
     * 发生时间
     */
    private String eventTime;

    /**
     * 报警内容
     */
    private String eventConstant;

    @ExcelFields({
            @ExcelField(title = "事件名称", attrName = "eventTitle", align = ExcelField.Align.CENTER, sort = 10),
            @ExcelField(title = "发生时间", attrName = "eventTime", align = ExcelField.Align.CENTER, sort = 30),
            @ExcelField(title = "报警内容", attrName = "eventConstant", align = ExcelField.Align.CENTER, sort = 40),
    })
    public EmsAlarmRecordEntity() {
    }
}
