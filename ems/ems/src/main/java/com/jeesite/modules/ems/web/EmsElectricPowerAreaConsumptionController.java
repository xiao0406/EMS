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
import com.jeesite.modules.ems.entity.EmsElectricPowerAreaConsumption;
import com.jeesite.modules.ems.service.EmsElectricPowerAreaConsumptionService;

/**
 * 区域电耗表数据基础表Controller
 * @author 李鹏
 * @version 2023-07-06
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsElectricPowerAreaConsumption")
@Api(value = "区域电耗表数据基础表接口", tags = "区域电耗表数据基础表")
public class EmsElectricPowerAreaConsumptionController extends BaseController {

	@Resource
	private EmsElectricPowerAreaConsumptionService emsElectricPowerAreaConsumptionService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public EmsElectricPowerAreaConsumption get(String id, boolean isNewRecord) {
		return emsElectricPowerAreaConsumptionService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ems:emsElectricPowerAreaConsumption:view")
	@RequestMapping(value = {"list", ""})
	@ApiOperation(value = "查询列表", notes = "查询列表")
	public String list(EmsElectricPowerAreaConsumption emsElectricPowerAreaConsumption, Model model) {
		model.addAttribute("emsElectricPowerAreaConsumption", emsElectricPowerAreaConsumption);
		return "modules/ems/emsElectricPowerAreaConsumptionList";
	}
	
	/**
	 * 查询分页列表数据
	 */
	@RequiresPermissions("ems:emsElectricPowerAreaConsumption:view")
	@RequestMapping(value = "pageList")
	@ResponseBody
	@ApiOperation(value = "查询分页列表数据", notes = "查询分页列表数据")
	public Page<EmsElectricPowerAreaConsumption> pageList(EmsElectricPowerAreaConsumption emsElectricPowerAreaConsumption, HttpServletRequest request, HttpServletResponse response) {
		emsElectricPowerAreaConsumption.setPage(new Page<>(request, response));
		Page<EmsElectricPowerAreaConsumption> page = emsElectricPowerAreaConsumptionService.findPage(emsElectricPowerAreaConsumption);
		return page;
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ems:emsElectricPowerAreaConsumption:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	@ApiOperation(value = "查询列表数据", notes = "查询列表数据")
	public List<EmsElectricPowerAreaConsumption> listData(EmsElectricPowerAreaConsumption emsElectricPowerAreaConsumption, HttpServletRequest request, HttpServletResponse response) {
		List<EmsElectricPowerAreaConsumption> list = emsElectricPowerAreaConsumptionService.findList(emsElectricPowerAreaConsumption);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ems:emsElectricPowerAreaConsumption:view")
	@RequestMapping(value = "form")
	@ApiOperation(value = "查看编辑表单", notes = "查看编辑表单")
	public String form(EmsElectricPowerAreaConsumption emsElectricPowerAreaConsumption, Model model) {
		model.addAttribute("emsElectricPowerAreaConsumption", emsElectricPowerAreaConsumption);
		return "modules/ems/emsElectricPowerAreaConsumptionForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("ems:emsElectricPowerAreaConsumption:edit")
	@PostMapping(value = "save")
	@ResponseBody
	@ApiOperation(value = "保存数据", notes = "保存数据")
	public String save(@Validated EmsElectricPowerAreaConsumption emsElectricPowerAreaConsumption) {
		emsElectricPowerAreaConsumptionService.save(emsElectricPowerAreaConsumption);
		return renderResult(Global.TRUE, text("保存区域电耗表数据基础表成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("ems:emsElectricPowerAreaConsumption:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	@ApiOperation(value = "删除数据", notes = "删除数据")
	public String delete(EmsElectricPowerAreaConsumption emsElectricPowerAreaConsumption) {
		emsElectricPowerAreaConsumptionService.delete(emsElectricPowerAreaConsumption);
		return renderResult(Global.TRUE, text("删除区域电耗表数据基础表成功！"));
	}
	
}