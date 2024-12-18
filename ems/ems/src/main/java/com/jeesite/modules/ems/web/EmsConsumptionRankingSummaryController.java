package com.jeesite.modules.ems.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.entity.EmsConsumptionRankingSummary;
import com.jeesite.modules.ems.service.EmsConsumptionRankingSummaryService;
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
 * 用电量排行Controller
 * @author 范富华
 * @version 2024-05-14
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsConsumptionRankingSummary")
@Api(value = "用电量排行接口", tags = "用电量排行")
public class EmsConsumptionRankingSummaryController extends BaseController {

	@Resource
	private EmsConsumptionRankingSummaryService emsConsumptionRankingSummaryService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public EmsConsumptionRankingSummary get(String id, boolean isNewRecord) {
		return emsConsumptionRankingSummaryService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ems:emsConsumptionRankingSummary:view")
	@RequestMapping(value = {"list", ""})
	@ApiOperation(value = "查询列表", notes = "查询列表")
	public String list(EmsConsumptionRankingSummary emsConsumptionRankingSummary, Model model) {
		model.addAttribute("emsConsumptionRankingSummary", emsConsumptionRankingSummary);
		return "modules/ems/emsConsumptionRankingSummaryList";
	}
	
	/**
	 * 查询分页列表数据
	 */
	@RequiresPermissions("ems:emsConsumptionRankingSummary:view")
	@RequestMapping(value = "pageList")
	@ResponseBody
	@ApiOperation(value = "查询分页列表数据", notes = "查询分页列表数据")
	public Page<EmsConsumptionRankingSummary> pageList(EmsConsumptionRankingSummary emsConsumptionRankingSummary, HttpServletRequest request, HttpServletResponse response) {
		emsConsumptionRankingSummary.setPage(new Page<>(request, response));
		Page<EmsConsumptionRankingSummary> page = emsConsumptionRankingSummaryService.findPage(emsConsumptionRankingSummary);
		return page;
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ems:emsConsumptionRankingSummary:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	@ApiOperation(value = "查询列表数据", notes = "查询列表数据")
	public List<EmsConsumptionRankingSummary> listData(EmsConsumptionRankingSummary emsConsumptionRankingSummary, HttpServletRequest request, HttpServletResponse response) {
		List<EmsConsumptionRankingSummary> list = emsConsumptionRankingSummaryService.findList(emsConsumptionRankingSummary);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ems:emsConsumptionRankingSummary:view")
	@RequestMapping(value = "form")
	@ApiOperation(value = "查看编辑表单", notes = "查看编辑表单")
	public String form(EmsConsumptionRankingSummary emsConsumptionRankingSummary, Model model) {
		model.addAttribute("emsConsumptionRankingSummary", emsConsumptionRankingSummary);
		return "modules/ems/emsConsumptionRankingSummaryForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("ems:emsConsumptionRankingSummary:edit")
	@PostMapping(value = "save")
	@ResponseBody
	@ApiOperation(value = "保存数据", notes = "保存数据")
	public String save(@Validated EmsConsumptionRankingSummary emsConsumptionRankingSummary) {
		emsConsumptionRankingSummaryService.save(emsConsumptionRankingSummary);
		return renderResult(Global.TRUE, text("保存用电量排行成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("ems:emsConsumptionRankingSummary:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	@ApiOperation(value = "删除数据", notes = "删除数据")
	public String delete(EmsConsumptionRankingSummary emsConsumptionRankingSummary) {
		emsConsumptionRankingSummaryService.delete(emsConsumptionRankingSummary);
		return renderResult(Global.TRUE, text("删除用电量排行成功！"));
	}
	
}