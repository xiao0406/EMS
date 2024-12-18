package com.jeesite.modules.ems.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.BaseEntity;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 电表设备表Entity
 *
 * @author 李鹏
 * @version 2023-06-06
 */
@Table(name = "ems_meter", alias = "a", label = "电表设备表信息", columns = {
        @Column(name = "meter_code", attrName = "meterCode", label = "电表编号", isPK = true),
        @Column(name = "meter_name", attrName = "meterName", label = "电表名称", queryType = QueryType.LIKE),
        @Column(name = "brand_model", attrName = "brandModel", label = "品牌型号", queryType = QueryType.LIKE),
        @Column(name = "pt", attrName = "pt", label = "电压倍率"),
        @Column(name = "ct", attrName = "ct", label = "电流倍率"),
        @Column(name = "qt", attrName = "qt", label = "综合倍率"),
        @Column(name = "installation_date", attrName = "installationDate", label = "安装时间"),
        @Column(name = "installation_position", attrName = "installationPosition", label = "安装位置"),
        @Column(name = "gps", attrName = "gps", label = "gps坐标"),
        @Column(name = "px", attrName = "px", label = "前端坐标;{＂xx＂", comment = "前端坐标;{＂xx＂:{＂x＂:100,＂y＂,110}}"),
        @Column(name = "electricity_type", attrName = "electricityType", label = "用电类型"),
        @Column(name = "no_load_threshold", attrName = "noLoadThreshold", label = "空载阈值"),
        @Column(name = "sort", attrName = "sort", label = "排序"),
        @Column(name = "operation_threshold", attrName = "operationTreshold", label = "运行阈值"),
        @Column(includeEntity = DataEntity.class),
        @Column(name = "company_code", attrName = "companyCode", label = "公司编码"),
        @Column(name = "company_name", attrName = "companyName", label = "公司名称", queryType = QueryType.LIKE),
        @Column(includeEntity = BaseEntity.class),
    }, joinTable = {
        @JoinTable(type = JoinTable.Type.JOIN, entity = EmsAreaMeter.class, attrName = "this", alias = "b",
                on = "b.meter_code = a.meter_code",
                columns = {
                        @Column(name="electricity_mark", attrName="electricityMark", label="计量标识;在区域能耗统计时，用于判断是否统计"),
                }),
    }, orderBy = "a.sort ASC"
)
@ApiModel(value = "EmsMeter对象", description = "电表设备表Entity")
public class EmsMeter extends DataEntity<EmsMeter> {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "电表名称")
    private String meterName;
    @ApiModelProperty(value = "电表编号")
    private String meterCode;
    @ApiModelProperty(value = "品牌型号")
    private String brandModel;
    @ApiModelProperty(value = "电压倍率")
    private Double pt;
    @ApiModelProperty(value = "电流倍率")
    private Double ct;
    @ApiModelProperty(value = "综合倍率")
    private Double qt;
    @ApiModelProperty(value = "安装时间")
    private Date installationDate;
    @ApiModelProperty(value = "安装位置")
    private String installationPosition;
    @ApiModelProperty(value = "gps坐标")
    private String gps;
    @ApiModelProperty(value = "前端坐标;{＂xx＂:{＂x＂:100,＂y＂,110}}")
    private String px;
    @ApiModelProperty(value = "用电类型")
    private String electricityType;
    @ApiModelProperty(value = "空载阈值")
    private Double noLoadThreshold;
    @ApiModelProperty(value = "运行阈值")
    private Double operationTreshold;

    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "公司编码")
    private String companyCode;
    @ApiModelProperty(value = "公司名称")
    private String companyName;
    
    @ApiModelProperty(value = "区域编号")
    private String areaCode;

    @ApiModelProperty(value = "计量标识")
    private String electricityMark;

    @ApiModelProperty(value = "电表部门绑定配置")
    private List<EmsMeterOffice> meterOfficeList;

    public EmsMeter() {
        this(null);
    }

    public EmsMeter(String id) {
        super(id);
    }

    public String getCompanyCode() {
        return companyCode;
    }
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Double getNoLoadThreshold() {
        return noLoadThreshold;
    }

    public void setNoLoadThreshold(Double noLoadThreshold) {
        this.noLoadThreshold = noLoadThreshold;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Double getOperationTreshold() {
        return operationTreshold;
    }

    public void setOperationTreshold(Double operationTreshold) {
        this.operationTreshold = operationTreshold;
    }

    public List<EmsMeterOffice> getMeterOfficeList() {
        return meterOfficeList;
    }

    public void setMeterOfficeList(List<EmsMeterOffice> meterOfficeList) {
        this.meterOfficeList = meterOfficeList;
    }

    @NotBlank(message = "区域编号不能为空")
    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    @NotBlank(message = "计量标识不能为空")
    public String getElectricityMark() {
        return electricityMark;
    }

    public void setElectricityMark(String electricityMark) {
        this.electricityMark = electricityMark;
    }

    @NotBlank(message = "用电类型不能为空")
    @Size(min = 0, max = 32, message = "用电类型长度不能超过 32 个字符")
    public String getElectricityType() {
        return electricityType;
    }

    public void setElectricityType(String electricityType) {
        this.electricityType = electricityType;
    }

    @NotBlank(message = "电表名称不能为空")
    @Size(min = 0, max = 100, message = "电表名称长度不能超过 100 个字符")
    public String getMeterName() {
        return meterName;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }

    @NotBlank(message = "电表编号不能为空")
    @Size(min = 0, max = 64, message = "电表编号长度不能超过 64 个字符")
    public String getMeterCode() {
        return meterCode;
    }

    public void setMeterCode(String meterCode) {
        this.meterCode = meterCode;
    }

    @Size(min = 0, max = 100, message = "品牌型号长度不能超过 100 个字符")
    public String getBrandModel() {
        return brandModel;
    }

    public void setBrandModel(String brandModel) {
        this.brandModel = brandModel;
    }

    @NotNull(message = "电压倍率不能为空")
    public Double getPt() {
        return pt;
    }

    public void setPt(Double pt) {
        this.pt = pt;
    }

    @NotNull(message = "电流倍率不能为空")
    public Double getCt() {
        return ct;
    }

    public void setCt(Double ct) {
        this.ct = ct;
    }

    @NotNull(message = "综合倍率不能为空")
    public Double getQt() {
        return qt;
    }

    public void setQt(Double qt) {
        this.qt = qt;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(Date installationDate) {
        this.installationDate = installationDate;
    }

    @Size(min = 0, max = 100, message = "安装位置长度不能超过 100 个字符")
    public String getInstallationPosition() {
        return installationPosition;
    }

    public void setInstallationPosition(String installationPosition) {
        this.installationPosition = installationPosition;
    }

    @Size(min = 0, max = 100, message = "gps坐标长度不能超过 100 个字符")
    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    @Size(min = 0, max = 255, message = "前端坐标;{＂xx＂长度不能超过 255 个字符")
    public String getPx() {
        return px;
    }

    public void setPx(String px) {
        this.px = px;
    }

}