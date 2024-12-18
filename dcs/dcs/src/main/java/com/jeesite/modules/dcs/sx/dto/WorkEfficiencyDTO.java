package com.jeesite.modules.dcs.sx.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class WorkEfficiencyDTO {

    private String deviceCode;
    private String deviceName;
    private LocalDate recordDate;
    private BigDecimal workEfficiency;
}