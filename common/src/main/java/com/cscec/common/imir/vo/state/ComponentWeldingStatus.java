package com.cscec.common.imir.vo.state;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComponentWeldingStatus {

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 运行状态 0:待机,1:焊接中,2:故障
     */
    private int runStatus;

    /**
     * 运行模式 0:手动,1:自动
     */
    private int runMode;

    /**
     * 电压
     */
    private double voltage;

    /**
     * 电流
     */
    private double electrical;
}
