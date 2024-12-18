package com.jeesite.modules.ems.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 *  设备单日利用率电表阈值Entity
 */
@ApiModel(value = "EmsMeterThresholdEntity对象", description = "设备单日利用率电表阈值Entity")
@Data
public class EmsMeterThresholdEntity {

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 空载阈值
     */
    private Double noLoadThreshold;

    /**
     * 运行阈值
     */
    private Double operationThreshold;
}
