package com.cscec.common.imir.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultEntity {

    @JsonProperty("Execution")
    private Integer execution;

    private Object[] dataList;
}
