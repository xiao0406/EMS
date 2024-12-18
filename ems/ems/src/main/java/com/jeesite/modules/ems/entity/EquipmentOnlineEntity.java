package com.jeesite.modules.ems.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentOnlineEntity {
    /**
     * 设备类型
     */
    private String equipmentType;

    /**
     * 在线数
     */
    private Integer onlineNum;

    /**
     * 总数
     */
    private Integer totalNum;
}
