package com.cscec.common.imir.vo.state;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PanasonicWeldStatus {

    /**
     * 制造编码
     */
    @JsonProperty("D01")
    private String deviceCode;

    /**
     * 班组名称
     */
    @JsonProperty("D02")
    private String team;

    /**
     * 设备状态(待机、焊接、报警、关机)
     */
    @JsonProperty("D03")
    private String runStatus;

    /**
     * 锁定（是/否）
     */
    @JsonProperty("D04")
    private String lock;

    /**
     * 预置电流（A）
     */
    @JsonProperty("D05")
    private String presetElectrical;

    /**
     * 预置电压（V）
     */
    @JsonProperty("D06")
    private String presetVoltage;

    /**
     * 焊接电流（A）
     */
    @JsonProperty("D07")
    private String electrical;

    /**
     * 焊接电压（V）
     */
    @JsonProperty("D08")
    private String voltage;

    /**
     * 材质
     */
    @JsonProperty("D09")
    private String wireType;

    /**
     * 焊丝直径
     */
    @JsonProperty("D10")
    private String wireDiameter;

    /**
     * 气体
     */
    @JsonProperty("D11")
    private String gasType;

    /**
     * 焊接方式
     */
    @JsonProperty("D12")
    private String weldMode;

    /**
     * 焊接控制
     */
    @JsonProperty("D13")
    private String weldControl;

    /**
     * 人员编号
     */
    @JsonProperty("D14")
    private String personnelNumber;

    /**
     * 工件编号
     */
    @JsonProperty("D15")
    private String partNumber;

}
