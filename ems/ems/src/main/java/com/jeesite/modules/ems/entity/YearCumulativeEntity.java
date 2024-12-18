package com.jeesite.modules.ems.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YearCumulativeEntity {
    /**
     * 展示标题
     */
    private String modelTopic;

    /**
     * 本月累计用电
     */
    private Double yearCumulative;

    /**
     * 较上月环比
     */
    private Double lastYearQoQ;
}
