package com.cscec.common.imir.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComponentWeldingParam {

    private String deviceCode;
    private String abutmentType;
    private String weldType;
    private String blunt;
    private String clearance;
    private String grooveType;
    private String grooveAngle;
    private String gasType;
    private String wireType;
    private String wireDiameter;
    private String weldMode;
    private String weldElectrical;
    private String weldVoltage;
    private String weldWidth;
    private String weldHeight;
    private String weldSpeed;
    private String swingType;
    private String amplitude;
    private String ts;

    public ComponentWeldingParam() {
    }

    public ComponentWeldingParam(String deviceCode) {
        this.deviceCode = deviceCode;
    }
}
