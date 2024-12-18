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
import com.jeesite.modules.ems.entity.EmsTimeShareAreaPowerConsumption;
import com.jeesite.modules.ems.service.EmsTimeShareAreaPowerConsumptionService;

/**
 * 区域峰平谷电耗表数据基础表Controller
 * @author 李鹏
 * @version 2023-07-25
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsTimeShareAreaPowerConsumption")
@Api(value = "区域峰平谷电耗表数据基础表接口", tags = "区域峰平谷电耗表数据基础表")
public class EmsTimeShareAreaPowerConsumptionController extends BaseController {

	@Resource
	private EmsTimeShareAreaPowerConsumptionService emsTimeShareAreaPowerConsumptionService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public EmsTimeShareAreaPowerConsumption get(String id, boolean isNewRecord) {
		return emsTimeShareAreaPowerConsumptionService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ems:emsTimeShareAreaPowerConsumption:view")
	@RequestMapping(value = {"list", ""})
	@ApiOperation(value = "查询列表", notes = "查询列表")
	public String list(EmsTimeShareAreaPowerConsumption emsTimeShareAreaPowerConsumption, Model model) {
		model.addAttribute("emsTimeShareAreaPowerConsumption", emsTimeShareAreaPowerConsumption);
		return "modules/ems/emsTimeShareAreaPowerConsumptionList";
	}
	
	/**
	 * 查询分页列表数据
	 */
	@RequiresPermissions("ems:emsTimeShareAreaPowerConsumption:view")
	@RequestMapping(value = "pageList")
	@ResponseBody
	@ApiOperation(value = "查询分页列表数据", notes = "查询分页列表数据")
	public Page<EmsTimeShareAreaPowerConsumption> pageList(EmsTimeShareAreaPowerConsumption emsTimeShareAreaPowerConsumption, HttpServletRequest request, HttpServletResponse response) {
		emsTimeShareAreaPowerConsumption.setPage(new Page<>(request, response));
		Page<EmsTimeShareAreaPowerConsumption> page = emsTimeShareAreaPowerConsumptionService.findPage(emsTimeShareAreaPowerConsumption);
		return page;
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ems:emsTimeShareAreaPowerConsumption:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	@ApiOperation(value = "查询列表数据", notes = "查询列表数据")
	public List<EmsTimeShareAreaPowerConsumption> listData(EmsTimeShareAreaPowerConsumption emsTimeShareAreaPowerConsumption, HttpServletRequest request, HttpServletResponse response) {
		List<EmsTimeShareAreaPowerConsumption> list = emsTimeShareAreaPowerConsumptionService.findList(emsTimeShareAreaPowerConsumption);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ems:emsTimeShareAreaPowerConsumption:view")
	@RequestMapping(value = "form")
	@ApiOperation(value = "查看编辑表单", notes = "查看编辑表单")
	public String form(EmsTimeShareAreaPowerConsumption emsTimeShareAreaPowerConsumption, Model model) {
		model.addAttribute("emsTimeShareAreaPowerConsumption", emsTimeShareAreaPowerConsumption);
		return "modules/ems/emsTimeShareAreaPowerConsumptionForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("ems:emsTimeShareAreaPowerConsumption:edit")
	@PostMapping(value = "save")
	@ResponseBody
	@ApiOperation(value = "保存数据", notes = "保存数据")
	public String save(@Validated EmsTimeShareAreaPowerConsumption emsTimeShareAreaPowerConsumption) {
		emsTimeShareAreaPowerConsumptionService.save(emsTimeShareAreaPowerConsumption);
		return renderResult(Global.TRUE, text("保存区域峰平谷电耗表数据基础表成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("ems:emsTimeShareAreaPowerConsumption:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	@ApiOperation(value = "删除数据", notes = "删除数据")
	public String delete(EmsTimeShareAreaPowerConsumption emsTimeShareAreaPowerConsumption) {
		emsTimeShareAreaPowerConsumptionService.delete(emsTimeShareAreaPowerConsumption);
		return renderResult(Global.TRUE, text("删除区域峰平谷电耗表数据基础表成功！"));
	}
	
}