package com.cscec.dmp.mqtt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName dmp_mqtt_error_data
 */
@Data
@TableName("dmp_mqtt_error_data")
public class MqttErrorData implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 主题
     */
    private String topic;

    /**
     * 机构编号
     */
    private String meterCode;

    /**
     * 终端编号
     */
    private String terminalCode;

    /**
     * 有效载荷
     */
    private String payload;

    /**
     * 时间戳
     */
    private LocalDateTime ts;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}