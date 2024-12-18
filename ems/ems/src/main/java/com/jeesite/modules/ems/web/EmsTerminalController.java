package com.jeesite.modules.ems.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import io.swagger.annotations.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.entity.EmsTerminal;
import com.jeesite.modules.ems.service.EmsTerminalService;

import java.util.List;

/**
 * 终端表Controller
 * @author 李鹏
 * @version 2023-06-14
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsTerminal")
@Api(value = "终端表接口", tags = "终端表")
public class EmsTerminalController extends BaseController {

	@Resource
	private EmsTerminalService emsTerminalService;
	
	/**
	 * 获取数据
	 */
//	@ModelAttribute
	public EmsTerminal get(String terminalCode, boolean isNewRecord) {
		return emsTerminalService.get(terminalCode, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ems:emsTerminal:view")
	@RequestMapping(value = {"list", ""})
	@ApiOperation(value = "查询列表", notes = "查询列表")
	public String list(EmsTerminal emsTerminal, Model model) {
		model.addAttribute("emsTerminal", emsTerminal);
		return "modules/ems/emsTerminalList";
	}
	
	/**
	 * 查询分页列表数据
	 */
	@RequiresPermissions("ems:emsTerminal:view")
	@RequestMapping(value = "pageList")
	@ResponseBody
	@ApiOperation(value = "查询分页列表数据", notes = "查询分页列表数据")
	public Page<EmsTerminal> pageList(EmsTerminal emsTerminal, HttpServletRequest request, HttpServletResponse response) {
		logger.info("--------查询终端分页列表数据-------");
		emsTerminal.setPage(new Page<>(request, response));
		Page<EmsTerminal> page = emsTerminalService.findPage(emsTerminal);
		return page;
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ems:emsTerminal:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	@ApiOperation(value = "查询列表数据", notes = "查询列表数据")
	public List<EmsTerminal> listData(EmsTerminal emsTerminal, HttpServletRequest request, HttpServletResponse response) {
		List<EmsTerminal> list = emsTerminalService.findList(emsTerminal);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ems:emsTerminal:view")
	@RequestMapping(value = "form")
	@ApiOperation(value = "查看编辑表单", notes = "查看编辑表单")
	public String form(EmsTerminal emsTerminal, Model model) {
		emsTerminal = emsTerminalService.get(emsTerminal.getTerminalCode(), emsTerminal.getIsNewRecord());
		model.addAttribute("emsTerminal", emsTerminal);
		return "modules/ems/emsTerminalForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("ems:emsTerminal:edit")
	@PostMapping(value = "save")
	@ResponseBody
	@ApiOperation(value = "保存数据", notes = "保存数据")
	public String save(@Validated EmsTerminal emsTerminal) {
		emsTerminalService.save(emsTerminal);
		return renderResult(Global.TRUE, text("保存终端表成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("ems:emsTerminal:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	@ApiOperation(value = "删除数据", notes = "删除数据")
	public String delete(EmsTerminal emsTerminal) {
		emsTerminalService.delete(emsTerminal);
		return renderResult(Global.TRUE, text("删除终端表成功！"));
	}
	
}