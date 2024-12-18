package com.jeesite.modules.ems.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DataSummary {

    @ApiModelProperty(value = "x轴数据")
    private String xaxis;
    @ApiModelProperty(value = "y轴数据")
    private String yaxis;
    private String lable;
}
