package com.jeesite.modules.ems.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 *  设备单日利用率统计柱状图Entity
 */
@ApiModel(value = "EmsDailyStatisticsEchartEntity", description = "设备单日利用率统计柱状图Entity")
@Data
public class EmsDailyStatisticsEchartEntity {

    /**
     *  能耗节点
     */
    private List<EChartData> consumptionData;

    /**
     * 机器状态节点
     */
    private List<EChartData> statusData;

}
