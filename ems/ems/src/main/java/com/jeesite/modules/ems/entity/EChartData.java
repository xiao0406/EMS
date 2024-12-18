package com.jeesite.modules.ems.entity;

import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EChartData {

    private String label;

    private String value;

}
