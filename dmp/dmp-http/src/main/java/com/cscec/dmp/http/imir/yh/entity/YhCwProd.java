package com.cscec.dmp.http.imir.yh.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 部件焊接单元生产数据记录表
 * @TableName dcs_yh_cw_prod
 */
@TableName(value ="dcs_yh_cw_prod")
@Data
public class YhCwProd implements Serializable {

    @TableId
    private String id;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 记录日期
     */
    private LocalDate recordDate;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 焊丝使用长度
     */
    private BigDecimal wireLength;

    /**
     * 焊丝使用重量
     */
    private BigDecimal wireWeight;

    /**
     * 白班运行时间
     */
    private BigDecimal dayRunTime;

    /**
     * 夜班运行时间
     */
    private BigDecimal nightRunTime;

    /**
     * 白班稼动率
     */
    private BigDecimal dayUtilizationRate;

    /**
     * 夜班稼动率
     */
    private BigDecimal nightUtilizationRate;

    /**
     * 白班焊接数量
     */
    private BigDecimal dayCapacity;

    /**
     * 夜班焊接数量
     */
    private BigDecimal nightCapacity;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateDate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}