package com.jeesite.modules.ems.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import java.util.List;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.entity.EmsEquipmentOnlineSummary;
import com.jeesite.modules.ems.service.EmsEquipmentOnlineSummaryService;

/**
 * 在线设备统计Controller
 * @author 范富华
 * @version 2024-05-29
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsEquipmentOnlineSummary")
@Api(value = "在线设备统计接口", tags = "在线设备统计")
public class EmsEquipmentOnlineSummaryController extends BaseController {

	@Resource
	private EmsEquipmentOnlineSummaryService emsEquipmentOnlineSummaryService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public EmsEquipmentOnlineSummary get(String id, boolean isNewRecord) {
		return emsEquipmentOnlineSummaryService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ems:emsEquipmentOnlineSummary:view")
	@RequestMapping(value = {"list", ""})
	@ApiOperation(value = "查询列表", notes = "查询列表")
	public String list(EmsEquipmentOnlineSummary emsEquipmentOnlineSummary, Model model) {
		model.addAttribute("emsEquipmentOnlineSummary", emsEquipmentOnlineSummary);
		return "modules/ems/emsEquipmentOnlineSummaryList";
	}
	
	/**
	 * 查询分页列表数据
	 */
	@RequiresPermissions("ems:emsEquipmentOnlineSummary:view")
	@RequestMapping(value = "pageList")
	@ResponseBody
	@ApiOperation(value = "查询分页列表数据", notes = "查询分页列表数据")
	public Page<EmsEquipmentOnlineSummary> pageList(EmsEquipmentOnlineSummary emsEquipmentOnlineSummary, HttpServletRequest request, HttpServletResponse response) {
		emsEquipmentOnlineSummary.setPage(new Page<>(request, response));
		Page<EmsEquipmentOnlineSummary> page = emsEquipmentOnlineSummaryService.findPage(emsEquipmentOnlineSummary);
		return page;
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ems:emsEquipmentOnlineSummary:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	@ApiOperation(value = "查询列表数据", notes = "查询列表数据")
	public List<EmsEquipmentOnlineSummary> listData(EmsEquipmentOnlineSummary emsEquipmentOnlineSummary, HttpServletRequest request, HttpServletResponse response) {
		List<EmsEquipmentOnlineSummary> list = emsEquipmentOnlineSummaryService.findList(emsEquipmentOnlineSummary);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ems:emsEquipmentOnlineSummary:view")
	@RequestMapping(value = "form")
	@ApiOperation(value = "查看编辑表单", notes = "查看编辑表单")
	public String form(EmsEquipmentOnlineSummary emsEquipmentOnlineSummary, Model model) {
		model.addAttribute("emsEquipmentOnlineSummary", emsEquipmentOnlineSummary);
		return "modules/ems/emsEquipmentOnlineSummaryForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("ems:emsEquipmentOnlineSummary:edit")
	@PostMapping(value = "save")
	@ResponseBody
	@ApiOperation(value = "保存数据", notes = "保存数据")
	public String save(@Validated EmsEquipmentOnlineSummary emsEquipmentOnlineSummary) {
		emsEquipmentOnlineSummaryService.save(emsEquipmentOnlineSummary);
		return renderResult(Global.TRUE, text("保存在线设备统计成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("ems:emsEquipmentOnlineSummary:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	@ApiOperation(value = "删除数据", notes = "删除数据")
	public String delete(EmsEquipmentOnlineSummary emsEquipmentOnlineSummary) {
		emsEquipmentOnlineSummaryService.delete(emsEquipmentOnlineSummary);
		return renderResult(Global.TRUE, text("删除在线设备统计成功！"));
	}
	
}