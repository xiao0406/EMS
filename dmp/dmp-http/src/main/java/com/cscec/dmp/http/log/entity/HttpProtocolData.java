package com.cscec.dmp.http.log.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @TableName dmp_http_protocol_data
 */
@TableName(value ="dmp_http_protocol_data")
@Data
@Builder
public class HttpProtocolData implements Serializable {
    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     * 客户端ip
     */
    private String remoteAddr;

    /**
     * 设备类型
     */
    private String type;

    /**
     * 方法名称
     */
    private String method;

    /**
     * 数据
     */
    private String body;

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

