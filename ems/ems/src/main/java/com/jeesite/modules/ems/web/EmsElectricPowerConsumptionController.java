package com.jeesite.modules.ems.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.entity.EmsElectricPowerConsumption;
import com.jeesite.modules.ems.service.EmsElectricPowerConsumptionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 电耗表Controller
 *
 * @author 李鹏
 * @version 2023-05-25
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsElectricPowerConsumption")
public class EmsElectricPowerConsumptionController extends BaseController {

    @Autowired
    private EmsElectricPowerConsumptionService emsElectricPowerConsumptionService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public EmsElectricPowerConsumption get(String id, boolean isNewRecord) {
        return emsElectricPowerConsumptionService.get(id, isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequiresPermissions("ems:emsElectricPowerConsumption:view")
    @RequestMapping(value = {"list", ""})
    public String list(EmsElectricPowerConsumption emsElectricPowerConsumption, Model model) {
        model.addAttribute("emsElectricPowerConsumption", emsElectricPowerConsumption);
        return "modules/ems/emsElectricPowerConsumptionList";
    }

    /**
     * 查询列表数据
     */
    @RequiresPermissions("ems:emsElectricPowerConsumption:view")
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<EmsElectricPowerConsumption> listData(EmsElectricPowerConsumption emsElectricPowerConsumption, HttpServletRequest request, HttpServletResponse response) {
        emsElectricPowerConsumption.setPage(new Page<>(request, response));
        Page<EmsElectricPowerConsumption> page = emsElectricPowerConsumptionService.findPage(emsElectricPowerConsumption);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequiresPermissions("ems:emsElectricPowerConsumption:view")
    @RequestMapping(value = "form")
    public String form(EmsElectricPowerConsumption emsElectricPowerConsumption, Model model) {
        model.addAttribute("emsElectricPowerConsumption", emsElectricPowerConsumption);
        return "modules/ems/emsElectricPowerConsumptionForm";
    }

    /**
     * 保存数据
     */
    @RequiresPermissions("ems:emsElectricPowerConsumption:edit")
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated EmsElectricPowerConsumption emsElectricPowerConsumption) {
        emsElectricPowerConsumptionService.save(emsElectricPowerConsumption);
        return renderResult(Global.TRUE, text("保存电耗表成功！"));
    }

    /**
     * 删除数据
     */
    @RequiresPermissions("ems:emsElectricPowerConsumption:edit")
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(EmsElectricPowerConsumption emsElectricPowerConsumption) {
        emsElectricPowerConsumptionService.delete(emsElectricPowerConsumption);
        return renderResult(Global.TRUE, text("删除电耗表成功！"));
    }

}