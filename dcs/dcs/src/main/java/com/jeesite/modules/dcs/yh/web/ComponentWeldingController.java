package com.jeesite.modules.dcs.yh.web;

import javax.annotation.Resource;

import com.cecec.api.redis.RedisKeyUtil;
import com.cecec.api.enumerate.RunStatus;
import com.cscec.common.imir.dto.state.DeviceStatus;
import com.jeesite.modules.dcs.yh.dto.DeviceCapacityDTO;
import com.cscec.common.imir.vo.ComponentWeldingParam;
import com.jeesite.modules.dcs.yh.entity.YhCwProd;
import com.jeesite.modules.dcs.yh.service.YhCwProdService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jeesite.common.web.BaseController;

import java.util.List;

/**
 * dmp_http_dataController
 * @author ds
 * @version 2023-06-21
 */
@Controller
@RequestMapping(value = "${adminPath}/dcs/componentWelding")
@Api(value = "部件焊接接口", tags = "component_welding")
public class ComponentWeldingController extends BaseController {

	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	@Resource
	private YhCwProdService yhCwProdService;

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


	@RequestMapping(value = "weldParam")
	@ApiOperation(value = "查询焊接参数", notes = "查询焊接参数")
	public Object weldParam(String deviceCode) {
		ComponentWeldingParam param = (ComponentWeldingParam)redisTemplate.opsForValue().get(RedisKeyUtil.weldParam(deviceCode));
		if(param == null){
			param = new ComponentWeldingParam(deviceCode);
		}
		return param;
	}


	@RequestMapping(value = "runTime")
	@ApiOperation(value = "设备运行时间", notes = "设备运行时间")
	public List<YhCwProd> deviceRunTime(String deviceCode) {
		return yhCwProdService.deviceRunTime(deviceCode);
	}

	@RequestMapping(value = "wire")
	@ApiOperation(value = "焊丝用量", notes = "焊丝用量")
	public List<YhCwProd> deviceWire(String deviceCode) {
		return yhCwProdService.deviceWire(deviceCode);
	}


	@RequestMapping(value = "capacity")
	@ApiOperation(value = "设备产量", notes = "设备产量")
	public List<DeviceCapacityDTO> deviceCapacity(String deviceCode) {
		return yhCwProdService.deviceCapacity(deviceCode);
	}


	@RequestMapping(value = "utilization")
	@ApiOperation(value = "设备稼动率", notes = "设备稼动率")
	public YhCwProd deviceUtilization(String deviceCode) {
		return yhCwProdService.deviceUtilization(deviceCode);
	}
}