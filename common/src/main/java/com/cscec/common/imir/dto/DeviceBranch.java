package com.cscec.common.imir.dto;

import lombok.Data;

@Data
public class DeviceBranch {

    /**
     * 设备数量
     */
    private int total;

    /**
     * 运行数量
     */
    private int run;

    /**
     * 关机数量
     */
    private int close;

    /**
     * 待机数量
     */
    private int wait;

    /**
     * 维修数量
     */
    private int fault;
}
