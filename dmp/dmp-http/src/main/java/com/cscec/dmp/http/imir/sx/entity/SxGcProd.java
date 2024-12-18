package com.cscec.dmp.http.imir.sx.entity;


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
 * 坡口切割单元生产数据记录表
 * @TableName dcs_sx_gc_prod
 */
@TableName(value ="dcs_sx_gc_prod")
@Data
public class SxGcProd implements Serializable {
    /**
     * 主键
     */
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
     * 白班产量
     */
    private Double yieldDay;

    /**
     * 夜班产量
     */
    private Double yieldNight;

    /**
     * 白班运行时间
     */
    private Double runTimeDay;

    /**
     * 夜班运行时间
     */
    private Double runTimeNight;

    /**
     * 白班稼动率
     */
    private Double utilizationDay;

    /**
     * 夜班稼动率
     */
    private Double utilizationNight;

    /**
     * 白班切割米数
     */
    private Double metersDay;

    /**
     * 夜班切割米数
     */
    private Double metersNight;

    /**
     * 白班不良率
     */
    private Double defectRateDay;

    /**
     * 夜班不良率
     */
    private Double defectRateNight;

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