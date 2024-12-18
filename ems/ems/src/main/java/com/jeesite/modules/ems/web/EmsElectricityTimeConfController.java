package com.jeesite.modules.ems.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.modules.ems.entity.CompanyVo;
import com.jeesite.modules.job.task.ems.timeshare.TimeShareCalculateDayDeviceRunningTimeTask;
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
import java.util.List;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.entity.EmsElectricityTimeConf;
import com.jeesite.modules.ems.service.EmsElectricityTimeConfService;

/**
 * 用电分时配置表Controller
 * @author 李鹏
 * @version 2023-06-15
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsElectricityTimeConf")
@Api(value = "用电分时配置表接口", tags = "用电分时配置表")
public class EmsElectricityTimeConfController extends BaseController {

	@Resource
	private EmsElectricityTimeConfService emsElectricityTimeConfService;

	@Autowired
	private TimeShareCalculateDayDeviceRunningTimeTask timeShareCalculateDayDeviceRunningTimeTask;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public EmsElectricityTimeConf get(String id, boolean isNewRecord) {
		return emsElectricityTimeConfService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ems:emsElectricityTimeConf:view")
	@RequestMapping(value = {"list", ""})
	@ApiOperation(value = "查询列表", notes = "查询列表")
	public String list(EmsElectricityTimeConf emsElectricityTimeConf, Model model) {
		model.addAttribute("emsElectricityTimeConf", emsElectricityTimeConf);
		return "modules/ems/emsElectricityTimeConfList";
	}
	
	/**
	 * 查询分页列表数据
	 */
	@RequiresPermissions("ems:emsElectricityTimeConf:view")
	@RequestMapping(value = "pageList")
	@ResponseBody
	@ApiOperation(value = "查询分页列表数据", notes = "查询分页列表数据")
	public Page<EmsElectricityTimeConf> pageList(EmsElectricityTimeConf emsElectricityTimeConf, HttpServletRequest request, HttpServletResponse response) {
		emsElectricityTimeConf.setPage(new Page<>(request, response));
		Page<EmsElectricityTimeConf> page = emsElectricityTimeConfService.findPage(emsElectricityTimeConf);
		return page;
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ems:emsElectricityTimeConf:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	@ApiOperation(value = "查询列表数据", notes = "查询列表数据")
	public List<EmsElectricityTimeConf> listData(EmsElectricityTimeConf emsElectricityTimeConf, HttpServletRequest request, HttpServletResponse response) {
		List<EmsElectricityTimeConf> list = emsElectricityTimeConfService.findList(emsElectricityTimeConf);
		return list;
	}

	/**
	 * 查询数据
	 */
	@RequiresPermissions("ems:emsElectricityTimeConf:view")
	@RequestMapping(value = "getData")
	@ResponseBody
	@ApiOperation(value = "查询列表数据", notes = "查询列表数据")
	public EmsElectricityTimeConf getData(CompanyVo vo, HttpServletRequest request, HttpServletResponse response) {
		return  emsElectricityTimeConfService.getData(vo);
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ems:emsElectricityTimeConf:view")
	@RequestMapping(value = "form")
	@ApiOperation(value = "查看编辑表单", notes = "查看编辑表单")
	public String form(EmsElectricityTimeConf emsElectricityTimeConf, Model model) {
		model.addAttribute("emsElectricityTimeConf", emsElectricityTimeConf);
		return "modules/ems/emsElectricityTimeConfForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("ems:emsElectricityTimeConf:edit")
	@PostMapping(value = "save")
	@ResponseBody
	@ApiOperation(value = "保存数据", notes = "保存数据")
	public String save(@Validated EmsElectricityTimeConf emsElectricityTimeConf) {
		emsElectricityTimeConfService.save(emsElectricityTimeConf);
		// 重新统计当天设备的尖峰平谷运行时长
		timeShareCalculateDayDeviceRunningTimeTask.updateThresholdReCalculate();
		return renderResult(Global.TRUE, text("保存用电分时配置表成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("ems:emsElectricityTimeConf:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	@ApiOperation(value = "删除数据", notes = "删除数据")
	public String delete(EmsElectricityTimeConf emsElectricityTimeConf) {
		emsElectricityTimeConfService.delete(emsElectricityTimeConf);
		return renderResult(Global.TRUE, text("删除用电分时配置表成功！"));
	}
	
}