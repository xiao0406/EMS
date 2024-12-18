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
import com.jeesite.modules.ems.entity.EmsElectricPowerAreaConsumptionStatistics;
import com.jeesite.modules.ems.service.EmsElectricPowerAreaConsumptionStatisticsService;

/**
 * 区域电耗表数据统计表Controller
 * @author 李鹏
 * @version 2023-07-06
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsElectricPowerAreaConsumptionStatistics")
@Api(value = "区域电耗表数据统计表接口", tags = "区域电耗表数据统计表")
public class EmsElectricPowerAreaConsumptionStatisticsController extends BaseController {

	@Resource
	private EmsElectricPowerAreaConsumptionStatisticsService emsElectricPowerAreaConsumptionStatisticsService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public EmsElectricPowerAreaConsumptionStatistics get(String id, boolean isNewRecord) {
		return emsElectricPowerAreaConsumptionStatisticsService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ems:emsElectricPowerAreaConsumptionStatistics:view")
	@RequestMapping(value = {"list", ""})
	@ApiOperation(value = "查询列表", notes = "查询列表")
	public String list(EmsElectricPowerAreaConsumptionStatistics emsElectricPowerAreaConsumptionStatistics, Model model) {
		model.addAttribute("emsElectricPowerAreaConsumptionStatistics", emsElectricPowerAreaConsumptionStatistics);
		return "modules/ems/emsElectricPowerAreaConsumptionStatisticsList";
	}
	
	/**
	 * 查询分页列表数据
	 */
	@RequiresPermissions("ems:emsElectricPowerAreaConsumptionStatistics:view")
	@RequestMapping(value = "pageList")
	@ResponseBody
	@ApiOperation(value = "查询分页列表数据", notes = "查询分页列表数据")
	public Page<EmsElectricPowerAreaConsumptionStatistics> pageList(EmsElectricPowerAreaConsumptionStatistics emsElectricPowerAreaConsumptionStatistics, HttpServletRequest request, HttpServletResponse response) {
		emsElectricPowerAreaConsumptionStatistics.setPage(new Page<>(request, response));
		Page<EmsElectricPowerAreaConsumptionStatistics> page = emsElectricPowerAreaConsumptionStatisticsService.findPage(emsElectricPowerAreaConsumptionStatistics);
		return page;
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ems:emsElectricPowerAreaConsumptionStatistics:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	@ApiOperation(value = "查询列表数据", notes = "查询列表数据")
	public List<EmsElectricPowerAreaConsumptionStatistics> listData(EmsElectricPowerAreaConsumptionStatistics emsElectricPowerAreaConsumptionStatistics, HttpServletRequest request, HttpServletResponse response) {
		List<EmsElectricPowerAreaConsumptionStatistics> list = emsElectricPowerAreaConsumptionStatisticsService.findList(emsElectricPowerAreaConsumptionStatistics);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ems:emsElectricPowerAreaConsumptionStatistics:view")
	@RequestMapping(value = "form")
	@ApiOperation(value = "查看编辑表单", notes = "查看编辑表单")
	public String form(EmsElectricPowerAreaConsumptionStatistics emsElectricPowerAreaConsumptionStatistics, Model model) {
		model.addAttribute("emsElectricPowerAreaConsumptionStatistics", emsElectricPowerAreaConsumptionStatistics);
		return "modules/ems/emsElectricPowerAreaConsumptionStatisticsForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("ems:emsElectricPowerAreaConsumptionStatistics:edit")
	@PostMapping(value = "save")
	@ResponseBody
	@ApiOperation(value = "保存数据", notes = "保存数据")
	public String save(@Validated EmsElectricPowerAreaConsumptionStatistics emsElectricPowerAreaConsumptionStatistics) {
		emsElectricPowerAreaConsumptionStatisticsService.save(emsElectricPowerAreaConsumptionStatistics);
		return renderResult(Global.TRUE, text("保存区域电耗表数据统计表成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("ems:emsElectricPowerAreaConsumptionStatistics:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	@ApiOperation(value = "删除数据", notes = "删除数据")
	public String delete(EmsElectricPowerAreaConsumptionStatistics emsElectricPowerAreaConsumptionStatistics) {
		emsElectricPowerAreaConsumptionStatisticsService.delete(emsElectricPowerAreaConsumptionStatistics);
		return renderResult(Global.TRUE, text("删除区域电耗表数据统计表成功！"));
	}
	
}