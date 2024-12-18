package com.jeesite.modules.ems.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.entity.EmsMeterCollectedData;
import com.jeesite.modules.ems.service.EmsMeterCollectedDataService;
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
 * 电表采集数据Controller
 *
 * @author 李鹏
 * @version 2023-06-08
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsMeterCollectedData")
public class EmsMeterCollectedDataController extends BaseController {

    @Autowired
    private EmsMeterCollectedDataService emsMeterCollectedDataService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public EmsMeterCollectedData get(String id, boolean isNewRecord) {
        return emsMeterCollectedDataService.get(id, isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequiresPermissions("ems:emsMeterCollectedData:view")
    @RequestMapping(value = {"list", ""})
    public String list(EmsMeterCollectedData emsMeterCollectedData, Model model) {
        model.addAttribute("emsMeterCollectedData", emsMeterCollectedData);
        return "modules/ems/emsMeterCollectedDataList";
    }

    /**
     * 查询列表数据
     */
    @RequiresPermissions("ems:emsMeterCollectedData:view")
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<EmsMeterCollectedData> listData(EmsMeterCollectedData emsMeterCollectedData, HttpServletRequest request, HttpServletResponse response) {
        emsMeterCollectedData.setPage(new Page<>(request, response));
        Page<EmsMeterCollectedData> page = emsMeterCollectedDataService.findPage(emsMeterCollectedData);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequiresPermissions("ems:emsMeterCollectedData:view")
    @RequestMapping(value = "form")
    public String form(EmsMeterCollectedData emsMeterCollectedData, Model model) {
        model.addAttribute("emsMeterCollectedData", emsMeterCollectedData);
        return "modules/ems/emsMeterCollectedDataForm";
    }

    /**
     * 保存数据
     */
    @RequiresPermissions("ems:emsMeterCollectedData:edit")
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated EmsMeterCollectedData emsMeterCollectedData) {
        emsMeterCollectedDataService.save(emsMeterCollectedData);
        return renderResult(Global.TRUE, text("保存电表采集数据成功！"));
    }

    /**
     * 删除数据
     */
    @RequiresPermissions("ems:emsMeterCollectedData:edit")
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(EmsMeterCollectedData emsMeterCollectedData) {
        emsMeterCollectedDataService.delete(emsMeterCollectedData);
        return renderResult(Global.TRUE, text("删除电表采集数据成功！"));
    }

}