package com.jeesite.modules.ems.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.modules.ems.entity.EmsTimeSharePowerConsumption;
import com.jeesite.modules.sys.utils.ExcelExportUtil;
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
import java.io.IOException;
import java.util.List;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.entity.EmsMeterThresholdConfig;
import com.jeesite.modules.ems.service.EmsMeterThresholdConfigService;

/**
 * 电表阈值配置表Controller
 * @author 李鹏
 * @version 2023-07-22
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsMeterThresholdConfig")
@Api(value = "电表阈值配置表接口", tags = "电表阈值配置表")
public class EmsMeterThresholdConfigController extends BaseController {

	@Resource
	private EmsMeterThresholdConfigService emsMeterThresholdConfigService;
	
	/**
	 * 获取数据
	 */
//	@ModelAttribute
	public EmsMeterThresholdConfig get(String meterCode, boolean isNewRecord) {
		return emsMeterThresholdConfigService.get(meterCode, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ems:emsMeterThresholdConfig:view")
	@RequestMapping(value = {"list", ""})
	@ApiOperation(value = "查询列表", notes = "查询列表")
	public String list(EmsMeterThresholdConfig emsMeterThresholdConfig, Model model) {
		model.addAttribute("emsMeterThresholdConfig", emsMeterThresholdConfig);
		return "modules/ems/emsMeterThresholdConfigList";
	}
	
	/**
	 * 查询分页列表数据
	 */
	@RequiresPermissions("ems:emsMeterThresholdConfig:view")
	@RequestMapping(value = "pageList")
	@ResponseBody
	@ApiOperation(value = "查询分页列表数据", notes = "查询分页列表数据")
	public Page<EmsMeterThresholdConfig> pageList(EmsMeterThresholdConfig emsMeterThresholdConfig, HttpServletRequest request, HttpServletResponse response) {
		emsMeterThresholdConfig.setPage(new Page<>(request, response));
		Page<EmsMeterThresholdConfig> page = emsMeterThresholdConfigService.findPage(emsMeterThresholdConfig);
		return page;
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ems:emsMeterThresholdConfig:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	@ApiOperation(value = "查询列表数据", notes = "查询列表数据")
	public List<EmsMeterThresholdConfig> listData(EmsMeterThresholdConfig emsMeterThresholdConfig, HttpServletRequest request, HttpServletResponse response) {
		List<EmsMeterThresholdConfig> list = emsMeterThresholdConfigService.findList(emsMeterThresholdConfig);
		return list;
	}

	/**
	 * 查询列表导出
	 */
	@RequiresPermissions("ems:emsMeterThresholdConfig:view")
	@RequestMapping(value = "listDataExp")
	@ResponseBody
	@ApiOperation(value = "查询列表导出", notes = "查询列表导出")
	public String listDataExp(EmsMeterThresholdConfig emsMeterThresholdConfig, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String presignedUrl = null;
		List<EmsMeterThresholdConfig> list = emsMeterThresholdConfigService.findList(emsMeterThresholdConfig);
		String fileName = "电表阈值配置列表导出" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try (ExcelExport ee = new ExcelExport("电表阈值配置列表导出", EmsMeterThresholdConfig.class)) {
			presignedUrl = ExcelExportUtil.uploadOss(ee.setDataList(list), fileName);
		}
		return renderResult(Global.TRUE, text("成功！"), presignedUrl);
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ems:emsMeterThresholdConfig:view")
	@RequestMapping(value = "form")
	@ApiOperation(value = "查看编辑表单", notes = "查看编辑表单")
	public String form(EmsMeterThresholdConfig emsMeterThresholdConfig, Model model) {
		EmsMeterThresholdConfig stock = this.get(emsMeterThresholdConfig.getMeterCode(), emsMeterThresholdConfig.getIsNewRecord());
		model.addAttribute("emsMeterThresholdConfig", stock);
		return "modules/ems/emsMeterThresholdConfigForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("ems:emsMeterThresholdConfig:edit")
	@PostMapping(value = "save")
	@ResponseBody
	@ApiOperation(value = "保存数据", notes = "保存数据")
	public String save(@Validated EmsMeterThresholdConfig emsMeterThresholdConfig) {
		emsMeterThresholdConfigService.save(emsMeterThresholdConfig);
		return renderResult(Global.TRUE, text("保存电表阈值配置表成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("ems:emsMeterThresholdConfig:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	@ApiOperation(value = "删除数据", notes = "删除数据")
	public String delete(EmsMeterThresholdConfig emsMeterThresholdConfig) {
		emsMeterThresholdConfigService.delete(emsMeterThresholdConfig);
		return renderResult(Global.TRUE, text("删除电表阈值配置表成功！"));
	}
	
}