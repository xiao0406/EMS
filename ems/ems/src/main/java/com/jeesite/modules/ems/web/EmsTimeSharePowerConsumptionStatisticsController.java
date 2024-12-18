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
import com.jeesite.modules.ems.entity.EmsTimeSharePowerConsumptionStatistics;
import com.jeesite.modules.ems.service.EmsTimeSharePowerConsumptionStatisticsService;

/**
 * 峰平谷电耗表数据统计表Controller
 * @author 李鹏
 * @version 2023-06-20
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsTimeSharePowerConsumptionStatistics")
@Api(value = "峰平谷电耗表数据统计表接口", tags = "峰平谷电耗表数据统计表")
public class EmsTimeSharePowerConsumptionStatisticsController extends BaseController {

	@Resource
	private EmsTimeSharePowerConsumptionStatisticsService emsTimeSharePowerConsumptionStatisticsService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public EmsTimeSharePowerConsumptionStatistics get(String id, boolean isNewRecord) {
		return emsTimeSharePowerConsumptionStatisticsService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ems:emsTimeSharePowerConsumptionStatistics:view")
	@RequestMapping(value = {"list", ""})
	@ApiOperation(value = "查询列表", notes = "查询列表")
	public String list(EmsTimeSharePowerConsumptionStatistics emsTimeSharePowerConsumptionStatistics, Model model) {
		model.addAttribute("emsTimeSharePowerConsumptionStatistics", emsTimeSharePowerConsumptionStatistics);
		return "modules/ems/emsTimeSharePowerConsumptionStatisticsList";
	}
	
	/**
	 * 查询分页列表数据
	 */
	@RequiresPermissions("ems:emsTimeSharePowerConsumptionStatistics:view")
	@RequestMapping(value = "pageList")
	@ResponseBody
	@ApiOperation(value = "查询分页列表数据", notes = "查询分页列表数据")
	public Page<EmsTimeSharePowerConsumptionStatistics> pageList(EmsTimeSharePowerConsumptionStatistics emsTimeSharePowerConsumptionStatistics, HttpServletRequest request, HttpServletResponse response) {
		emsTimeSharePowerConsumptionStatistics.setPage(new Page<>(request, response));
		Page<EmsTimeSharePowerConsumptionStatistics> page = emsTimeSharePowerConsumptionStatisticsService.findPage(emsTimeSharePowerConsumptionStatistics);
		return page;
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ems:emsTimeSharePowerConsumptionStatistics:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	@ApiOperation(value = "查询列表数据", notes = "查询列表数据")
	public List<EmsTimeSharePowerConsumptionStatistics> listData(EmsTimeSharePowerConsumptionStatistics emsTimeSharePowerConsumptionStatistics, HttpServletRequest request, HttpServletResponse response) {
		List<EmsTimeSharePowerConsumptionStatistics> list = emsTimeSharePowerConsumptionStatisticsService.findList(emsTimeSharePowerConsumptionStatistics);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ems:emsTimeSharePowerConsumptionStatistics:view")
	@RequestMapping(value = "form")
	@ApiOperation(value = "查看编辑表单", notes = "查看编辑表单")
	public String form(EmsTimeSharePowerConsumptionStatistics emsTimeSharePowerConsumptionStatistics, Model model) {
		model.addAttribute("emsTimeSharePowerConsumptionStatistics", emsTimeSharePowerConsumptionStatistics);
		return "modules/ems/emsTimeSharePowerConsumptionStatisticsForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("ems:emsTimeSharePowerConsumptionStatistics:edit")
	@PostMapping(value = "save")
	@ResponseBody
	@ApiOperation(value = "保存数据", notes = "保存数据")
	public String save(@Validated EmsTimeSharePowerConsumptionStatistics emsTimeSharePowerConsumptionStatistics) {
		emsTimeSharePowerConsumptionStatisticsService.save(emsTimeSharePowerConsumptionStatistics);
		return renderResult(Global.TRUE, text("保存峰平谷电耗表数据统计表成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("ems:emsTimeSharePowerConsumptionStatistics:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	@ApiOperation(value = "删除数据", notes = "删除数据")
	public String delete(EmsTimeSharePowerConsumptionStatistics emsTimeSharePowerConsumptionStatistics) {
		emsTimeSharePowerConsumptionStatisticsService.delete(emsTimeSharePowerConsumptionStatistics);
		return renderResult(Global.TRUE, text("删除峰平谷电耗表数据统计表成功！"));
	}
	
}