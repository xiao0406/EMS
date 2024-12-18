package com.jeesite.modules.ems.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;
import com.jeesite.common.entity.BaseEntity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import io.swagger.annotations.*;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 峰平谷设备运行时长表Entity
 * @author 李鹏
 * @version 2023-06-27
 */
@Table(name="ems_time_share_device_runtime", alias="a", label="峰平谷设备运行时长表信息", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="device_id", attrName="deviceId", label="设备ID"),
		@Column(name="device_name", attrName="deviceName", label="设备名称", queryType=QueryType.LIKE),
		@Column(name="data_date", attrName="dataDate", label="数据日期， 2023-03-24"),
		@Column(name="data_type", attrName="dataType", label="数据类型", comment="数据类型：15分钟、小时、日、月、年"),
		@Column(name="total_no_load", attrName="totalNoLoad", label="空载-总", isUpdateForce=true),
		@Column(name="total_stop", attrName="totalStop", label="停机-总", isUpdateForce=true),
		@Column(name="total_running", attrName="totalRunning", label="运行-总", isUpdateForce=true),
		@Column(name="cusp_no_load_time", attrName="cuspNoLoadTime", label="空载-尖", isUpdateForce=true),
		@Column(name="cusp_stop_time", attrName="cuspStopTime", label="停机-尖", isUpdateForce=true),
		@Column(name="cusp_running_time", attrName="cuspRunningTime", label="运行-尖", isUpdateForce=true),
		@Column(name="peak_no_load_time", attrName="peakNoLoadTime", label="空载-峰", isUpdateForce=true),
		@Column(name="peak_stop_time", attrName="peakStopTime", label="停机-峰", isUpdateForce=true),
		@Column(name="peak_running_time", attrName="peakRunningTime", label="运行-峰", isUpdateForce=true),
		@Column(name="fair_no_load_time", attrName="fairNoLoadTime", label="空载-平", isUpdateForce=true),
		@Column(name="fair_stop_time", attrName="fairStopTime", label="停机-平", isUpdateForce=true),
		@Column(name="fair_running_time", attrName="fairRunningTime", label="运行-平", isUpdateForce=true),
		@Column(name="valley_no_load_time", attrName="valleyNoLoadTime", label="空载-谷", isUpdateForce=true),
		@Column(name="valley_stop_time", attrName="valleyStopTime", label="停机-谷", isUpdateForce=true),
		@Column(name="valley_running_time", attrName="valleyRunningTime", label="运行-谷", isUpdateForce=true),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", queryType=QueryType.LIKE),
		@Column(name = "company_code", attrName = "companyCode", label = "公司编码"),
		@Column(name = "company_name", attrName = "companyName", label = "公司名称", queryType = QueryType.LIKE),
		@Column(includeEntity=BaseEntity.class),
	}, orderBy="a.data_date DESC"
)
@Builder
@AllArgsConstructor
@Data
@ApiModel(value = "EmsTimeShareDeviceRuntime对象", description = "峰平谷设备运行时长表Entity")
public class EmsTimeShareDeviceRuntime extends DataEntity<EmsTimeShareDeviceRuntime> {
	
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "设备ID")
	private String deviceId;
	@ApiModelProperty(value = "设备名称")
	private String deviceName;
	@ApiModelProperty(value = "数据日期， 2023-03-24")
	private Date dataDate;
	@ApiModelProperty(value = "数据类型：15分钟、小时、日、月、年")
	private String dataType;
	@ApiModelProperty(value = "空载-总")
	private Double totalNoLoad;
	@ApiModelProperty(value = "停机-总")
	private Double totalStop;
	@ApiModelProperty(value = "运行-总")
	private Double totalRunning;
	@ApiModelProperty(value = "空载-尖")
	private Double cuspNoLoadTime;
	@ApiModelProperty(value = "停机-尖")
	private Double cuspStopTime;
	@ApiModelProperty(value = "运行-尖")
	private Double cuspRunningTime;
	@ApiModelProperty(value = "空载-峰")
	private Double peakNoLoadTime;
	@ApiModelProperty(value = "停机-峰")
	private Double peakStopTime;
	@ApiModelProperty(value = "运行-峰")
	private Double peakRunningTime;
	@ApiModelProperty(value = "空载-平")
	private Double fairNoLoadTime;
	@ApiModelProperty(value = "停机-平")
	private Double fairStopTime;
	@ApiModelProperty(value = "运行-平")
	private Double fairRunningTime;
	@ApiModelProperty(value = "空载-谷")
	private Double valleyNoLoadTime;
	@ApiModelProperty(value = "停机-谷")
	private Double valleyStopTime;
	@ApiModelProperty(value = "运行-谷")
	private Double valleyRunningTime;
	@ApiModelProperty(value = "公司编码")
	private String companyCode;
	@ApiModelProperty(value = "公司名称")
	private String companyName;

	private Date dataDateStart;
	private Date dataDateEnd;
	@ApiModelProperty(value = "总用电能耗")
	private Double positiveActiveEnergy;
	@ApiModelProperty(value = "总时长")
	private Double totalTime;
	@ExcelFields({
			@ExcelField(title="时间", attrName = "dataDate", align = ExcelField.Align.CENTER, sort = 10, dataFormat="yyyy-MM-dd"),
			@ExcelField(title="设备名称", attrName="deviceName", align = ExcelField.Align.CENTER, sort=20),
			@ExcelField(title="总用电（kwh）", attrName="positiveActiveEnergy", align= ExcelField.Align.CENTER, sort=30),
			@ExcelField(title="总时长（小时）", attrName="totalTime", align= ExcelField.Align.CENTER, sort=40),
			@ExcelField(title="停机时长（小时）", attrName="totalStop", align= ExcelField.Align.CENTER, sort=50),
			@ExcelField(title="空载时长（小时）", attrName="totalNoLoad", align= ExcelField.Align.CENTER, sort=60),
			@ExcelField(title="运行时长（小时）", attrName="totalRunning", align= ExcelField.Align.CENTER, sort=70),
	})
	public EmsTimeShareDeviceRuntime() {
		this(null);
	}
	
	public EmsTimeShareDeviceRuntime(String id){
		super(id);
	}

	public Date getDataDateStart() {
		return dataDateStart;
	}

	public void setDataDateStart(Date dataDateStart) {
		this.dataDateStart = dataDateStart;
	}

	public Date getDataDateEnd() {
		return dataDateEnd;
	}

	public void setDataDateEnd(Date dataDateEnd) {
		this.dataDateEnd = dataDateEnd;
	}

	@NotBlank(message="设备ID不能为空")
	@Size(min=0, max=32, message="设备ID长度不能超过 32 个字符")
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	@NotBlank(message="设备名称不能为空")
	@Size(min=0, max=100, message="设备名称长度不能超过 100 个字符")
	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="数据日期， 2023-03-24不能为空")
	public Date getDataDate() {
		return dataDate;
	}

	public void setDataDate(Date dataDate) {
		this.dataDate = dataDate;
	}
	
	@NotBlank(message="数据类型不能为空")
	@Size(min=0, max=10, message="数据类型长度不能超过 10 个字符")
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public Double getTotalNoLoad() {
		return totalNoLoad;
	}

	public void setTotalNoLoad(Double totalNoLoad) {
		this.totalNoLoad = totalNoLoad;
	}
	
	public Double getTotalStop() {
		return totalStop;
	}

	public void setTotalStop(Double totalStop) {
		this.totalStop = totalStop;
	}
	
	public Double getTotalRunning() {
		return totalRunning;
	}

	public void setTotalRunning(Double totalRunning) {
		this.totalRunning = totalRunning;
	}
	
	public Double getCuspNoLoadTime() {
		return cuspNoLoadTime;
	}

	public void setCuspNoLoadTime(Double cuspNoLoadTime) {
		this.cuspNoLoadTime = cuspNoLoadTime;
	}
	
	public Double getCuspStopTime() {
		return cuspStopTime;
	}

	public void setCuspStopTime(Double cuspStopTime) {
		this.cuspStopTime = cuspStopTime;
	}
	
	public Double getCuspRunningTime() {
		return cuspRunningTime;
	}

	public void setCuspRunningTime(Double cuspRunningTime) {
		this.cuspRunningTime = cuspRunningTime;
	}
	
	public Double getPeakNoLoadTime() {
		return peakNoLoadTime;
	}

	public void setPeakNoLoadTime(Double peakNoLoadTime) {
		this.peakNoLoadTime = peakNoLoadTime;
	}
	
	public Double getPeakStopTime() {
		return peakStopTime;
	}

	public void setPeakStopTime(Double peakStopTime) {
		this.peakStopTime = peakStopTime;
	}
	
	public Double getPeakRunningTime() {
		return peakRunningTime;
	}

	public void setPeakRunningTime(Double peakRunningTime) {
		this.peakRunningTime = peakRunningTime;
	}
	
	public Double getFairNoLoadTime() {
		return fairNoLoadTime;
	}

	public void setFairNoLoadTime(Double fairNoLoadTime) {
		this.fairNoLoadTime = fairNoLoadTime;
	}
	
	public Double getFairStopTime() {
		return fairStopTime;
	}

	public void setFairStopTime(Double fairStopTime) {
		this.fairStopTime = fairStopTime;
	}
	
	public Double getFairRunningTime() {
		return fairRunningTime;
	}

	public void setFairRunningTime(Double fairRunningTime) {
		this.fairRunningTime = fairRunningTime;
	}
	
	public Double getValleyNoLoadTime() {
		return valleyNoLoadTime;
	}

	public void setValleyNoLoadTime(Double valleyNoLoadTime) {
		this.valleyNoLoadTime = valleyNoLoadTime;
	}
	
	public Double getValleyStopTime() {
		return valleyStopTime;
	}

	public void setValleyStopTime(Double valleyStopTime) {
		this.valleyStopTime = valleyStopTime;
	}
	
	public Double getValleyRunningTime() {
		return valleyRunningTime;
	}

	public void setValleyRunningTime(Double valleyRunningTime) {
		this.valleyRunningTime = valleyRunningTime;
	}
	
}