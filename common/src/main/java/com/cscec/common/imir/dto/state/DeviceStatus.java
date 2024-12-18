package com.cscec.common.imir.dto.state;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class DeviceStatus<T> {

    private String deviceCode;
    private int runStatus;
    private String ts;
    private T data;

    public DeviceStatus() {
    }

    public DeviceStatus(String deviceCode, int runStatus) {
        this.deviceCode = deviceCode;
        this.runStatus = runStatus;
        this.ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public DeviceStatus(String deviceCode, int runStatus, T data) {
        this.deviceCode = deviceCode;
        this.runStatus = runStatus;
        this.data = data;
        this.ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
