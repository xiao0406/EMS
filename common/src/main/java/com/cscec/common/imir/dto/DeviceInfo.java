package com.cscec.common.imir.dto;

import lombok.Data;

@Data
public class DeviceInfo {

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 品牌
     */
    private String deviceBrand;

    /**
     * 型号
     */
    private String deviceModel;

    /**
     * 运行状态
     */
    private int runStatus;
}
