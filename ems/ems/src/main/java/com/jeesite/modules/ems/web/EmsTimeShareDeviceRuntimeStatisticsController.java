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
import com.jeesite.modules.ems.entity.EmsTimeShareDeviceRuntimeStatistics;
import com.jeesite.modules.ems.service.EmsTimeShareDeviceRuntimeStatisticsService;

/**
 * 峰平谷设备运行时长统计表Controller
 * @author 李鹏
 * @version 2023-06-27
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsTimeShareDeviceRuntimeStatistics")
@Api(value = "峰平谷设备运行时长统计表接口", tags = "峰平谷设备运行时长统计表")
public class EmsTimeShareDeviceRuntimeStatisticsController extends BaseController {

	@Resource
	private EmsTimeShareDeviceRuntimeStatisticsService emsTimeShareDeviceRuntimeStatisticsService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public EmsTimeShareDeviceRuntimeStatistics get(String id, boolean isNewRecord) {
		return emsTimeShareDeviceRuntimeStatisticsService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ems:emsTimeShareDeviceRuntimeStatistics:view")
	@RequestMapping(value = {"list", ""})
	@ApiOperation(value = "查询列表", notes = "查询列表")
	public String list(EmsTimeShareDeviceRuntimeStatistics emsTimeShareDeviceRuntimeStatistics, Model model) {
		model.addAttribute("emsTimeShareDeviceRuntimeStatistics", emsTimeShareDeviceRuntimeStatistics);
		return "modules/ems/emsTimeShareDeviceRuntimeStatisticsList";
	}
	
	/**
	 * 查询分页列表数据
	 */
	@RequiresPermissions("ems:emsTimeShareDeviceRuntimeStatistics:view")
	@RequestMapping(value = "pageList")
	@ResponseBody
	@ApiOperation(value = "查询分页列表数据", notes = "查询分页列表数据")
	public Page<EmsTimeShareDeviceRuntimeStatistics> pageList(EmsTimeShareDeviceRuntimeStatistics emsTimeShareDeviceRuntimeStatistics, HttpServletRequest request, HttpServletResponse response) {
		emsTimeShareDeviceRuntimeStatistics.setPage(new Page<>(request, response));
		Page<EmsTimeShareDeviceRuntimeStatistics> page = emsTimeShareDeviceRuntimeStatisticsService.findPage(emsTimeShareDeviceRuntimeStatistics);
		return page;
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ems:emsTimeShareDeviceRuntimeStatistics:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	@ApiOperation(value = "查询列表数据", notes = "查询列表数据")
	public List<EmsTimeShareDeviceRuntimeStatistics> listData(EmsTimeShareDeviceRuntimeStatistics emsTimeShareDeviceRuntimeStatistics, HttpServletRequest request, HttpServletResponse response) {
		List<EmsTimeShareDeviceRuntimeStatistics> list = emsTimeShareDeviceRuntimeStatisticsService.findList(emsTimeShareDeviceRuntimeStatistics);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ems:emsTimeShareDeviceRuntimeStatistics:view")
	@RequestMapping(value = "form")
	@ApiOperation(value = "查看编辑表单", notes = "查看编辑表单")
	public String form(EmsTimeShareDeviceRuntimeStatistics emsTimeShareDeviceRuntimeStatistics, Model model) {
		model.addAttribute("emsTimeShareDeviceRuntimeStatistics", emsTimeShareDeviceRuntimeStatistics);
		return "modules/ems/emsTimeShareDeviceRuntimeStatisticsForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("ems:emsTimeShareDeviceRuntimeStatistics:edit")
	@PostMapping(value = "save")
	@ResponseBody
	@ApiOperation(value = "保存数据", notes = "保存数据")
	public String save(@Validated EmsTimeShareDeviceRuntimeStatistics emsTimeShareDeviceRuntimeStatistics) {
		emsTimeShareDeviceRuntimeStatisticsService.save(emsTimeShareDeviceRuntimeStatistics);
		return renderResult(Global.TRUE, text("保存峰平谷设备运行时长统计表成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("ems:emsTimeShareDeviceRuntimeStatistics:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	@ApiOperation(value = "删除数据", notes = "删除数据")
	public String delete(EmsTimeShareDeviceRuntimeStatistics emsTimeShareDeviceRuntimeStatistics) {
		emsTimeShareDeviceRuntimeStatisticsService.delete(emsTimeShareDeviceRuntimeStatistics);
		return renderResult(Global.TRUE, text("删除峰平谷设备运行时长统计表成功！"));
	}
	
}