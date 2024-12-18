package com.jeesite.modules.ems.web;

import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.entity.*;
import com.jeesite.modules.ems.service.EmsTimeShareDeviceRuntimeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * 单日设备利用率 Controller
 *
 * @author 吴鹏
 * @version 2023-07-18
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/dailyUtilization")
@Api(value = "单日设备利用率接口", tags = "单日设备利用率")
public class EmsDailyUtilizationController extends BaseController {

    @Resource
    private EmsTimeShareDeviceRuntimeService emsTimeShareDeviceRuntimeService;


    /**
     * 单日设备统计-总时长、停机、空载、运行
     *
     * @param dailyStatisticsParam
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "dailyStatistics")
    @ResponseBody
    @ApiOperation(value = "单日设备统计-总时长、停机、空载、运行", notes = "单日设备统计")
    public EmsDailyStatisticsEntity dailyStatistics(DailyStatisticsParam dailyStatisticsParam, HttpServletRequest request, HttpServletResponse response) throws ExecutionException, InterruptedException, TimeoutException {
        return emsTimeShareDeviceRuntimeService.dailyStatistics(dailyStatisticsParam);
    }


    /**
     * 单日设备统计-历史记录
     *
     * @param emsTimeShareDeviceRuntime
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "pageList")
    @ResponseBody
    @ApiOperation(value = "单日设备统计-历史记录", notes = "单日设备统计")
    public Page<EmsDailyStatisticsListEntity> pageList(EmsTimeShareDeviceRuntime emsTimeShareDeviceRuntime, HttpServletRequest request, HttpServletResponse response) throws ExecutionException, InterruptedException, TimeoutException {
        // 开始日期向前推30天
        if(emsTimeShareDeviceRuntime.getDataDateStart() == null){
            emsTimeShareDeviceRuntime.setDataDateStart(DateUtils.addDays(emsTimeShareDeviceRuntime.getDataDateEnd(),-30));
        }
        return emsTimeShareDeviceRuntimeService.pageList(emsTimeShareDeviceRuntime,request,response);
    }



    /**
     * 单日设备统计-设备空载运行的阈值
     *
     * @param emsMeterThresholdEntity
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "thresholdValue")
    @ResponseBody
    @ApiOperation(value = "单日设备统计-空载阈值", notes = "单日设备统计")
    public EmsMeterThresholdEntity thresholdValue(EmsMeterThresholdEntity emsMeterThresholdEntity, HttpServletRequest request, HttpServletResponse response) throws ExecutionException, InterruptedException, TimeoutException {
        return emsTimeShareDeviceRuntimeService.thresholdValue(emsMeterThresholdEntity,request,response);
    }


    /**
     * 单日设备统计-设备时刻能耗和状态柱状图
     *
     * @param dailyStatisticsParam
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "chartValue")
    @ResponseBody
    @ApiOperation(value = "单日设备统计-设备时刻能耗和状态柱状图", notes = "单日设备统计")
    public EmsDailyStatisticsEchartEntity chartValue(DailyStatisticsParam dailyStatisticsParam, HttpServletRequest request, HttpServletResponse response) throws ExecutionException, InterruptedException, TimeoutException {
        return emsTimeShareDeviceRuntimeService.chartValue(dailyStatisticsParam,request,response);
    }
}