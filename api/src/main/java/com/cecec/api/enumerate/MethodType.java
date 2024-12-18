package com.cecec.api.enumerate;

public enum MethodType {

    state("状态","state", 0),
    prod("生产","prod", 0),
    alarm("维修","alarm", 1);

    // 成员变量
    private String name;
    private String type;
    private final int index;

    MethodType(String name, String type, int index) {
        this.name = name;
        this.type = type;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (MethodType c : MethodType.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public static MethodType getEnum(int index) {
        for (MethodType c : MethodType.values()) {
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
