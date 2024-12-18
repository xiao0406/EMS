package com.cecec.api.base;

/**
 * 区域公司
 */
public enum ZoneEnum {

    GD("gd"),// 广东厂
    SC("sc"),// 四川厂
    WH("wh"),// 武汉厂
    JS("js"),// 江苏厂
    TJ("tj");// 天津厂

    private final String topic;

    public String getTopic() {
        return topic;
    }

    ZoneEnum(String topic) {
        this.topic = topic;
    }

    public static boolean isExist(String topic) {
        for (ZoneEnum zoneEnum : ZoneEnum.values()) {
            if (zoneEnum.getTopic().equalsIgnoreCase(topic)) {
                return true;
            }
        }
        return false;
    }
}
