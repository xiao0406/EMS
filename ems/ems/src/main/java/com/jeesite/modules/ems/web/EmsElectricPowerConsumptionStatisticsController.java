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
import com.jeesite.modules.ems.entity.EmsElectricPowerConsumptionStatistics;
import com.jeesite.modules.ems.service.EmsElectricPowerConsumptionStatisticsService;

/**
 * 电耗表数据统计表Controller
 * @author 李鹏
 * @version 2023-06-19
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsElectricPowerConsumptionStatistics")
@Api(value = "电耗表数据统计表接口", tags = "电耗表数据统计表")
public class EmsElectricPowerConsumptionStatisticsController extends BaseController {

	@Resource
	private EmsElectricPowerConsumptionStatisticsService emsElectricPowerConsumptionStatisticsService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public EmsElectricPowerConsumptionStatistics get(String id, boolean isNewRecord) {
		return emsElectricPowerConsumptionStatisticsService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ems:emsElectricPowerConsumptionStatistics:view")
	@RequestMapping(value = {"list", ""})
	@ApiOperation(value = "查询列表", notes = "查询列表")
	public String list(EmsElectricPowerConsumptionStatistics emsElectricPowerConsumptionStatistics, Model model) {
		model.addAttribute("emsElectricPowerConsumptionStatistics", emsElectricPowerConsumptionStatistics);
		return "modules/ems/emsElectricPowerConsumptionStatisticsList";
	}
	
	/**
	 * 查询分页列表数据
	 */
	@RequiresPermissions("ems:emsElectricPowerConsumptionStatistics:view")
	@RequestMapping(value = "pageList")
	@ResponseBody
	@ApiOperation(value = "查询分页列表数据", notes = "查询分页列表数据")
	public Page<EmsElectricPowerConsumptionStatistics> pageList(EmsElectricPowerConsumptionStatistics emsElectricPowerConsumptionStatistics, HttpServletRequest request, HttpServletResponse response) {
		emsElectricPowerConsumptionStatistics.setPage(new Page<>(request, response));
		Page<EmsElectricPowerConsumptionStatistics> page = emsElectricPowerConsumptionStatisticsService.findPage(emsElectricPowerConsumptionStatistics);
		return page;
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ems:emsElectricPowerConsumptionStatistics:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	@ApiOperation(value = "查询列表数据", notes = "查询列表数据")
	public List<EmsElectricPowerConsumptionStatistics> listData(EmsElectricPowerConsumptionStatistics emsElectricPowerConsumptionStatistics, HttpServletRequest request, HttpServletResponse response) {
		List<EmsElectricPowerConsumptionStatistics> list = emsElectricPowerConsumptionStatisticsService.findList(emsElectricPowerConsumptionStatistics);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ems:emsElectricPowerConsumptionStatistics:view")
	@RequestMapping(value = "form")
	@ApiOperation(value = "查看编辑表单", notes = "查看编辑表单")
	public String form(EmsElectricPowerConsumptionStatistics emsElectricPowerConsumptionStatistics, Model model) {
		model.addAttribute("emsElectricPowerConsumptionStatistics", emsElectricPowerConsumptionStatistics);
		return "modules/ems/emsElectricPowerConsumptionStatisticsForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("ems:emsElectricPowerConsumptionStatistics:edit")
	@PostMapping(value = "save")
	@ResponseBody
	@ApiOperation(value = "保存数据", notes = "保存数据")
	public String save(@Validated EmsElectricPowerConsumptionStatistics emsElectricPowerConsumptionStatistics) {
		emsElectricPowerConsumptionStatisticsService.save(emsElectricPowerConsumptionStatistics);
		return renderResult(Global.TRUE, text("保存电耗表数据统计表成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("ems:emsElectricPowerConsumptionStatistics:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	@ApiOperation(value = "删除数据", notes = "删除数据")
	public String delete(EmsElectricPowerConsumptionStatistics emsElectricPowerConsumptionStatistics) {
		emsElectricPowerConsumptionStatisticsService.delete(emsElectricPowerConsumptionStatistics);
		return renderResult(Global.TRUE, text("删除电耗表数据统计表成功！"));
	}
	
}