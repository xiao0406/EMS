package com.jeesite.modules.ems.vo;

import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Data
public class TimeSharePowerQueryVo {

    /**
     * 查询维度
     */
    private TemporalGranularityEnum temporalGranularity;
    /**
     * 区域编码
     */
    private String areaCode;
    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 查询开始时间
     */
    private String qryStartTime;
    /**
     * 查询结束时间
     */
    private String qryEndTime;
    /**
     * 查询分组类型
     */
    private String energyGroup;
    private HttpServletRequest request;
    private HttpServletResponse response;
}
