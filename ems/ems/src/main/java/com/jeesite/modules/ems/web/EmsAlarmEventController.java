package com.jeesite.modules.ems.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.modules.ems.entity.EmsAlarmEventRecordExport;
import com.jeesite.modules.ems.entity.EmsMeterThresholdConfig;
import com.jeesite.modules.sys.utils.ExcelExportUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import io.swagger.annotations.*;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.entity.EmsAlarmEvent;
import com.jeesite.modules.ems.service.EmsAlarmEventService;

/**
 * 报警事件记录表Controller
 * @author 李鹏
 * @version 2023-07-22
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsAlarmEvent")
@Api(value = "报警事件记录表接口", tags = "报警事件记录表")
public class EmsAlarmEventController extends BaseController {

	@Resource
	private EmsAlarmEventService emsAlarmEventService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public EmsAlarmEvent get(String id, boolean isNewRecord) {
		return emsAlarmEventService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ems:emsAlarmEvent:view")
	@RequestMapping(value = {"list", ""})
	@ApiOperation(value = "查询列表", notes = "查询列表")
	public String list(EmsAlarmEvent emsAlarmEvent, Model model) {
		model.addAttribute("emsAlarmEvent", emsAlarmEvent);
		return "modules/ems/emsAlarmEventList";
	}
	
	/**
	 * 查询分页列表数据
	 */
	@RequiresPermissions("ems:emsAlarmEvent:view")
	@RequestMapping(value = "pageList")
	@ResponseBody
	@ApiOperation(value = "查询分页列表数据", notes = "查询分页列表数据")
	public Page<EmsAlarmEvent> pageList(EmsAlarmEvent emsAlarmEvent, HttpServletRequest request, HttpServletResponse response) {
		emsAlarmEvent.setPage(new Page<>(request, response));
		if(emsAlarmEvent.getQryEndTime() != null){
			emsAlarmEvent.setQryEndTime( emsAlarmEvent.getQryEndTime() + " 23:59:59");
		}
		Page<EmsAlarmEvent> page = emsAlarmEventService.findPage(emsAlarmEvent);
		return page;
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ems:emsAlarmEvent:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	@ApiOperation(value = "查询列表数据", notes = "查询列表数据")
	public List<EmsAlarmEvent> listData(EmsAlarmEvent emsAlarmEvent, HttpServletRequest request, HttpServletResponse response) {
		List<EmsAlarmEvent> list = emsAlarmEventService.findList(emsAlarmEvent);
		return list;
	}

	/**
	 * 查询列表数据导出
	 */
	@RequiresPermissions("ems:emsAlarmEvent:view")
	@RequestMapping(value = "listDataExp")
	@ResponseBody
	@ApiOperation(value = "查询列表数据导出", notes = "查询列表数据导出")
	public String listDataExp(EmsAlarmEvent emsAlarmEvent, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String presignedUrl = null;
		List<EmsAlarmEvent> list = emsAlarmEventService.findList(emsAlarmEvent);
		if(CollectionUtils.isEmpty(list)){
			return "";
		}
		List<EmsAlarmEventRecordExport> exportList = new ArrayList<>();
		list.forEach(en->{
			EmsAlarmEventRecordExport export = new EmsAlarmEventRecordExport();
			export.setDeviceName(en.getDeviceName());
			export.setDeviceId(en.getDeviceId());
			export.setDataDateTime(en.getDataDateTime());
			export.setEventTypeMsg(en.getEventTypeMsg());
			export.setEventLevel(en.getEventLevel());
			export.setEventContent(en.getEventContent());
			exportList.add(export);
		});
		String fileName = "电表报警日志导出" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try (ExcelExport ee = new ExcelExport("电表报警日志导出", EmsAlarmEventRecordExport.class)) {
			presignedUrl = ExcelExportUtil.uploadOss(ee.setDataList(exportList), fileName);
		}
		return renderResult(Global.TRUE, text("成功！"), presignedUrl);
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ems:emsAlarmEvent:view")
	@RequestMapping(value = "form")
	@ApiOperation(value = "查看编辑表单", notes = "查看编辑表单")
	public String form(EmsAlarmEvent emsAlarmEvent, Model model) {
		model.addAttribute("emsAlarmEvent", emsAlarmEvent);
		return "modules/ems/emsAlarmEventForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("ems:emsAlarmEvent:edit")
	@PostMapping(value = "save")
	@ResponseBody
	@ApiOperation(value = "保存数据", notes = "保存数据")
	public String save(@Validated EmsAlarmEvent emsAlarmEvent) {
		emsAlarmEventService.save(emsAlarmEvent);
		return renderResult(Global.TRUE, text("保存报警事件记录表成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("ems:emsAlarmEvent:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	@ApiOperation(value = "删除数据", notes = "删除数据")
	public String delete(EmsAlarmEvent emsAlarmEvent) {
		emsAlarmEventService.delete(emsAlarmEvent);
		return renderResult(Global.TRUE, text("删除报警事件记录表成功！"));
	}
	
}