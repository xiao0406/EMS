package com.jeesite.modules.ems.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.entity.EmsTimeShareConsumptionTrendSummary;
import com.jeesite.modules.ems.service.EmsTimeShareConsumptionTrendSummaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 尖峰平谷用电趋势汇总Controller
 * @author 范富华
 * @version 2024-05-17
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsTimeShareConsumptionTrendSummary")
@Api(value = "尖峰平谷用电趋势汇总接口", tags = "尖峰平谷用电趋势汇总")
public class EmsTimeShareConsumptionTrendSummaryController extends BaseController {

	@Resource
	private EmsTimeShareConsumptionTrendSummaryService emsTimeShareConsumptionTrendSummaryService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public EmsTimeShareConsumptionTrendSummary get(String id, boolean isNewRecord) {
		return emsTimeShareConsumptionTrendSummaryService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ems:emsTimeShareConsumptionTrendSummary:view")
	@RequestMapping(value = {"list", ""})
	@ApiOperation(value = "查询列表", notes = "查询列表")
	public String list(EmsTimeShareConsumptionTrendSummary emsTimeShareConsumptionTrendSummary, Model model) {
		model.addAttribute("emsTimeShareConsumptionTrendSummary", emsTimeShareConsumptionTrendSummary);
		return "modules/ems/emsTimeShareConsumptionTrendSummaryList";
	}
	
	/**
	 * 查询分页列表数据
	 */
	@RequiresPermissions("ems:emsTimeShareConsumptionTrendSummary:view")
	@RequestMapping(value = "pageList")
	@ResponseBody
	@ApiOperation(value = "查询分页列表数据", notes = "查询分页列表数据")
	public Page<EmsTimeShareConsumptionTrendSummary> pageList(EmsTimeShareConsumptionTrendSummary emsTimeShareConsumptionTrendSummary, HttpServletRequest request, HttpServletResponse response) {
		emsTimeShareConsumptionTrendSummary.setPage(new Page<>(request, response));
		Page<EmsTimeShareConsumptionTrendSummary> page = emsTimeShareConsumptionTrendSummaryService.findPage(emsTimeShareConsumptionTrendSummary);
		return page;
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ems:emsTimeShareConsumptionTrendSummary:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	@ApiOperation(value = "查询列表数据", notes = "查询列表数据")
	public List<EmsTimeShareConsumptionTrendSummary> listData(EmsTimeShareConsumptionTrendSummary emsTimeShareConsumptionTrendSummary, HttpServletRequest request, HttpServletResponse response) {
		List<EmsTimeShareConsumptionTrendSummary> list = emsTimeShareConsumptionTrendSummaryService.findList(emsTimeShareConsumptionTrendSummary);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ems:emsTimeShareConsumptionTrendSummary:view")
	@RequestMapping(value = "form")
	@ApiOperation(value = "查看编辑表单", notes = "查看编辑表单")
	public String form(EmsTimeShareConsumptionTrendSummary emsTimeShareConsumptionTrendSummary, Model model) {
		model.addAttribute("emsTimeShareConsumptionTrendSummary", emsTimeShareConsumptionTrendSummary);
		return "modules/ems/emsTimeShareConsumptionTrendSummaryForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("ems:emsTimeShareConsumptionTrendSummary:edit")
	@PostMapping(value = "save")
	@ResponseBody
	@ApiOperation(value = "保存数据", notes = "保存数据")
	public String save(@Validated EmsTimeShareConsumptionTrendSummary emsTimeShareConsumptionTrendSummary) {
		emsTimeShareConsumptionTrendSummaryService.save(emsTimeShareConsumptionTrendSummary);
		return renderResult(Global.TRUE, text("保存尖峰平谷用电趋势汇总成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("ems:emsTimeShareConsumptionTrendSummary:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	@ApiOperation(value = "删除数据", notes = "删除数据")
	public String delete(EmsTimeShareConsumptionTrendSummary emsTimeShareConsumptionTrendSummary) {
		emsTimeShareConsumptionTrendSummaryService.delete(emsTimeShareConsumptionTrendSummary);
		return renderResult(Global.TRUE, text("删除尖峰平谷用电趋势汇总成功！"));
	}
	
}