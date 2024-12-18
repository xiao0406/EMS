package com.cscec.common.imir.vo.state;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrooveCuttingStatus {

    private String deviceNumber;

    private int cuttingRobotStatus;

    private int handlingRobotStatus;

    private int unitStatus;
}
