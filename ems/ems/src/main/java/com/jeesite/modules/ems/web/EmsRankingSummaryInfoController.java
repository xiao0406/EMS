package com.jeesite.modules.ems.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.entity.EmsRankingSummaryInfo;
import com.jeesite.modules.ems.service.EmsRankingSummaryInfoService;
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
 * 耗电量排行关联表Controller
 * @author 范富华
 * @version 2024-05-15
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsRankingSummaryInfo")
@Api(value = "耗电量排行关联表接口", tags = "耗电量排行关联表")
public class EmsRankingSummaryInfoController extends BaseController {

	@Resource
	private EmsRankingSummaryInfoService emsRankingSummaryInfoService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public EmsRankingSummaryInfo get(String id, boolean isNewRecord) {
		return emsRankingSummaryInfoService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ems:emsRankingSummaryInfo:view")
	@RequestMapping(value = {"list", ""})
	@ApiOperation(value = "查询列表", notes = "查询列表")
	public String list(EmsRankingSummaryInfo emsRankingSummaryInfo, Model model) {
		model.addAttribute("emsRankingSummaryInfo", emsRankingSummaryInfo);
		return "modules/ems/emsRankingSummaryInfoList";
	}
	
	/**
	 * 查询分页列表数据
	 */
	@RequiresPermissions("ems:emsRankingSummaryInfo:view")
	@RequestMapping(value = "pageList")
	@ResponseBody
	@ApiOperation(value = "查询分页列表数据", notes = "查询分页列表数据")
	public Page<EmsRankingSummaryInfo> pageList(EmsRankingSummaryInfo emsRankingSummaryInfo, HttpServletRequest request, HttpServletResponse response) {
		emsRankingSummaryInfo.setPage(new Page<>(request, response));
		Page<EmsRankingSummaryInfo> page = emsRankingSummaryInfoService.findPage(emsRankingSummaryInfo);
		return page;
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ems:emsRankingSummaryInfo:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	@ApiOperation(value = "查询列表数据", notes = "查询列表数据")
	public List<EmsRankingSummaryInfo> listData(EmsRankingSummaryInfo emsRankingSummaryInfo, HttpServletRequest request, HttpServletResponse response) {
		List<EmsRankingSummaryInfo> list = emsRankingSummaryInfoService.findList(emsRankingSummaryInfo);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ems:emsRankingSummaryInfo:view")
	@RequestMapping(value = "form")
	@ApiOperation(value = "查看编辑表单", notes = "查看编辑表单")
	public String form(EmsRankingSummaryInfo emsRankingSummaryInfo, Model model) {
		model.addAttribute("emsRankingSummaryInfo", emsRankingSummaryInfo);
		return "modules/ems/emsRankingSummaryInfoForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("ems:emsRankingSummaryInfo:edit")
	@PostMapping(value = "save")
	@ResponseBody
	@ApiOperation(value = "保存数据", notes = "保存数据")
	public String save(@Validated EmsRankingSummaryInfo emsRankingSummaryInfo) {
		emsRankingSummaryInfoService.save(emsRankingSummaryInfo);
		return renderResult(Global.TRUE, text("保存耗电量排行关联表成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("ems:emsRankingSummaryInfo:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	@ApiOperation(value = "删除数据", notes = "删除数据")
	public String delete(EmsRankingSummaryInfo emsRankingSummaryInfo) {
		emsRankingSummaryInfoService.delete(emsRankingSummaryInfo);
		return renderResult(Global.TRUE, text("删除耗电量排行关联表成功！"));
	}
	
}