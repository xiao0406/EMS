package com.jeesite.modules.ems.web;

import cn.hutool.core.util.ObjectUtil;
import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.entity.*;
import com.jeesite.modules.ems.service.EmsTimeShareDeviceRuntimeService;
import com.jeesite.modules.ems.service.EmsTimeShareDeviceRuntimeStatisticsService;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * 设备利用率 Controller
 *
 * @author 吴鹏
 * @version 2023-07-19
 */

@Controller
@RequestMapping(value = "${adminPath}/ems/deviceRateUtilization")
@Api(value = "设备利用率接口", tags = "设备利用率")
public class DeviceRateUtilizationController extends BaseController {

    @Resource
    private EmsTimeShareDeviceRuntimeStatisticsService emsTimeShareDeviceRuntimeStatisticsService;

    @Resource
    private EmsTimeShareDeviceRuntimeService emsTimeShareDeviceRuntimeService;


    /**
     * 设备按月年统计利用率
     *
     * @param deviceStatisticsParam
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "deviceRateStatistics")
    @ResponseBody
    @ApiOperation(value = "设备按月天统计利用率-总时长、停机、空载、运行", notes = "设备利用率统计")
    public EmsDailyStatisticsEntity deviceRateStatistics(DeviceStatisticsParam deviceStatisticsParam, HttpServletRequest request, HttpServletResponse response) throws ExecutionException, InterruptedException, TimeoutException {
        if(deviceStatisticsParam.getTemporalGranularity().equals(TemporalGranularityEnum.VD_Day.getCode())){
            // 按天统计
            return emsTimeShareDeviceRuntimeStatisticsService.deviceRateStatistics(deviceStatisticsParam);
        }else {
            // 按月统计
            return emsTimeShareDeviceRuntimeStatisticsService.deviceRateStatisticsYear(deviceStatisticsParam);
        }
    }



    /**
     * 单日设备统计-月份 天 历史记录柱状图
     *
     * @param deviceStatisticsParam
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "recordEchart")
    @ResponseBody
    @ApiOperation(value = "单日设备统计-总时长、停机、空载、运行", notes = "单日设备统计")
    public HomePageEntity recordEchart(DeviceStatisticsParam deviceStatisticsParam, HttpServletRequest request, HttpServletResponse response) throws ExecutionException, InterruptedException, TimeoutException {
        if(deviceStatisticsParam.getTemporalGranularity().equals(TemporalGranularityEnum.VD_Day.getCode())){
            return emsTimeShareDeviceRuntimeStatisticsService.dayRecord(deviceStatisticsParam);
        }else {
            return emsTimeShareDeviceRuntimeStatisticsService.monthRecord(deviceStatisticsParam);
        }
    }



    /**
     * 设备月份内能耗记录
     *
     * @param deviceStatisticsParam
     * @param request
     * @param response
     * @return
     */

    @RequestMapping(value = "recordList")
    @ResponseBody
    @ApiOperation(value = "设备日月份内能耗记录", notes = "日月记录")
    public Page<EmsDailyStatisticsListEntity> recordList(DeviceStatisticsParam deviceStatisticsParam, HttpServletRequest request, HttpServletResponse response) throws ExecutionException, InterruptedException, TimeoutException {
        if(deviceStatisticsParam.getTemporalGranularity().equals(TemporalGranularityEnum.VD_Month.getCode())){
            return emsTimeShareDeviceRuntimeStatisticsService.monthRecordList(deviceStatisticsParam,request,response);
        }
        EmsTimeShareDeviceRuntime emsTimeShareDeviceRuntime = new EmsTimeShareDeviceRuntime();
        emsTimeShareDeviceRuntime.setDeviceId(deviceStatisticsParam.getDeviceId());
        if(ObjectUtil.isEmpty(deviceStatisticsParam.getStart())){
            deviceStatisticsParam.setStart(DateUtils.addDays(deviceStatisticsParam.getDate(),-30));
            deviceStatisticsParam.setEnd(deviceStatisticsParam.getDate());
        }
        emsTimeShareDeviceRuntime.setDataDateStart(deviceStatisticsParam.getStart());
        emsTimeShareDeviceRuntime.setDataDateEnd(deviceStatisticsParam.getEnd());
        return emsTimeShareDeviceRuntimeService.pageList(emsTimeShareDeviceRuntime,request,response);
    }


    /**
     * 设备利用率历史记录导出
     *
     * @param deviceStatisticsParam
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "dayExport")
    @ResponseBody
    @ApiOperation(value = "单日设备统计-总时长、停机、空载、运行", notes = "单日设备统计")
    public String dayExport(DeviceStatisticsParam deviceStatisticsParam, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String presignedUrl = null;
        if(deviceStatisticsParam.getTemporalGranularity().equals("VD_DayTime")){
            deviceStatisticsParam.setStart(DateUtils.addDays(deviceStatisticsParam.getEnd(),-30));
            deviceStatisticsParam.setTemporalGranularity(TemporalGranularityEnum.VD_Day.getCode());
        }

        if(deviceStatisticsParam.getTemporalGranularity().equals(TemporalGranularityEnum.VD_Day.getCode())){
            List<EmsTimeShareDeviceRuntime> list = emsTimeShareDeviceRuntimeStatisticsService.dayExport(deviceStatisticsParam);
            String fileName = "设备运行状态历史记录" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            try (ExcelExport ee = new ExcelExport("设备运行状态历史记录导出", EmsTimeShareDeviceRuntime.class)) {
                presignedUrl = ExcelExportUtil.uploadOss(ee.setDataList(list), fileName);
            }
        }else {
            List<EmsTimeShareDeviceRuntimeStatistics> list = emsTimeShareDeviceRuntimeStatisticsService.monthExport(deviceStatisticsParam);
            String fileName = "设备运行状态历史记录" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            try (ExcelExport ee = new ExcelExport("设备运行状态历史记录导出", EmsTimeShareDeviceRuntimeStatistics.class)) {
                presignedUrl = ExcelExportUtil.uploadOss(ee.setDataList(list), fileName);
            }
        }
        return presignedUrl;
    }
}