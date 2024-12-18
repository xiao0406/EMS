package com.jeesite.modules.ems.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.DictDataEntity;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.entity.CompanyVo;
import com.jeesite.modules.ems.entity.EmsMeter;
import com.jeesite.modules.ems.entity.EmsMeterOffice;
import com.jeesite.modules.ems.service.EmsMeterService;
import com.jeesite.modules.job.task.ems.timeshare.TimeShareCalculateDayDeviceRunningTimeTask;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 电表设备表Controller
 *
 * @author 李鹏
 * @version 2023-06-06
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsMeter")
@Api(value = "电表设备表接口", tags = "电表设备表")
public class EmsMeterController extends BaseController {

    @Autowired
    private EmsMeterService emsMeterService;

    @Autowired
    private TimeShareCalculateDayDeviceRunningTimeTask timeShareCalculateDayDeviceRunningTimeTask;


    /**
     * 获取数据
     */
    @ModelAttribute
    public EmsMeter get(String id, boolean isNewRecord) {
        return emsMeterService.get(id, isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequiresPermissions("ems:emsMeter:view")
    @RequestMapping(value = {"list", ""})
    @ApiOperation(value = "查询列表", notes = "查询列表")
    public String list(EmsMeter emsMeter, Model model) {
        model.addAttribute("emsMeter", emsMeter);
        return "modules/ems/emsMeterList";
    }

    /**
     * 查询分页列表数据
     */
    @RequiresPermissions("ems:emsMeter:view")
    @RequestMapping(value = "pageList")
    @ResponseBody
    @ApiOperation(value = "查询分页列表数据", notes = "查询分页列表数据")
    public Page<EmsMeter> pageList(EmsMeter emsMeter, HttpServletRequest request, HttpServletResponse response) {
        emsMeter.setPage(new Page<>(request, response));
        Page<EmsMeter> page = emsMeterService.findPage(emsMeter);
        return page;
    }

    /**
     * 查询列表数据
     */
    @RequiresPermissions("ems:emsMeter:view")
    @RequestMapping(value = "listData")
    @ResponseBody
    @ApiOperation(value = "查询列表数据", notes = "查询列表数据")
    public List<EmsMeter> listData(EmsMeter emsMeter, HttpServletRequest request, HttpServletResponse response) {
        List<EmsMeter> list = emsMeterService.findList(emsMeter);
        return list;
    }

    /**
     * 查询下拉框数据
     */
    @RequiresPermissions("ems:emsMeter:view")
    @RequestMapping(value = "dictData")
    @ResponseBody
    @ApiOperation(value = "查询列表数据", notes = "查询列表数据")
    public List<DictDataEntity> dictData(EmsMeter emsMeter, HttpServletRequest request, HttpServletResponse response) {
        List<DictDataEntity> list = emsMeterService.dictData(emsMeter);
        return list;
    }

    /**
     * 查看编辑表单
     */
    @RequiresPermissions("ems:emsMeter:view")
    @RequestMapping(value = "form")
    @ApiOperation(value = "查看编辑表单", notes = "查看编辑表单")
    public String form(EmsMeter emsMeter, Model model) {
        model.addAttribute("emsMeter", emsMeter);
        return "modules/ems/emsMeterForm";
    }

    /**
     * 保存数据
     */
    @RequiresPermissions("ems:emsMeter:edit")
    @PostMapping(value = "save")
    @ResponseBody
    @ApiOperation(value = "保存数据", notes = "保存数据")
    public String save(@Validated EmsMeter emsMeter) {
        emsMeterService.save(emsMeter);
        return renderResult(Global.TRUE, text("保存电表设备表成功！"));
    }

    /**
     * 设置电表阈值
     */
    @RequiresPermissions("ems:emsMeter:edit")
    @PostMapping(value = "saveThreshold")
    @ResponseBody
    @ApiOperation(value = "设置电表阈值", notes = "设置电表阈值")
    public String saveThreshold(EmsMeter emsMeter) {
        emsMeterService.saveThreshold(emsMeter);
        // 重新统计当天设备的状态时长
        timeShareCalculateDayDeviceRunningTimeTask.updateThresholdReCalculate();
        return renderResult(Global.TRUE, text("设置成功！"));
    }

    /**
     * 删除数据
     */
    @RequiresPermissions("ems:emsMeter:edit")
    @RequestMapping(value = "delete")
    @ResponseBody
    @ApiOperation(value = "删除数据", notes = "删除数据")
    public String delete(EmsMeter emsMeter) {
        emsMeterService.delete(emsMeter);
        return renderResult(Global.TRUE, text("删除电表设备表成功！"));
    }

    /**
     * 根据电编编号查询部门绑定配置
     */
    @RequiresPermissions("ems:emsMeter:view")
    @RequestMapping(value = "meterOfficeList")
    @ResponseBody
    @ApiOperation(value = "根据电编编号查询部门绑定配置", notes = "根据电编编号查询部门绑定配置")
    public List<EmsMeterOffice> meterOfficeList(EmsMeterOffice emsMeterOffice, HttpServletRequest request, HttpServletResponse response) {
        List<EmsMeterOffice> list = emsMeterService.meterOfficeList(emsMeterOffice);
        return list;
    }

    /**
     * 查询部门列表
     */
    @RequiresPermissions("ems:emsMeter:view")
    @RequestMapping(value = "officeList")
    @ResponseBody
    @ApiOperation(value = "查询部门列表", notes = "查询部门列表")
    public List<Map<String, Object>> officeList(@RequestBody CompanyVo companyVo, HttpServletRequest request, HttpServletResponse response) {
        return emsMeterService.officeList(companyVo);
    }

    /**
     * 保存电表部门配置
     */
    @RequiresPermissions("ems:emsMeter:edit")
    @PostMapping(value = "saveOfficeConf")
    @ResponseBody
    @ApiOperation(value = "保存电表部门配置", notes = "保存电表部门配置")
    public String saveOfficeConf(@RequestBody EmsMeter emsMeter) {
        emsMeterService.saveOfficeConf(emsMeter);
        return renderResult(Global.TRUE, text("保存电表设备表成功！"));
    }
}