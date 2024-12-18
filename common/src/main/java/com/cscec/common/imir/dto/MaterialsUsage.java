package com.cscec.common.imir.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialsUsage {

    /**
     * 日期
     */
    private String recordDate;

    /**
     * 焊丝用量
     */
    private BigDecimal wireWeight;

    /**
     * 气体消耗
     */
    private BigDecimal gasUsage;

    /**
     * 电量调号
     */
    private BigDecimal electricUsage;

}
