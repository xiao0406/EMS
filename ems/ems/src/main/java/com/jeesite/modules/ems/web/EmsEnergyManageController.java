package com.jeesite.modules.ems.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.entity.EChart;
import com.jeesite.modules.ems.entity.EnergyManageEntity;
import com.jeesite.modules.ems.entity.EnergyManageParam;
import com.jeesite.modules.ems.entity.EnergyManageStatisticsEntity;
import com.jeesite.modules.ems.service.EmsEnergyManageService;
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
 * 有功/无功电量 Controller
 *
 * @author 李鹏
 * @version 2023-06-12
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/energy")
@Api(value = "有功/无功电量接口", tags = "有功/无功电量接口")
public class EmsEnergyManageController extends BaseController {

    @Resource
    private EmsEnergyManageService emsEnergyManageService;

    /**
     * 有功/无功电量汇总表头
     *
     * @param energyManageParam
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "energyManageStatistics")
    @ResponseBody
    @ApiOperation(value = "有功/无功电量汇总表头", notes = "有功/无功电量汇总表头")
    public EnergyManageStatisticsEntity energyManageStatistics(EnergyManageParam energyManageParam, HttpServletRequest request, HttpServletResponse response) throws ExecutionException, InterruptedException, TimeoutException {
        return emsEnergyManageService.energyManageStatistics(energyManageParam);
    }

    /**
     * 有功/无功电量图表
     *
     * @param energyManageParam
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "energyManageEChart")
    @ResponseBody
    @ApiOperation(value = "有功/无功电量图表", notes = "有功/无功电量图表")
    public EChart energyManageEChart(EnergyManageParam energyManageParam, HttpServletRequest request, HttpServletResponse response) {
        return emsEnergyManageService.energyManageEChart(energyManageParam);
    }

    /**
     * 有功/无功电量记录列表
     *
     * @param energyManageParam
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "energyManageHisPageList")
    @ResponseBody
    @ApiOperation(value = "有功/无功电量记录列表", notes = "有功/无功电量记录列表")
    public Page<EnergyManageEntity> energyManageHisPageList(EnergyManageParam energyManageParam, HttpServletRequest request, HttpServletResponse response) {
        return emsEnergyManageService.energyManageHisPageList(energyManageParam, request, response);
    }

    /**
     * 有功/无功电量记录列表导出
     *
     * @param energyManageParam
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "energyManageHisListExp")
    @ResponseBody
    @ApiOperation(value = "有功/无功电量记录列表导出", notes = "有功/无功电量记录列表导出")
    public String energyManageHisListExp(EnergyManageParam energyManageParam, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return renderResult(Global.TRUE, text("成功！"), emsEnergyManageService.energyManageHisListExp(energyManageParam));
    }

}