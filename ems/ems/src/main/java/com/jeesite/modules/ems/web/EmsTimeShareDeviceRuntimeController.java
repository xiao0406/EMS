package com.jeesite.modules.ems.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import io.swagger.annotations.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.List;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.entity.EmsTimeShareDeviceRuntime;
import com.jeesite.modules.ems.service.EmsTimeShareDeviceRuntimeService;

/**
 * 峰平谷设备运行时长表Controller
 * @author 李鹏
 * @version 2023-06-27
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsTimeShareDeviceRuntime")
@Api(value = "峰平谷设备运行时长表接口", tags = "峰平谷设备运行时长表")
public class EmsTimeShareDeviceRuntimeController extends BaseController {

	@Resource
	private EmsTimeShareDeviceRuntimeService emsTimeShareDeviceRuntimeService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public EmsTimeShareDeviceRuntime get(String id, boolean isNewRecord) {
		return emsTimeShareDeviceRuntimeService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ems:emsTimeShareDeviceRuntime:view")
	@RequestMapping(value = {"list", ""})
	@ApiOperation(value = "查询列表", notes = "查询列表")
	public String list(EmsTimeShareDeviceRuntime emsTimeShareDeviceRuntime, Model model) {
		model.addAttribute("emsTimeShareDeviceRuntime", emsTimeShareDeviceRuntime);
		return "modules/ems/emsTimeShareDeviceRuntimeList";
	}
	
	/**
	 * 查询分页列表数据
	 */
	@RequiresPermissions("ems:emsTimeShareDeviceRuntime:view")
	@RequestMapping(value = "pageList")
	@ResponseBody
	@ApiOperation(value = "查询分页列表数据", notes = "查询分页列表数据")
	public Page<EmsTimeShareDeviceRuntime> pageList(EmsTimeShareDeviceRuntime emsTimeShareDeviceRuntime, HttpServletRequest request, HttpServletResponse response) {
		emsTimeShareDeviceRuntime.setPage(new Page<>(request, response));
		Page<EmsTimeShareDeviceRuntime> page = emsTimeShareDeviceRuntimeService.findPage(emsTimeShareDeviceRuntime);
		return page;
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ems:emsTimeShareDeviceRuntime:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	@ApiOperation(value = "查询列表数据", notes = "查询列表数据")
	public List<EmsTimeShareDeviceRuntime> listData(EmsTimeShareDeviceRuntime emsTimeShareDeviceRuntime, HttpServletRequest request, HttpServletResponse response) {
		List<EmsTimeShareDeviceRuntime> list = emsTimeShareDeviceRuntimeService.findList(emsTimeShareDeviceRuntime);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ems:emsTimeShareDeviceRuntime:view")
	@RequestMapping(value = "form")
	@ApiOperation(value = "查看编辑表单", notes = "查看编辑表单")
	public String form(EmsTimeShareDeviceRuntime emsTimeShareDeviceRuntime, Model model) {
		model.addAttribute("emsTimeShareDeviceRuntime", emsTimeShareDeviceRuntime);
		return "modules/ems/emsTimeShareDeviceRuntimeForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("ems:emsTimeShareDeviceRuntime:edit")
	@PostMapping(value = "save")
	@ResponseBody
	@ApiOperation(value = "保存数据", notes = "保存数据")
	public String save(@Validated EmsTimeShareDeviceRuntime emsTimeShareDeviceRuntime) {
		emsTimeShareDeviceRuntimeService.save(emsTimeShareDeviceRuntime);
		return renderResult(Global.TRUE, text("保存峰平谷设备运行时长表成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("ems:emsTimeShareDeviceRuntime:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	@ApiOperation(value = "删除数据", notes = "删除数据")
	public String delete(EmsTimeShareDeviceRuntime emsTimeShareDeviceRuntime) {
		emsTimeShareDeviceRuntimeService.delete(emsTimeShareDeviceRuntime);
		return renderResult(Global.TRUE, text("删除峰平谷设备运行时长表成功！"));
	}
	
}