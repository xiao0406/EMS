package com.jeesite.modules.ems.entity;

import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.modules.ems.entity.enums.EnergyGroupEnum;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@ApiModel(value = "EnergyManageEntity对象", description = "能源管理页面Entity")
@Data
public class EnergyManageParam extends CompanyVo{
    /**
     * 报表查询维度
     */
    private TemporalGranularityEnum temporalGranularity;

    /**
     * 区域编码
     */
    private String areaCode;

    /**
     * 设备编码
     */
    private String meterCode;

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
    private EnergyGroupEnum energyGroup;
}
