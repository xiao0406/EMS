package com.jeesite.modules.ems.web;

import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.entity.DeviceStatisticsParam;
import com.jeesite.modules.ems.entity.EmsDevicePowerEntity;
import com.jeesite.modules.ems.entity.EmsElectricPowerConsumption;
import com.jeesite.modules.ems.entity.HomePageEntity;
import com.jeesite.modules.ems.service.EmsElectricPowerConsumptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * 设备功率电流电压 Controller
 *
 * @author 吴鹏
 * @version 2023-07-21
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/devicePowerCurrentVoltage")
@Api(value = "设备功率电流电压", tags = "设备功率电流电压")
public class DevicePowerCurrentVoltageController extends BaseController {

    @Autowired
    private EmsElectricPowerConsumptionService emsElectricPowerConsumptionService;


    /**
     * 设备有功无功功率查询
     *
     * @param deviceStatisticsParam
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "deviceActiveAndReactivePower")
    @ResponseBody
    @ApiOperation(value = "设备按月年统计利用率-总时长、停机、空载、运行", notes = "设备利用率统计")
    public EmsDevicePowerEntity deviceActiveAndReactivePower(DeviceStatisticsParam deviceStatisticsParam, HttpServletRequest request, HttpServletResponse response) throws ExecutionException, InterruptedException, TimeoutException {
        return emsElectricPowerConsumptionService.deviceActiveAndReactivePower(deviceStatisticsParam);
    }



    /**
     * 设备柱状图
     *
     * @param deviceStatisticsParam
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "activeAndReactivePowerChart")
    @ResponseBody
    @ApiOperation(value = "设备功率电压电流柱状图", notes = "设备功率电压电流柱状图")
    public HomePageEntity activeAndReactivePowerChart(DeviceStatisticsParam deviceStatisticsParam, HttpServletRequest request, HttpServletResponse response) throws ExecutionException, InterruptedException, TimeoutException {
        return emsElectricPowerConsumptionService.activeAndReactivePowerChart(deviceStatisticsParam);
    }

    /**
     * 历史记录
     *
     * @param deviceStatisticsParam
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "recordList")
    @ResponseBody
    @ApiOperation(value = "电耗表参数历史记录", notes = "电耗表参数历史记录")
    public Page<EmsElectricPowerConsumption> recordList(DeviceStatisticsParam deviceStatisticsParam, HttpServletRequest request, HttpServletResponse response) {
        return emsElectricPowerConsumptionService.recordList(deviceStatisticsParam,request,response);
    }


    /**
     * 导出
     *
     * @param deviceStatisticsParam
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "export")
    @ResponseBody
    @ApiOperation(value = "电耗表参数历史记录", notes = "电耗表参数历史记录")
    public String export(DeviceStatisticsParam deviceStatisticsParam, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return emsElectricPowerConsumptionService.export(deviceStatisticsParam);
    }




}