package com.jeesite.modules.ems.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EChartMultiData {

    private String label;

    private List<Double> value;

}
