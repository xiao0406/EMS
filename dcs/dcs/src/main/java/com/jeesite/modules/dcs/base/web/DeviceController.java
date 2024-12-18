package com.jeesite.modules.dcs.base.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.modules.dcs.base.dto.DeviceDTO;
import org.springframework.stereotype.Controller;
import io.swagger.annotations.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.dcs.base.entity.Device;
import com.jeesite.modules.dcs.base.service.DeviceService;

import java.util.List;


/**
 * 设备信息Controller
 * @author ds
 * @version 2023-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/dcs/device")
@Api(value = "设备信息接口", tags = "设备信息")
public class DeviceController extends BaseController {

	@Resource
	private DeviceService deviceService;

	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	@ApiOperation(value = "查询列表数据", notes = "查询列表数据")
	public List<DeviceDTO> listData(DeviceDTO device) {
		return deviceService.findDeviceList(device);
	}

	/**
	 * 保存数据
	 */
	@PostMapping(value = "save")
	@ResponseBody
	@ApiOperation(value = "保存数据", notes = "保存数据")
	public String save(@Validated Device device) {
		deviceService.save(device);
		return renderResult(Global.TRUE, text("保存设备信息成功！"));
	}

	/**
	 * 删除数据
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	@ApiOperation(value = "删除数据", notes = "删除数据")
	public String delete(Device device) {
		deviceService.delete(device);
		return renderResult(Global.TRUE, text("删除设备信息成功！"));
	}
}