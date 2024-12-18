package com.cecec.api.util;

public class ZoneUtil {

    public static String ZoneTableName(String zoneCode, Integer year){
        return "dmp_mqtt_" + zoneCode + "_" + year;
    }
}
