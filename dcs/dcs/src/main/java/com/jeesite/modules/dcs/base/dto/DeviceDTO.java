package com.jeesite.modules.dcs.base.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceDTO {
    private static final long serialVersionUID = 1L;
    private Long id;
    @ApiModelProperty(value = "设备出厂编号")
    private String deviceCode;
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    @ApiModelProperty(value = "设备自定义编号")
    private String deviceNumber;
    @ApiModelProperty(value = "类型")
    private int deviceType;
    @ApiModelProperty(value = "品牌")
    private String deviceBrand;
    @ApiModelProperty(value = "型号")
    private String deviceModel;
    @ApiModelProperty(value = "运行状态")
    private int runStatus;
    @ApiModelProperty(value = "机构代码")
    private String officeCode;
    @ApiModelProperty(value = "机构名称")
    private String officeName;
    @ApiModelProperty(value = "图片地址")
    private String imageUrl;
}
