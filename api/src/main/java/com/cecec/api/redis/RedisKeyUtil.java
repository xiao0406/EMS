package com.cecec.api.redis;

import java.util.ArrayList;
import java.util.List;

public class RedisKeyUtil {

    protected static String DMP_PREFIX = "dmp:";

    protected static String DEVICE_STATUS = ":status";

    protected static String WELD_PARAM = ":weldParam";

    public static String TERMINAL_CODE = "terminalCode:";

    public static String DEVICE_POWER = "device:";

    public static String DEVICE_CONSUMPTION = "powerConsumption";

    public static String deviceStatus(String deviceCode){
        return DMP_PREFIX + deviceCode + DEVICE_STATUS;
    }

    public static String weldParam(String deviceCode){
        return DMP_PREFIX + deviceCode + WELD_PARAM;
    }

    public static String deviceCurrentPower(String deviceCode){
        return DEVICE_POWER + deviceCode + DEVICE_CONSUMPTION;
    }

    public static String terminalStatus(String terminalCode){
        return DMP_PREFIX + TERMINAL_CODE + terminalCode + DEVICE_STATUS;
    }

    public static List<String> terminalStatus(List<String> terminalCodes){
        ArrayList<String> rlt = new ArrayList<>();
        for (String terminalCode : terminalCodes) {
            rlt.add(DMP_PREFIX + TERMINAL_CODE + terminalCode + DEVICE_STATUS);
        }
        return rlt;
    }
}
