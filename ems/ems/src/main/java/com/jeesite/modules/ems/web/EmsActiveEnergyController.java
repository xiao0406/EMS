package com.jeesite.modules.ems.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.entity.ActiveEnergyEntity;
import com.jeesite.modules.ems.entity.ActiveEnergyStatisticsEntity;
import com.jeesite.modules.ems.entity.EChart;
import com.jeesite.modules.ems.entity.EnergyManageParam;
import com.jeesite.modules.ems.service.EmsActiveEnergyService;
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
 * 有功电量 Controller
 *
 * @author 李鹏
 * @version 2023-06-12
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/active")
@Api(value = "有功电量接口", tags = "有功电量接口")
public class EmsActiveEnergyController extends BaseController {

    @Resource
    private EmsActiveEnergyService emsActiveEnergyService;

    /**
     * 有功电量汇总表头
     *
     * @param energyManageParam
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "activeEnergyStatistics")
    @ResponseBody
    @ApiOperation(value = "有功电量汇总表头", notes = "有功电量汇总表头")
    public ActiveEnergyStatisticsEntity activeEnergyStatistics(EnergyManageParam energyManageParam, HttpServletRequest request, HttpServletResponse response) throws ExecutionException, InterruptedException, TimeoutException {
        return emsActiveEnergyService.activeEnergyStatistics(energyManageParam);
    }

    /**
     * 有功电量图表
     *
     * @param energyManageParam
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "activeEnergyEChart")
    @ResponseBody
    @ApiOperation(value = "有功电量图表", notes = "有功电量图表")
    public EChart activeEnergyEChart(EnergyManageParam energyManageParam, HttpServletRequest request, HttpServletResponse response) {
        return emsActiveEnergyService.activeEnergyEChart(energyManageParam);
    }

    /**
     * 有功电量记录列表
     *
     * @param energyManageParam
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "activeEnergyHisPageList")
    @ResponseBody
    @ApiOperation(value = "有功电量记录列表", notes = "有功电量记录列表")
    public Page<ActiveEnergyEntity> activeEnergyHisPageList(EnergyManageParam energyManageParam, HttpServletRequest request, HttpServletResponse response) {
        return emsActiveEnergyService.activeEnergyHisPageList(energyManageParam, request, response);
    }

    /**
     * 有功电量记录列表导出
     *
     * @param energyManageParam
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "activeEnergyHisListExp")
    @ResponseBody
    @ApiOperation(value = "有功电量记录列表导出", notes = "有功电量记录列表导出")
    public String activeEnergyHisListExp(EnergyManageParam energyManageParam, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return renderResult(Global.TRUE, text("成功！"), emsActiveEnergyService.activeEnergyHisListExp(energyManageParam));
    }

}