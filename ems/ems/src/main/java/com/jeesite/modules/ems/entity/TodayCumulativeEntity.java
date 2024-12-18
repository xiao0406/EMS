package com.jeesite.modules.ems.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodayCumulativeEntity {
    /**
     * 展示标题
     */
    private String modelTopic;

    /**
     * 今日累计用电
     */
    private Double todayCumulative;

    /**
     * 较昨日同时段
     */
    private Double yesterdayQoQ;

    /**
     * 较上周同时段
     */
    private Double lastWeekQoQ;
}
