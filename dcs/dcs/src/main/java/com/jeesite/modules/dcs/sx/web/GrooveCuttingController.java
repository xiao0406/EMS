package com.jeesite.modules.dcs.sx.web;

import com.cecec.api.redis.RedisKeyUtil;
import com.cscec.common.imir.dto.state.DeviceStatus;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.dcs.base.entity.DeviceAlarm;
import com.cecec.api.enumerate.RunStatus;
import com.jeesite.modules.dcs.base.service.DeviceAlarmService;
import com.jeesite.modules.dcs.sx.dto.RunRecordDTO;
import com.jeesite.modules.dcs.sx.dto.WorkEfficiencyDTO;
import com.jeesite.modules.dcs.sx.dto.YieldDTO;
import com.jeesite.modules.dcs.sx.entity.SxGcProd;
import com.jeesite.modules.dcs.sx.service.SxGcProdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * dmp_http_dataController
 * @author ds
 * @version 2023-06-21
 */
@Controller
@RequestMapping(value = "${adminPath}/dcs/grooveCutting")
@Api(value = "坡口切割接口", tags = "groove_cutting")
public class GrooveCuttingController extends BaseController {

	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	@Resource
	private SxGcProdService sxGcProdService;
	@Resource
	private DeviceAlarmService deviceAlarmService;

	/**
	 * 查询设备状态
	 */
	@RequestMapping(value = "deviceStatus")
	@ApiOperation(value = "查询设备状态", notes = "查询设备状态")
	public Object deviceStatus(String deviceCode) {
		Object o = redisTemplate.opsForValue().get(RedisKeyUtil.deviceStatus(deviceCode));
		if(o != null){
			return o;
		}else {
			return new DeviceStatus<>(deviceCode, RunStatus.Close.getIndex());
		}
	}

	/**
	 * 运行数据报表
	 */
	@RequestMapping(value = "runRecord")
	@ApiOperation(value = "运行数据报表", notes = "运行数据报表")
	public List<RunRecordDTO> deviceRunRecord(String deviceCode) {
		return sxGcProdService.deviceRunRecord(deviceCode);
	}

	/**
	 * 维修记录报表
	 */
	@RequestMapping(value = "repairRecord")
	@ApiOperation(value = "维修记录报表", notes = "维修记录报表")
	public List<DeviceAlarm> deviceRepairRecord(String deviceCode) {
		return deviceAlarmService.findDeviceAlarm(deviceCode);
	}

	/**
	 * 运行时间统计图
	 */
	@RequestMapping(value = "runTime")
	@ApiOperation(value = "运行时间统计图表", notes = "运行时间统计图表")
	public List<SxGcProd> deviceRunTime(String deviceCode) {
		return sxGcProdService.deviceRunTime(deviceCode);
	}


	/**
	 *  稼动率图表
	 */
	@RequestMapping(value = "utilization")
	@ApiOperation(value = "稼动率图表", notes = "稼动率图表")
	public SxGcProd deviceUtilization(String deviceCode) {
		return sxGcProdService.deviceUtilization(deviceCode);
	}

	/**
	 * 工效统计图
	 */
	@RequestMapping(value = "workEfficiency")
	@ApiOperation(value = "工效统计图", notes = "工效统计图")
	public List<WorkEfficiencyDTO> deviceWorkEfficiency(String deviceCode) {
		return sxGcProdService.deviceWorkEfficiency(deviceCode);
	}

	/**
	 * 切割米数折线图
	 */
	@RequestMapping(value = "cuttingMeters")
	@ApiOperation(value = "切割米数折线图", notes = "切割米数折线图")
	public List<SxGcProd> deviceCuttingMeters(String deviceCode) {
		return sxGcProdService.deviceCuttingMeters(deviceCode);
	}


	/**
	 * 产量统计图
	 */
	@RequestMapping(value = "deviceYield")
	@ApiOperation(value = "产量统计图", notes = "产量统计图")
	public List<YieldDTO> deviceYield(String deviceCode) {
		return sxGcProdService.deviceYield(deviceCode);
	}
}