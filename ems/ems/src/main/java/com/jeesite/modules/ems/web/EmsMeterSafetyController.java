package com.jeesite.modules.ems.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.entity.*;
import com.jeesite.modules.ems.service.EmsMeterSafetyService;
import com.jeesite.modules.sys.utils.ExcelExportUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * 设备安全/故障 Controller
 *
 * @author 李鹏
 * @version 2023-06-12
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/safety")
@Api(value = "设备安全/故障接口", tags = "设备安全/故障接口")
public class EmsMeterSafetyController extends BaseController {

    @Resource
    private EmsMeterSafetyService emsMeterSafetyService;

    /**
     * 汇总表头
     *
     * @param emsMeterSafetyEntity
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "meterSafetyStatistics")
    @ResponseBody
    @ApiOperation(value = "汇总表头", notes = "汇总表头")
    public MeterSafetyStatisticsEntity meterSafetyStatistics(EmsMeterSafetyEntity emsMeterSafetyEntity, HttpServletRequest request, HttpServletResponse response) throws ExecutionException, InterruptedException, TimeoutException {
        return emsMeterSafetyService.meterSafetyStatistics(emsMeterSafetyEntity);
    }

    /**
     * EChart汇总表头
     *
     * @param emsMeterSafetyEntity
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "meterSafetyEChartStatistics")
    @ResponseBody
    @ApiOperation(value = "EChart汇总表头", notes = "EChart汇总表头")
    public EmsMeterSafetyEntity meterSafetyEChartStatistics(EmsMeterSafetyEntity emsMeterSafetyEntity, HttpServletRequest request, HttpServletResponse response) throws ExecutionException, InterruptedException, TimeoutException {
        return emsMeterSafetyService.meterSafetyEChartStatistics(emsMeterSafetyEntity);
    }

    /**
     * EChart
     *
     * @param emsMeterSafetyEntity
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "meterSafetyEChart")
    @ResponseBody
    @ApiOperation(value = "EChart", notes = "EChart")
    public EChart meterSafetyEChart(EmsMeterSafetyEntity emsMeterSafetyEntity, HttpServletRequest request, HttpServletResponse response) {
        return emsMeterSafetyService.meterSafetyEChart(emsMeterSafetyEntity);
    }

    /**
     * 记录列表
     *
     * @param emsMeterSafetyEntity
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "meterSafetyRecPageList")
    @ResponseBody
    @ApiOperation(value = "记录列表", notes = "记录列表")
    public Page<EmsAlarmRecordEntity> meterSafetyRecPageList(EmsMeterSafetyEntity emsMeterSafetyEntity, HttpServletRequest request, HttpServletResponse response) {
        return emsMeterSafetyService.meterSafetyRecPageList(emsMeterSafetyEntity, request, response);
    }

    /**
     * 记录列表导出
     *
     * @param emsMeterSafetyEntity
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "meterSafetyRecListExp")
    @ResponseBody
    @ApiOperation(value = "有功电量记录列表导出", notes = "有功电量记录列表导出")
    public String meterSafetyRecListExp(EmsMeterSafetyEntity emsMeterSafetyEntity, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return renderResult(Global.TRUE, text("成功！"), emsMeterSafetyService.meterSafetyRecListExp(emsMeterSafetyEntity));
    }


    /**
     * 设备运行检测 报警记录
     *
     * @param emsAlarmEvent
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "meterSafetyRecord")
    @ResponseBody
    @ApiOperation(value = "设备运行检测报警记录", notes = "设备运行检测报警记录")
    public Page<EmsAlarmEvent> meterSafetyRecord(EmsAlarmEvent emsAlarmEvent, HttpServletRequest request, HttpServletResponse response) {
        if(emsAlarmEvent == null && emsAlarmEvent.getDeviceId().isEmpty()){
            logger.info("EmsMeterSafetyController  meterSafetyRecord 参数异常");
            return new Page<>();
        }
        return emsMeterSafetyService.meterSafetyRecord(emsAlarmEvent, request, response);
    }

    /**
     * 设备运行检测报警记录导出
     *
     * @param emsAlarmEvent
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "meterSafetyRecordExport")
    @ResponseBody
    @ApiOperation(value = "设备运行检测报警记录导出", notes = "设备运行检测报警记录导出")
    public String meterSafetyRecordExport(EmsAlarmEvent emsAlarmEvent, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String presignedUrl = null;

        List<EmsAlarmEvent> list = emsMeterSafetyService.meterSafetyRecordExport(emsAlarmEvent);
        if(CollectionUtils.isEmpty(list)){
            return "";
        }
        List<EmsAlarmEventExport> exportList = new ArrayList<>();
        int num = 1;
        for(EmsAlarmEvent en : list){
            EmsAlarmEventExport export = new EmsAlarmEventExport();
            export.setSortNum(num);
            export.setEventTypeMsg(en.getEventTypeMsg());
            export.setDataDateTime(en.getDataDateTime());
            export.setEventContent(en.getEventContent());
            exportList.add(export);
            num++;
        }
        String fileName = "设备运行检测报警记历史记录" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
        try (ExcelExport ee = new ExcelExport("设备运行检测报警记历史记录导出", EmsAlarmEventExport.class)) {
            presignedUrl = ExcelExportUtil.uploadOss(ee.setDataList(exportList), fileName);
        }
        return presignedUrl;
    }
}