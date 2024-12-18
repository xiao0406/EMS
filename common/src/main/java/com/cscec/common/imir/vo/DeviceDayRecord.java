package com.cscec.common.imir.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
public class DeviceDayRecord {

    @JsonProperty("D01")
    private String deviceCode;

    private List<Info> list;

    @Data
    public static class Info{

        /**
         * 班组名称
         */
        @JsonProperty("D02")
        private String team;

        /**
         * 班次
         */
        @JsonProperty("D03")
        private String teamLot;

        /**
         * 日报时间
         */
        @JsonProperty("D04")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate recordDate;

        /**
         * 焊接时长（秒）
         */
        @JsonProperty("D05")
        private String weldTime;

        /**
         * 开机时长（秒）
         */
        @JsonProperty("D06")
        private String runTime;

        /**
         * 焊丝消耗（kg）
         */
        @JsonProperty("D07")
        private String wireWeight;

        /**
         * 气体消耗（L）
         */
        @JsonProperty("D08")
        private Double gasUsage;

        /**
         * 电能消耗（kWh）
         */
        @JsonProperty("D09")
        private Double electricUsage;
    }
}
