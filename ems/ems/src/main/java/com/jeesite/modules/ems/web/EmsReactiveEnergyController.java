package com.jeesite.modules.ems.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.entity.EChart;
import com.jeesite.modules.ems.entity.EnergyManageParam;
import com.jeesite.modules.ems.entity.ReactiveEnergyEntity;
import com.jeesite.modules.ems.entity.ReactiveEnergyStatisticsEntity;
import com.jeesite.modules.ems.service.EmsReactiveEnergyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * 无功电量 Controller
 *
 * @author 李鹏
 * @version 2023-06-12
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/reactive")
@Api(value = "无功电量接口", tags = "无功电量接口")
public class EmsReactiveEnergyController extends BaseController {

    @Resource
    private EmsReactiveEnergyService emsReactiveEnergyService;

    /**
     * 无功电量汇总表头
     *
     * @param energyManageParam
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "reactiveEnergyStatistics")
    @ResponseBody
    @ApiOperation(value = "无功电量汇总表头", notes = "无功电量汇总表头")
    public ReactiveEnergyStatisticsEntity reactiveEnergyStatistics(EnergyManageParam energyManageParam, HttpServletRequest request, HttpServletResponse response) throws ExecutionException, InterruptedException, TimeoutException {
        return emsReactiveEnergyService.reactiveEnergyStatistics(energyManageParam);
    }

    /**
     * 无功电量图表
     *
     * @param energyManageParam
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "reactiveEnergyEChart")
    @ResponseBody
    @ApiOperation(value = "无功电量图表", notes = "无功电量图表")
    public EChart reactiveEnergyEChart(EnergyManageParam energyManageParam, HttpServletRequest request, HttpServletResponse response) {
        return emsReactiveEnergyService.reactiveEnergyEChart(energyManageParam);
    }

    /**
     * 无功电量记录列表
     *
     * @param energyManageParam
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "reactiveEnergyHisPageList")
    @ResponseBody
    @ApiOperation(value = "无功电量记录列表", notes = "无功电量记录列表")
    public Page<ReactiveEnergyEntity> reactiveEnergyHisPageList(EnergyManageParam energyManageParam, HttpServletRequest request, HttpServletResponse response) {
        return emsReactiveEnergyService.reactiveEnergyHisPageList(energyManageParam, request, response);
    }

    /**
     * 无功电量记录列表导出
     *
     * @param energyManageParam
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "reactiveEnergyHisListExp")
    @ResponseBody
    @ApiOperation(value = "无功电量记录列表导出", notes = "无功电量记录列表导出")
    public String reactiveEnergyHisListExp(EnergyManageParam energyManageParam, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return renderResult(Global.TRUE, text("成功！"), emsReactiveEnergyService.reactiveEnergyHisListExp(energyManageParam));
    }

}