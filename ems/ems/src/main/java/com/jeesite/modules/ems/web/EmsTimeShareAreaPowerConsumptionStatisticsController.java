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
import com.jeesite.modules.ems.entity.EmsTimeShareAreaPowerConsumptionStatistics;
import com.jeesite.modules.ems.service.EmsTimeShareAreaPowerConsumptionStatisticsService;

/**
 * 区域峰平谷电耗表数据统计表Controller
 * @author 李鹏
 * @version 2023-07-25
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsTimeShareAreaPowerConsumptionStatistics")
@Api(value = "区域峰平谷电耗表数据统计表接口", tags = "区域峰平谷电耗表数据统计表")
public class EmsTimeShareAreaPowerConsumptionStatisticsController extends BaseController {

	@Resource
	private EmsTimeShareAreaPowerConsumptionStatisticsService emsTimeShareAreaPowerConsumptionStatisticsService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public EmsTimeShareAreaPowerConsumptionStatistics get(String id, boolean isNewRecord) {
		return emsTimeShareAreaPowerConsumptionStatisticsService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ems:emsTimeShareAreaPowerConsumptionStatistics:view")
	@RequestMapping(value = {"list", ""})
	@ApiOperation(value = "查询列表", notes = "查询列表")
	public String list(EmsTimeShareAreaPowerConsumptionStatistics emsTimeShareAreaPowerConsumptionStatistics, Model model) {
		model.addAttribute("emsTimeShareAreaPowerConsumptionStatistics", emsTimeShareAreaPowerConsumptionStatistics);
		return "modules/ems/emsTimeShareAreaPowerConsumptionStatisticsList";
	}
	
	/**
	 * 查询分页列表数据
	 */
	@RequiresPermissions("ems:emsTimeShareAreaPowerConsumptionStatistics:view")
	@RequestMapping(value = "pageList")
	@ResponseBody
	@ApiOperation(value = "查询分页列表数据", notes = "查询分页列表数据")
	public Page<EmsTimeShareAreaPowerConsumptionStatistics> pageList(EmsTimeShareAreaPowerConsumptionStatistics emsTimeShareAreaPowerConsumptionStatistics, HttpServletRequest request, HttpServletResponse response) {
		emsTimeShareAreaPowerConsumptionStatistics.setPage(new Page<>(request, response));
		Page<EmsTimeShareAreaPowerConsumptionStatistics> page = emsTimeShareAreaPowerConsumptionStatisticsService.findPage(emsTimeShareAreaPowerConsumptionStatistics);
		return page;
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ems:emsTimeShareAreaPowerConsumptionStatistics:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	@ApiOperation(value = "查询列表数据", notes = "查询列表数据")
	public List<EmsTimeShareAreaPowerConsumptionStatistics> listData(EmsTimeShareAreaPowerConsumptionStatistics emsTimeShareAreaPowerConsumptionStatistics, HttpServletRequest request, HttpServletResponse response) {
		List<EmsTimeShareAreaPowerConsumptionStatistics> list = emsTimeShareAreaPowerConsumptionStatisticsService.findList(emsTimeShareAreaPowerConsumptionStatistics);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ems:emsTimeShareAreaPowerConsumptionStatistics:view")
	@RequestMapping(value = "form")
	@ApiOperation(value = "查看编辑表单", notes = "查看编辑表单")
	public String form(EmsTimeShareAreaPowerConsumptionStatistics emsTimeShareAreaPowerConsumptionStatistics, Model model) {
		model.addAttribute("emsTimeShareAreaPowerConsumptionStatistics", emsTimeShareAreaPowerConsumptionStatistics);
		return "modules/ems/emsTimeShareAreaPowerConsumptionStatisticsForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("ems:emsTimeShareAreaPowerConsumptionStatistics:edit")
	@PostMapping(value = "save")
	@ResponseBody
	@ApiOperation(value = "保存数据", notes = "保存数据")
	public String save(@Validated EmsTimeShareAreaPowerConsumptionStatistics emsTimeShareAreaPowerConsumptionStatistics) {
		emsTimeShareAreaPowerConsumptionStatisticsService.save(emsTimeShareAreaPowerConsumptionStatistics);
		return renderResult(Global.TRUE, text("保存区域峰平谷电耗表数据统计表成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("ems:emsTimeShareAreaPowerConsumptionStatistics:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	@ApiOperation(value = "删除数据", notes = "删除数据")
	public String delete(EmsTimeShareAreaPowerConsumptionStatistics emsTimeShareAreaPowerConsumptionStatistics) {
		emsTimeShareAreaPowerConsumptionStatisticsService.delete(emsTimeShareAreaPowerConsumptionStatistics);
		return renderResult(Global.TRUE, text("删除区域峰平谷电耗表数据统计表成功！"));
	}
	
}