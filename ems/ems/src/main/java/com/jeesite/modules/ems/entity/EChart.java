package com.jeesite.modules.ems.entity;

import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EChart<K, V> {
    /**
     * 报表查询维度
     */
    private TemporalGranularityEnum temporalGranularity;

    private K head;

    /**
     * 数据结果
     */
    private V body;
}
