package com.jeesite.modules.ems.entity;

import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.entity.DataEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StageCumulativeQueryEntity extends DataEntity<StageCumulativeQueryEntity> {

    private String companyCode;

    private Date startTime;

    private String startKey;

    private Date endTime;

    private String endKey;

    private TemporalGranularityEnum temporalGranularity;

    private String format;

    private List<String> meterMarkList;
}
