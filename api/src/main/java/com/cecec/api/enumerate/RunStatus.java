package com.cecec.api.enumerate;

public enum RunStatus {

    Close("关机", 0),
    Run("运行", 1),
    Wait("待机", 2),
    Fault ("故障维修", 3);
    // 成员变量
    private String name;
    private final int index;

    // 构造方法
    RunStatus(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (RunStatus c : RunStatus.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public static RunStatus getEnum(int index) {
        for (RunStatus c : RunStatus.values()) {
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
}
