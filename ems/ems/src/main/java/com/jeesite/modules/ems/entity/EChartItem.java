package com.jeesite.modules.ems.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class EChartItem {

    private Object lable;

    private Object value;

    public EChartItem(Object lable, Object value) {
        this.lable = lable;
        this.value = value;
    }
}
