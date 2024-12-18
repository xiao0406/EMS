package com.jeesite.modules.dcs.yh.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * 焊接数量
 */
@Data
public class DeviceCapacityDTO {

    private static final long serialVersionUID = 161591956156L;

    private String deviceCode;
    private String deviceName;
    private LocalDate recordDate;
    private Double capacity;
}