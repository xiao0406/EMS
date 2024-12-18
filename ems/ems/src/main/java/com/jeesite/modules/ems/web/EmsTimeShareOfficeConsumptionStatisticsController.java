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
import com.jeesite.modules.ems.entity.EmsTimeShareOfficeConsumptionStatistics;
import com.jeesite.modules.ems.service.EmsTimeShareOfficeConsumptionStatisticsService;

/**
 * 峰平谷部门电耗表数据统计表Controller
 * @author 李鹏
 * @version 2023-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsTimeShareOfficeConsumptionStatistics")
@Api(value = "峰平谷部门电耗表数据统计表接口", tags = "峰平谷部门电耗表数据统计表")
public class EmsTimeShareOfficeConsumptionStatisticsController extends BaseController {

	@Resource
	private EmsTimeShareOfficeConsumptionStatisticsService emsTimeShareOfficeConsumptionStatisticsService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public EmsTimeShareOfficeConsumptionStatistics get(String id, boolean isNewRecord) {
		return emsTimeShareOfficeConsumptionStatisticsService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ems:emsTimeShareOfficeConsumptionStatistics:view")
	@RequestMapping(value = {"list", ""})
	@ApiOperation(value = "查询列表", notes = "查询列表")
	public String list(EmsTimeShareOfficeConsumptionStatistics emsTimeShareOfficeConsumptionStatistics, Model model) {
		model.addAttribute("emsTimeShareOfficeConsumptionStatistics", emsTimeShareOfficeConsumptionStatistics);
		return "modules/ems/emsTimeShareOfficeConsumptionStatisticsList";
	}
	
	/**
	 * 查询分页列表数据
	 */
	@RequiresPermissions("ems:emsTimeShareOfficeConsumptionStatistics:view")
	@RequestMapping(value = "pageList")
	@ResponseBody
	@ApiOperation(value = "查询分页列表数据", notes = "查询分页列表数据")
	public Page<EmsTimeShareOfficeConsumptionStatistics> pageList(EmsTimeShareOfficeConsumptionStatistics emsTimeShareOfficeConsumptionStatistics, HttpServletRequest request, HttpServletResponse response) {
		emsTimeShareOfficeConsumptionStatistics.setPage(new Page<>(request, response));
		Page<EmsTimeShareOfficeConsumptionStatistics> page = emsTimeShareOfficeConsumptionStatisticsService.findPage(emsTimeShareOfficeConsumptionStatistics);
		return page;
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ems:emsTimeShareOfficeConsumptionStatistics:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	@ApiOperation(value = "查询列表数据", notes = "查询列表数据")
	public List<EmsTimeShareOfficeConsumptionStatistics> listData(EmsTimeShareOfficeConsumptionStatistics emsTimeShareOfficeConsumptionStatistics, HttpServletRequest request, HttpServletResponse response) {
		List<EmsTimeShareOfficeConsumptionStatistics> list = emsTimeShareOfficeConsumptionStatisticsService.findList(emsTimeShareOfficeConsumptionStatistics);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ems:emsTimeShareOfficeConsumptionStatistics:view")
	@RequestMapping(value = "form")
	@ApiOperation(value = "查看编辑表单", notes = "查看编辑表单")
	public String form(EmsTimeShareOfficeConsumptionStatistics emsTimeShareOfficeConsumptionStatistics, Model model) {
		model.addAttribute("emsTimeShareOfficeConsumptionStatistics", emsTimeShareOfficeConsumptionStatistics);
		return "modules/ems/emsTimeShareOfficeConsumptionStatisticsForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("ems:emsTimeShareOfficeConsumptionStatistics:edit")
	@PostMapping(value = "save")
	@ResponseBody
	@ApiOperation(value = "保存数据", notes = "保存数据")
	public String save(@Validated EmsTimeShareOfficeConsumptionStatistics emsTimeShareOfficeConsumptionStatistics) {
		emsTimeShareOfficeConsumptionStatisticsService.save(emsTimeShareOfficeConsumptionStatistics);
		return renderResult(Global.TRUE, text("保存峰平谷部门电耗表数据统计表成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("ems:emsTimeShareOfficeConsumptionStatistics:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	@ApiOperation(value = "删除数据", notes = "删除数据")
	public String delete(EmsTimeShareOfficeConsumptionStatistics emsTimeShareOfficeConsumptionStatistics) {
		emsTimeShareOfficeConsumptionStatisticsService.delete(emsTimeShareOfficeConsumptionStatistics);
		return renderResult(Global.TRUE, text("删除峰平谷部门电耗表数据统计表成功！"));
	}
	
}