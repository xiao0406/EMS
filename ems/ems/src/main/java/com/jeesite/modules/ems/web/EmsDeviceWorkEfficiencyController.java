package com.jeesite.modules.ems.web;

import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.entity.DeviceWorkEfficiencyEntity;
import com.jeesite.modules.ems.entity.DeviceWorkEfficiencyQryEntity;
import com.jeesite.modules.ems.entity.EChart;
import com.jeesite.modules.ems.service.EmsDeviceWorkEfficiencyService;
import com.jeesite.modules.sys.utils.ExcelExportUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 有功电量 Controller
 *
 * @author 李鹏
 * @version 2023-06-12
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/workEfficiency")
@Api(value = "有功电量接口", tags = "有功电量接口")
public class EmsDeviceWorkEfficiencyController extends BaseController {

    @Resource
    private EmsDeviceWorkEfficiencyService emsDeviceWorkEfficiencyService;

    /**
     * 设备工效统计 EChart
     *
     * @param deviceWorkEfficiencyQryEntity
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "workEfficiencyEChart")
    @ResponseBody
    @ApiOperation(value = "设备工效统计 EChart", notes = "设备工效统计 EChart")
    public EChart workEfficiencyEChart(DeviceWorkEfficiencyQryEntity deviceWorkEfficiencyQryEntity, HttpServletRequest request, HttpServletResponse response) {
        return emsDeviceWorkEfficiencyService.workEfficiencyEChart(deviceWorkEfficiencyQryEntity);
    }

    /**
     * 设备工效统计分页列表查询
     *
     * @param deviceWorkEfficiencyQryEntity
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "workEfficiencyPageList")
    @ResponseBody
    @ApiOperation(value = "有功电量记录列表", notes = "有功电量记录列表")
    public Page<DeviceWorkEfficiencyEntity> workEfficiencyPageList(DeviceWorkEfficiencyQryEntity deviceWorkEfficiencyQryEntity, HttpServletRequest request, HttpServletResponse response) {
        return emsDeviceWorkEfficiencyService.workEfficiencyPageList(deviceWorkEfficiencyQryEntity, request, response);
    }

    /**
     * 设备工效统计分页列表导出
     *
     * @param deviceWorkEfficiencyQryEntity
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "workEfficiencyListExp")
    @ResponseBody
    @ApiOperation(value = "设备工效统计分页列表导出", notes = "设备工效统计分页列表导出")
    public String workEfficiencyListExp(DeviceWorkEfficiencyQryEntity deviceWorkEfficiencyQryEntity, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String presignedUrl = null;
        List<DeviceWorkEfficiencyEntity> list = emsDeviceWorkEfficiencyService.workEfficiencyListData(deviceWorkEfficiencyQryEntity);
        String fileName = "设备工效统计列表导出" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
        try (ExcelExport ee = new ExcelExport("设备工效统计列表导出", DeviceWorkEfficiencyEntity.class)) {
            presignedUrl = ExcelExportUtil.uploadOss(ee.setDataList(list), fileName);
        }
        return presignedUrl;
    }

}