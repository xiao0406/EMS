package com.jeesite.modules.ems.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.entity.EmsStatisticsReportEntity;
import com.jeesite.modules.ems.entity.MeterPendulumDisplayEntity;
import com.jeesite.modules.ems.service.EmsStatisticsReportService;
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
 * 统计报表Controller
 *
 * @author 李鹏
 * @version 2023-06-12
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/report")
@Api(value = "区域表接口", tags = "区域表")
public class EmsStatisticsReportController extends BaseController {

    @Resource
    private EmsStatisticsReportService emsStatisticsReportService;

    /**
     * 设备耗电量报表
     */
    @RequestMapping(value = "meterElectricConsumption")
    @ResponseBody
    @ApiOperation(value = "设备耗电量报表", notes = "设备耗电量报表")
    public EmsStatisticsReportEntity meterElectricConsumption(EmsStatisticsReportEntity emsStatisticsReportEntity, HttpServletRequest request, HttpServletResponse response) {
        EmsStatisticsReportEntity rlt = emsStatisticsReportService.meterElectricConsumption(emsStatisticsReportEntity);
        return rlt;
    }

    /**
     * 导出--设备耗电量报表
     */
    @RequestMapping(value = "meterElectricConsumptionExp")
    @ResponseBody
    @ApiOperation(value = "导出--设备耗电量报表", notes = "导出--设备耗电量报表")
    public String meterElectricConsumptionExp(EmsStatisticsReportEntity emsStatisticsReportEntity, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName = "设备耗电量报表导出" + DateUtils.getDate("yyyyMMddHHmmss") + ".xls";
        String presignedUrl = emsStatisticsReportService.meterElectricConsumptionExp(fileName, emsStatisticsReportEntity);
        return renderResult(Global.TRUE, text("下载成功！"), presignedUrl);
    }

    /**
     * 设备分时耗电量报表
     */
    @RequestMapping(value = "meterTimeShareConsumption")
    @ResponseBody
    @ApiOperation(value = "设备分时耗电量报表", notes = "设备分时耗电量报表")
    public EmsStatisticsReportEntity meterTimeShareConsumption(EmsStatisticsReportEntity emsStatisticsReportEntity, HttpServletRequest request, HttpServletResponse response) {
        EmsStatisticsReportEntity rlt = emsStatisticsReportService.meterTimeShareConsumption(emsStatisticsReportEntity);
        return rlt;
    }

    /**
     * 导出--设备分时耗电量报表
     */
    @RequestMapping(value = "meterTimeShareConsumptionExp")
    @ResponseBody
    @ApiOperation(value = "导出--设备分时耗电量报表", notes = "导出--设备分时耗电量报表")
    public String meterTimeShareConsumptionExp(EmsStatisticsReportEntity emsStatisticsReportEntity, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName = "设备分时耗电量报表导出" + DateUtils.getDate("yyyyMMddHHmmss") + ".xls";
        String presignedUrl = emsStatisticsReportService.meterTimeShareConsumptionExp(fileName, emsStatisticsReportEntity);
        return renderResult(Global.TRUE, text("下载成功！"), presignedUrl);
    }

    /**
     * 功效统计报表
     */
    @RequestMapping(value = "meterEfficacy")
    @ResponseBody
    @ApiOperation(value = "功效统计报表", notes = "功效统计报表")
    public EmsStatisticsReportEntity meterEfficacy(EmsStatisticsReportEntity emsStatisticsReportEntity, HttpServletRequest request, HttpServletResponse response) {
        EmsStatisticsReportEntity rlt = emsStatisticsReportService.meterEfficacy(emsStatisticsReportEntity);
        return rlt;
    }

    /**
     * 导出--功效统计报表
     */
    @RequestMapping(value = "meterEfficacyExp")
    @ResponseBody
    @ApiOperation(value = "导出--功效统计报表", notes = "导出--功效统计报表")
    public String meterEfficacyExp(EmsStatisticsReportEntity emsStatisticsReportEntity, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName = "功效统计报表导出" + DateUtils.getDate("yyyyMMddHHmmss") + ".xls";
        String presignedUrl = emsStatisticsReportService.meterEfficacyExp(fileName, emsStatisticsReportEntity);
        return renderResult(Global.TRUE, text("下载成功！"), presignedUrl);
    }

    /**
     * 运行时长报表
     */
    @RequestMapping(value = "meterRuntime")
    @ResponseBody
    @ApiOperation(value = "运行时长报表", notes = "运行时长报表")
    public EmsStatisticsReportEntity meterRuntime(EmsStatisticsReportEntity emsStatisticsReportEntity, HttpServletRequest request, HttpServletResponse response) {
        EmsStatisticsReportEntity rlt = emsStatisticsReportService.meterRuntime(emsStatisticsReportEntity);
        return rlt;
    }

    /**
     * 导出--运行时长报表
     */
    @RequestMapping(value = "meterRuntimeExp")
    @ResponseBody
    @ApiOperation(value = "导出--运行时长报表", notes = "导出--运行时长报表")
    public String meterRuntimeExp(EmsStatisticsReportEntity emsStatisticsReportEntity, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName = "运行时长报表导出" + DateUtils.getDate("yyyyMMddHHmmss") + ".xls";
        String presignedUrl = emsStatisticsReportService.meterRuntimeExp(fileName, emsStatisticsReportEntity);
        return renderResult(Global.TRUE, text("下载成功！"), presignedUrl);
    }

    /**
     * 设备利用率报表
     */
    @RequestMapping(value = "meterUseRatio")
    @ResponseBody
    @ApiOperation(value = "设备利用率报表", notes = "设备利用率报表")
    public EmsStatisticsReportEntity meterUseRatio(EmsStatisticsReportEntity emsStatisticsReportEntity, HttpServletRequest request, HttpServletResponse response) {
        EmsStatisticsReportEntity rlt = emsStatisticsReportService.meterUseRatio(emsStatisticsReportEntity);
        return rlt;
    }

    /**
     * 导出--设备利用率报表
     */
    @RequestMapping(value = "meterUseRatioExp")
    @ResponseBody
    @ApiOperation(value = "导出--设备利用率报表", notes = "导出--设备利用率报表")
    public String meterUseRatioExp(EmsStatisticsReportEntity emsStatisticsReportEntity, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName = "设备利用率报表导出" + DateUtils.getDate("yyyyMMddHHmmss") + ".xls";
        String presignedUrl = emsStatisticsReportService.meterUseRatioExp(fileName, emsStatisticsReportEntity);
        return renderResult(Global.TRUE, text("下载成功！"), presignedUrl);
    }

    /**
     * 部门耗电量报表
     */
    @RequestMapping(value = "officeConsumption")
    @ResponseBody
    @ApiOperation(value = "部门耗电量报表", notes = "部门耗电量报表")
    public EmsStatisticsReportEntity officeConsumption(EmsStatisticsReportEntity emsStatisticsReportEntity, HttpServletRequest request, HttpServletResponse response) {
        EmsStatisticsReportEntity rlt = emsStatisticsReportService.officeConsumption(emsStatisticsReportEntity);
        return rlt;
    }

    /**
     * 导出--部门耗电量报表
     */
    @RequestMapping(value = "officeConsumptionExp")
    @ResponseBody
    @ApiOperation(value = "导出--部门耗电量报表", notes = "导出--部门耗电量报表")
    public String officeConsumptionExp(EmsStatisticsReportEntity emsStatisticsReportEntity, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName = "部门耗电量报表导出" + DateUtils.getDate("yyyyMMddHHmmss") + ".xls";
        String presignedUrl = emsStatisticsReportService.officeConsumptionExp(fileName, emsStatisticsReportEntity);
        return renderResult(Global.TRUE, text("下载成功！"), presignedUrl);
    }

    /**
     * 电表示数报表
     */
    @RequestMapping(value = "meterPendulumDisplay")
    @ResponseBody
    @ApiOperation(value = "电表示数报表", notes = "电表示数报表")
    public List<MeterPendulumDisplayEntity> meterPendulumDisplay(EmsStatisticsReportEntity emsStatisticsReportEntity, HttpServletRequest request, HttpServletResponse response) {
        List<MeterPendulumDisplayEntity> rlt = emsStatisticsReportService.meterPendulumDisplay(emsStatisticsReportEntity);
        return rlt;
    }

    /**
     * 导出--电表示数报表
     */
    @RequestMapping(value = "meterPendulumDisplayExp")
    @ResponseBody
    @ApiOperation(value = "导出--电表示数报表", notes = "导出--电表示数报表")
    public String meterPendulumDisplayExp(EmsStatisticsReportEntity emsStatisticsReportEntity, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName = "电表示数报表导出" + DateUtils.getDate("yyyyMMddHHmmss") + ".xls";
        String presignedUrl = emsStatisticsReportService.meterPendulumDisplayExp(fileName, emsStatisticsReportEntity);
        return renderResult(Global.TRUE, text("下载成功！"), presignedUrl);
    }

}