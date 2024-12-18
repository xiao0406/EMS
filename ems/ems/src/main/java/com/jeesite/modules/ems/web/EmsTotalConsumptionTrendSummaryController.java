package com.jeesite.modules.ems.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.entity.EmsTotalConsumptionTrendSummary;
import com.jeesite.modules.ems.service.EmsTotalConsumptionTrendSummaryService;
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
 * 总用电量汇总Controller
 * @author 范富华
 * @version 2024-05-13
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsTotalConsumptionTrendSummary")
@Api(value = "总用电量汇总接口", tags = "总用电量汇总")
public class EmsTotalConsumptionTrendSummaryController extends BaseController {

	@Resource
	private EmsTotalConsumptionTrendSummaryService emsTotalConsumptionTrendSummaryService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public EmsTotalConsumptionTrendSummary get(String id, boolean isNewRecord) {
		return emsTotalConsumptionTrendSummaryService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ems:emsTotalConsumptionTrendSummary:view")
	@RequestMapping(value = {"list", ""})
	@ApiOperation(value = "查询列表", notes = "查询列表")
	public String list(EmsTotalConsumptionTrendSummary emsTotalConsumptionTrendSummary, Model model) {
		model.addAttribute("emsTotalConsumptionTrendSummary", emsTotalConsumptionTrendSummary);
		return "modules/ems/emsTotalConsumptionTrendSummaryList";
	}
	
	/**
	 * 查询分页列表数据
	 */
	@RequiresPermissions("ems:emsTotalConsumptionTrendSummary:view")
	@RequestMapping(value = "pageList")
	@ResponseBody
	@ApiOperation(value = "查询分页列表数据", notes = "查询分页列表数据")
	public Page<EmsTotalConsumptionTrendSummary> pageList(EmsTotalConsumptionTrendSummary emsTotalConsumptionTrendSummary, HttpServletRequest request, HttpServletResponse response) {
		emsTotalConsumptionTrendSummary.setPage(new Page<>(request, response));
		Page<EmsTotalConsumptionTrendSummary> page = emsTotalConsumptionTrendSummaryService.findPage(emsTotalConsumptionTrendSummary);
		return page;
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ems:emsTotalConsumptionTrendSummary:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	@ApiOperation(value = "查询列表数据", notes = "查询列表数据")
	public List<EmsTotalConsumptionTrendSummary> listData(EmsTotalConsumptionTrendSummary emsTotalConsumptionTrendSummary, HttpServletRequest request, HttpServletResponse response) {
		List<EmsTotalConsumptionTrendSummary> list = emsTotalConsumptionTrendSummaryService.findList(emsTotalConsumptionTrendSummary);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ems:emsTotalConsumptionTrendSummary:view")
	@RequestMapping(value = "form")
	@ApiOperation(value = "查看编辑表单", notes = "查看编辑表单")
	public String form(EmsTotalConsumptionTrendSummary emsTotalConsumptionTrendSummary, Model model) {
		model.addAttribute("emsTotalConsumptionTrendSummary", emsTotalConsumptionTrendSummary);
		return "modules/ems/emsTotalConsumptionTrendSummaryForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("ems:emsTotalConsumptionTrendSummary:edit")
	@PostMapping(value = "save")
	@ResponseBody
	@ApiOperation(value = "保存数据", notes = "保存数据")
	public String save(@Validated EmsTotalConsumptionTrendSummary emsTotalConsumptionTrendSummary) {
		emsTotalConsumptionTrendSummaryService.save(emsTotalConsumptionTrendSummary);
		return renderResult(Global.TRUE, text("保存总用电量汇总成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("ems:emsTotalConsumptionTrendSummary:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	@ApiOperation(value = "删除数据", notes = "删除数据")
	public String delete(EmsTotalConsumptionTrendSummary emsTotalConsumptionTrendSummary) {
		emsTotalConsumptionTrendSummaryService.delete(emsTotalConsumptionTrendSummary);
		return renderResult(Global.TRUE, text("删除总用电量汇总成功！"));
	}
	
}