package com.jeesite.modules.job.entity;

import lombok.Data;

import java.util.List;

@Data
public class CalculateJobParam {

    /**
     * 公司编码
     */
    private List<String> companyCodes;

    /**
     * 执行时间
     */
    private String appointedTime;

    public CalculateJobParam() {
    }

    public CalculateJobParam(List<String> companyCodes, String appointedTime) {
        this.companyCodes = companyCodes;
        this.appointedTime = appointedTime;
    }
}
