package com.jeesite.modules.ems.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.entity.EmsElectricityConsumptionSummar;
import com.jeesite.modules.ems.service.EmsElectricityConsumptionSummarService;
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
 * 用电量汇总Controller
 * @author 范富华
 * @version 2024-05-13
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsElectricityConsumptionSummar")
@Api(value = "用电量汇总接口", tags = "用电量汇总")
public class EmsElectricityConsumptionSummarController extends BaseController {

	@Resource
	private EmsElectricityConsumptionSummarService emsElectricityConsumptionSummarService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public EmsElectricityConsumptionSummar get(String id, boolean isNewRecord) {
		return emsElectricityConsumptionSummarService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ems:emsElectricityConsumptionSummar:view")
	@RequestMapping(value = {"list", ""})
	@ApiOperation(value = "查询列表", notes = "查询列表")
	public String list(EmsElectricityConsumptionSummar emsElectricityConsumptionSummar, Model model) {
		model.addAttribute("emsElectricityConsumptionSummar", emsElectricityConsumptionSummar);
		return "modules/ems/emsElectricityConsumptionSummarList";
	}
	
	/**
	 * 查询分页列表数据
	 */
	@RequiresPermissions("ems:emsElectricityConsumptionSummar:view")
	@RequestMapping(value = "pageList")
	@ResponseBody
	@ApiOperation(value = "查询分页列表数据", notes = "查询分页列表数据")
	public Page<EmsElectricityConsumptionSummar> pageList(EmsElectricityConsumptionSummar emsElectricityConsumptionSummar, HttpServletRequest request, HttpServletResponse response) {
		emsElectricityConsumptionSummar.setPage(new Page<>(request, response));
		Page<EmsElectricityConsumptionSummar> page = emsElectricityConsumptionSummarService.findPage(emsElectricityConsumptionSummar);
		return page;
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ems:emsElectricityConsumptionSummar:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	@ApiOperation(value = "查询列表数据", notes = "查询列表数据")
	public List<EmsElectricityConsumptionSummar> listData(EmsElectricityConsumptionSummar emsElectricityConsumptionSummar, HttpServletRequest request, HttpServletResponse response) {
		List<EmsElectricityConsumptionSummar> list = emsElectricityConsumptionSummarService.findList(emsElectricityConsumptionSummar);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ems:emsElectricityConsumptionSummar:view")
	@RequestMapping(value = "form")
	@ApiOperation(value = "查看编辑表单", notes = "查看编辑表单")
	public String form(EmsElectricityConsumptionSummar emsElectricityConsumptionSummar, Model model) {
		model.addAttribute("emsElectricityConsumptionSummar", emsElectricityConsumptionSummar);
		return "modules/ems/emsElectricityConsumptionSummarForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("ems:emsElectricityConsumptionSummar:edit")
	@PostMapping(value = "save")
	@ResponseBody
	@ApiOperation(value = "保存数据", notes = "保存数据")
	public String save(@Validated EmsElectricityConsumptionSummar emsElectricityConsumptionSummar) {
		emsElectricityConsumptionSummarService.save(emsElectricityConsumptionSummar);
		return renderResult(Global.TRUE, text("保存用电量汇总成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("ems:emsElectricityConsumptionSummar:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	@ApiOperation(value = "删除数据", notes = "删除数据")
	public String delete(EmsElectricityConsumptionSummar emsElectricityConsumptionSummar) {
		emsElectricityConsumptionSummarService.delete(emsElectricityConsumptionSummar);
		return renderResult(Global.TRUE, text("删除用电量汇总成功！"));
	}
	
}