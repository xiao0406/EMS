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
import com.jeesite.modules.ems.entity.EmsMeterYield;
import com.jeesite.modules.ems.service.EmsMeterYieldService;

/**
 * 电表产量表Controller
 * @author 李鹏
 * @version 2023-06-15
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsMeterYield")
@Api(value = "电表产量表接口", tags = "电表产量表")
public class EmsMeterYieldController extends BaseController {

	@Resource
	private EmsMeterYieldService emsMeterYieldService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public EmsMeterYield get(String meterCode, String dataMonth, boolean isNewRecord) {
		EmsMeterYield emsMeterYield = new EmsMeterYield();
		emsMeterYield.setMeterCode(meterCode);
		emsMeterYield.setDataMonth(dataMonth);
		emsMeterYield.setIsNewRecord(isNewRecord);
		return emsMeterYieldService.getAndValid(emsMeterYield);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ems:emsMeterYield:view")
	@RequestMapping(value = {"list", ""})
	@ApiOperation(value = "查询列表", notes = "查询列表")
	public String list(EmsMeterYield emsMeterYield, Model model) {
		model.addAttribute("emsMeterYield", emsMeterYield);
		return "modules/ems/emsMeterYieldList";
	}
	
	/**
	 * 查询分页列表数据
	 */
	@RequiresPermissions("ems:emsMeterYield:view")
	@RequestMapping(value = "pageList")
	@ResponseBody
	@ApiOperation(value = "查询分页列表数据", notes = "查询分页列表数据")
	public Page<EmsMeterYield> pageList(EmsMeterYield emsMeterYield, HttpServletRequest request, HttpServletResponse response) {
		emsMeterYield.setPage(new Page<>(request, response));
		Page<EmsMeterYield> page = emsMeterYieldService.findPage(emsMeterYield);
		return page;
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ems:emsMeterYield:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	@ApiOperation(value = "查询列表数据", notes = "查询列表数据")
	public List<EmsMeterYield> listData(EmsMeterYield emsMeterYield, HttpServletRequest request, HttpServletResponse response) {
		List<EmsMeterYield> list = emsMeterYieldService.findList(emsMeterYield);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ems:emsMeterYield:view")
	@RequestMapping(value = "form")
	@ApiOperation(value = "查看编辑表单", notes = "查看编辑表单")
	public String form(EmsMeterYield emsMeterYield, Model model) {
		model.addAttribute("emsMeterYield", emsMeterYield);
		return "modules/ems/emsMeterYieldForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("ems:emsMeterYield:edit")
	@PostMapping(value = "save")
	@ResponseBody
	@ApiOperation(value = "保存数据", notes = "保存数据")
	public String save(@Validated EmsMeterYield emsMeterYield) {
		emsMeterYieldService.save(emsMeterYield);
		return renderResult(Global.TRUE, text("保存电表产量表成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("ems:emsMeterYield:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	@ApiOperation(value = "删除数据", notes = "删除数据")
	public String delete(EmsMeterYield emsMeterYield) {
		emsMeterYieldService.delete(emsMeterYield);
		return renderResult(Global.TRUE, text("删除电表产量表成功！"));
	}
	
}