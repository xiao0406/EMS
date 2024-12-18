package com.jeesite.modules.ems.entity;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.entity.SheetHead;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 统计报表entity
 */
@ApiModel(value = "EmsStatisticsReportEntity对象", description = "报表查询Entity")
@Data
public class EmsStatisticsReportEntity {

    /**
     * 查询维度
     */
    private TemporalGranularityEnum temporalGranularity;

    /**
     * 查询开始时间
     */
    private String qryStartTime;

    /**
     * 查询结束时间
     */
    private String qryEndTime;

    /**
     * 查询类型 0 电量  1 百分比  2 all_in
     */
    private String qryType;

    /**
     * 表头
     */
    private List<SheetHead> heads;

    /**
     * 报表数据
     */
    private List<JSONObject> data;

    private String companyCode;
}
