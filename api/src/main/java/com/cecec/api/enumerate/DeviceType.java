package com.cecec.api.enumerate;

public enum DeviceType {

    grooveCutting("坡口切割单元", "grooveCutting",0),
    componentWelding("部件焊接单元", "componentWelding",1);

    // 成员变量
    private String name;
    private String type;
    private final int index;

    DeviceType(String name, String type, int index) {
        this.name = name;
        this.type = type;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (DeviceType c : DeviceType.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public static DeviceType getEnum(int index) {
        for (DeviceType c : DeviceType.values()) {
            if (c.getIndex() == index) {
                return c;
            }
        }
        return null;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
