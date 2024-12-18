package com.jeesite.modules.dcs.base.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.BaseEntity;

import com.jeesite.common.entity.DataEntity;
import io.swagger.annotations.*;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 设备信息Entity
 * @author ds
 * @version 2023-06-25
 */
@Table(name="dcs_device", alias="a", label="设备信息信息", columns={
        @Column(name="id", attrName="id", label="主键", isPK=true),
        @Column(name="device_code", attrName="deviceCode", label="设备出厂编号"),
        @Column(name="device_name", attrName="deviceName", label="设备名称", queryType=QueryType.LIKE),
        @Column(name="device_number", attrName="deviceName", label="自定义编号", queryType=QueryType.LIKE),
        @Column(name="device_type", attrName="deviceType", label="设备类型"),
        @Column(name="device_brand", attrName="deviceBrand", label="品牌"),
        @Column(name="device_model", attrName="deviceModel", label="型号"),
        @Column(name="run_status", attrName="runStatus", label="设备状态"),
        @Column(name="office_code", attrName="officeCode", label="机构代码"),
        @Column(name="office_name", attrName="officeName", label="机构名称", queryType=QueryType.LIKE),
        @Column(name="production_date", attrName="productionDate", label="出厂日期", isUpdateForce=true),
        @Column(name="installation_date", attrName="installationDate", label="安装日期", isUpdateForce=true),
        @Column(name="installation_position", attrName="installationPosition", label="安装位置"),
        @Column(name="host", attrName="host", label="主机地址"),
        @Column(name="port", attrName="port", label="端口", isUpdateForce=true),
        @Column(name="gps", attrName="gps", label="gps坐标"),
        @Column(name="px", attrName="px", label="前端坐标;{＂xx＂", comment="前端坐标;{＂xx＂:{＂x＂:100,＂y＂,110}}"),
        @Column(name="image_url", attrName="imageUrl", label="图片地址"),
        @Column(includeEntity=DataEntity.class),
        @Column(includeEntity=BaseEntity.class),
}, orderBy="a.id ASC"
)
@ApiModel(value = "Device对象", description = "设备信息Entity")
public class Device extends DataEntity<Device> {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    private String id;
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
    @ApiModelProperty(value = "出厂日期")
    private Date productionDate;
    @ApiModelProperty(value = "安装日期")
    private Date installationDate;
    @ApiModelProperty(value = "安装位置")
    private String installationPosition;
    @ApiModelProperty(value = "主机地址")
    private String host;
    @ApiModelProperty(value = "端口")
    private Long port;
    @ApiModelProperty(value = "gps坐标")
    private String gps;
    @ApiModelProperty(value = "前端坐标;{＂xx＂:{＂x＂:100,＂y＂,110}}")
    private String px;
    @ApiModelProperty(value = "图片地址")
    private String imageUrl;


    public Device() {
        this(null);
    }

    public Device(String id){
        super(id);
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    @NotBlank(message="设备名称不能为空")
    @Size(min=0, max=100, message="设备名称长度不能超过 100 个字符")
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    @Size(min=0, max=32, message="机构代码长度不能超过 32 个字符")
    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    @Size(min=0, max=100, message="机构名称长度不能超过 100 个字符")
    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(Date installationDate) {
        this.installationDate = installationDate;
    }

    @Size(min=0, max=100, message="安装位置长度不能超过 100 个字符")
    public String getInstallationPosition() {
        return installationPosition;
    }

    public void setInstallationPosition(String installationPosition) {
        this.installationPosition = installationPosition;
    }

    @Size(min=0, max=100, message="主机地址长度不能超过 100 个字符")
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Long getPort() {
        return port;
    }

    public void setPort(Long port) {
        this.port = port;
    }

    @Size(min=0, max=100, message="gps坐标长度不能超过 100 个字符")
    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    @Size(min=0, max=255, message="前端坐标;{＂xx＂长度不能超过 255 个字符")
    public String getPx() {
        return px;
    }

    public void setPx(String px) {
        this.px = px;
    }

    @Size(min=0, max=255, message="图片地址")
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(int runStatus) {
        this.runStatus = runStatus;
    }
}