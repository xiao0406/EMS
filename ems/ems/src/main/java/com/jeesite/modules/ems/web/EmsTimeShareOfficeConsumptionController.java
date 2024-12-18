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
import com.jeesite.modules.ems.entity.EmsTimeShareOfficeConsumption;
import com.jeesite.modules.ems.service.EmsTimeShareOfficeConsumptionService;

/**
 * 峰平谷部门电耗表数据基础表Controller
 * @author 李鹏
 * @version 2023-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsTimeShareOfficeConsumption")
@Api(value = "峰平谷部门电耗表数据基础表接口", tags = "峰平谷部门电耗表数据基础表")
public class EmsTimeShareOfficeConsumptionController extends BaseController {

	@Resource
	private EmsTimeShareOfficeConsumptionService emsTimeShareOfficeConsumptionService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public EmsTimeShareOfficeConsumption get(String id, boolean isNewRecord) {
		return emsTimeShareOfficeConsumptionService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ems:emsTimeShareOfficeConsumption:view")
	@RequestMapping(value = {"list", ""})
	@ApiOperation(value = "查询列表", notes = "查询列表")
	public String list(EmsTimeShareOfficeConsumption emsTimeShareOfficeConsumption, Model model) {
		model.addAttribute("emsTimeShareOfficeConsumption", emsTimeShareOfficeConsumption);
		return "modules/ems/emsTimeShareOfficeConsumptionList";
	}
	
	/**
	 * 查询分页列表数据
	 */
	@RequiresPermissions("ems:emsTimeShareOfficeConsumption:view")
	@RequestMapping(value = "pageList")
	@ResponseBody
	@ApiOperation(value = "查询分页列表数据", notes = "查询分页列表数据")
	public Page<EmsTimeShareOfficeConsumption> pageList(EmsTimeShareOfficeConsumption emsTimeShareOfficeConsumption, HttpServletRequest request, HttpServletResponse response) {
		emsTimeShareOfficeConsumption.setPage(new Page<>(request, response));
		Page<EmsTimeShareOfficeConsumption> page = emsTimeShareOfficeConsumptionService.findPage(emsTimeShareOfficeConsumption);
		return page;
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ems:emsTimeShareOfficeConsumption:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	@ApiOperation(value = "查询列表数据", notes = "查询列表数据")
	public List<EmsTimeShareOfficeConsumption> listData(EmsTimeShareOfficeConsumption emsTimeShareOfficeConsumption, HttpServletRequest request, HttpServletResponse response) {
		List<EmsTimeShareOfficeConsumption> list = emsTimeShareOfficeConsumptionService.findList(emsTimeShareOfficeConsumption);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ems:emsTimeShareOfficeConsumption:view")
	@RequestMapping(value = "form")
	@ApiOperation(value = "查看编辑表单", notes = "查看编辑表单")
	public String form(EmsTimeShareOfficeConsumption emsTimeShareOfficeConsumption, Model model) {
		model.addAttribute("emsTimeShareOfficeConsumption", emsTimeShareOfficeConsumption);
		return "modules/ems/emsTimeShareOfficeConsumptionForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("ems:emsTimeShareOfficeConsumption:edit")
	@PostMapping(value = "save")
	@ResponseBody
	@ApiOperation(value = "保存数据", notes = "保存数据")
	public String save(@Validated EmsTimeShareOfficeConsumption emsTimeShareOfficeConsumption) {
		emsTimeShareOfficeConsumptionService.save(emsTimeShareOfficeConsumption);
		return renderResult(Global.TRUE, text("保存峰平谷部门电耗表数据基础表成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("ems:emsTimeShareOfficeConsumption:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	@ApiOperation(value = "删除数据", notes = "删除数据")
	public String delete(EmsTimeShareOfficeConsumption emsTimeShareOfficeConsumption) {
		emsTimeShareOfficeConsumptionService.delete(emsTimeShareOfficeConsumption);
		return renderResult(Global.TRUE, text("删除峰平谷部门电耗表数据基础表成功！"));
	}
	
}