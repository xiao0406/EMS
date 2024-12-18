package com.jeesite.modules.ems.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.modules.ems.entity.*;
import com.jeesite.modules.ems.entity.enums.EnergyGroupEnum;
import com.jeesite.modules.sys.utils.ExcelExportUtil;
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
import java.io.IOException;
import java.util.List;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.service.EmsTimeSharePowerConsumptionService;

/**
 * 峰平谷电耗表数据基础表Controller
 * @author 李鹏
 * @version 2023-06-20
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsTimeSharePowerConsumption")
@Api(value = "峰平谷电耗表数据基础表接口", tags = "峰平谷电耗表数据基础表")
public class EmsTimeSharePowerConsumptionController extends BaseController {

	@Resource
	private EmsTimeSharePowerConsumptionService emsTimeSharePowerConsumptionService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public EmsTimeSharePowerConsumption get(String id, boolean isNewRecord) {
		return emsTimeSharePowerConsumptionService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ems:emsTimeSharePowerConsumption:view")
	@RequestMapping(value = {"list", ""})
	@ApiOperation(value = "查询列表", notes = "查询列表")
	public String list(EmsTimeSharePowerConsumption emsTimeSharePowerConsumption, Model model) {
		model.addAttribute("emsTimeSharePowerConsumption", emsTimeSharePowerConsumption);
		return "modules/ems/emsTimeSharePowerConsumptionList";
	}

	/**
	 * 汇总查询
	 */
	@RequiresPermissions("ems:emsTimeSharePowerConsumption:view")
	@RequestMapping(value = "timeShareStatistics")
	@ResponseBody
	@ApiOperation(value = "汇总查询", notes = "汇总查询")
	public TimeShareStatisticsEntity timeShareStatistics(TimeSharePowerQueryEntity timeSharePowerQueryEntity, HttpServletRequest request, HttpServletResponse response) {
		return emsTimeSharePowerConsumptionService.timeShareStatistics(timeSharePowerQueryEntity);
	}

	/**
	 * 分时用电echart
	 *
	 * @param timeSharePowerQueryEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "timeShareEChart")
	@ResponseBody
	@ApiOperation(value = "分时用电echart", notes = "分时用电echart")
	public EChart timeShareEChart(TimeSharePowerQueryEntity timeSharePowerQueryEntity, HttpServletRequest request, HttpServletResponse response) {
		return emsTimeSharePowerConsumptionService.timeShareEChart(timeSharePowerQueryEntity);
	}
	
	/**
	 * 查询分页列表数据
	 */
	@RequiresPermissions("ems:emsTimeSharePowerConsumption:view")
	@RequestMapping(value = "pageList")
	@ResponseBody
	@ApiOperation(value = "查询分页列表数据", notes = "查询分页列表数据")
	public Page<TimeSharePowerConsumptionEntity> pageList(TimeSharePowerQueryEntity timeSharePowerQueryEntity, HttpServletRequest request, HttpServletResponse response) {
		Page<TimeSharePowerConsumptionEntity> page = emsTimeSharePowerConsumptionService.findTimeSharePage(timeSharePowerQueryEntity, request, response);
		return page;
	}

	/**
	 * 设备峰平谷用电列表导出
	 */
	@RequiresPermissions("ems:emsTimeSharePowerConsumption:view")
	@RequestMapping(value = "listDataExp")
	@ResponseBody
	@ApiOperation(value = "设备峰平谷用电列表导出", notes = "设备峰平谷用电列表导出")
	public String listDataExp(TimeSharePowerQueryEntity timeSharePowerQueryEntity, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String presignedUrl = null;
		List<TimeSharePowerConsumptionEntity> list = emsTimeSharePowerConsumptionService.findTimeShareList(timeSharePowerQueryEntity);
		String fileName = "设备峰平谷用电列表导出" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try (ExcelExport ee = new ExcelExport("设备峰平谷用电列表导出", TimeSharePowerConsumptionEntity.class)) {
			presignedUrl = ExcelExportUtil.uploadOss(ee.setDataList(list), fileName);
		}
		return presignedUrl;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ems:emsTimeSharePowerConsumption:view")
	@RequestMapping(value = "form")
	@ApiOperation(value = "查看编辑表单", notes = "查看编辑表单")
	public String form(EmsTimeSharePowerConsumption emsTimeSharePowerConsumption, Model model) {
		model.addAttribute("emsTimeSharePowerConsumption", emsTimeSharePowerConsumption);
		return "modules/ems/emsTimeSharePowerConsumptionForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("ems:emsTimeSharePowerConsumption:edit")
	@PostMapping(value = "save")
	@ResponseBody
	@ApiOperation(value = "保存数据", notes = "保存数据")
	public String save(@Validated EmsTimeSharePowerConsumption emsTimeSharePowerConsumption) {
		emsTimeSharePowerConsumptionService.save(emsTimeSharePowerConsumption);
		return renderResult(Global.TRUE, text("保存峰平谷电耗表数据基础表成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("ems:emsTimeSharePowerConsumption:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	@ApiOperation(value = "删除数据", notes = "删除数据")
	public String delete(EmsTimeSharePowerConsumption emsTimeSharePowerConsumption) {
		emsTimeSharePowerConsumptionService.delete(emsTimeSharePowerConsumption);
		return renderResult(Global.TRUE, text("删除峰平谷电耗表数据基础表成功！"));
	}
	
}