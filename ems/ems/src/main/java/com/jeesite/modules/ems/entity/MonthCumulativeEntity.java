package com.jeesite.modules.ems.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonthCumulativeEntity {
    /**
     * 展示标题
     */
    private String modelTopic;

    /**
     * 本月累计用电
     */
    private Double monthCumulative;

    /**
     * 较上月环比
     */
    private Double lastMonthQoQ;
}
