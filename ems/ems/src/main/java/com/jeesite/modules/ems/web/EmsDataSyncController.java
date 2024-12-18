package com.jeesite.modules.ems.web;

import com.jeesite.modules.ems.entity.HomePageEntity;
import com.jeesite.modules.ems.service.*;
import com.jeesite.modules.sys.service.UserService;
import com.jeesite.modules.sys.utils.CorpUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

@RestController
@RequestMapping(value = "${adminPath}/sync/home")
public class EmsDataSyncController {

    @Resource
    private EmsDataSyncService emsDataSyncService;
    @Resource
    private EmsElectricityConsumptionSummarService emsElectricityConsumptionSummarService;
    @Resource
    private EmsTotalConsumptionTrendSummaryService emsTotalConsumptionTrendSummaryService;
    @Resource
    private EmsConsumptionRankingSummaryService emsConsumptionRankingSummaryService;
    @Resource
    private EmsTimeShareConsumptionTrendSummaryService emsTimeShareConsumptionTrendSummaryService;
    @Resource
    private UserService userService;

    @RequestMapping(value = "testSync")
    @ResponseBody
    public String testSync() throws ParseException {
        CorpUtils.setCurrentCorpCode("SXJT", "山西建投");
        emsDataSyncService.syncTotal();
        emsDataSyncService.syncTodayElectricity();
        emsDataSyncService.synCconsumptionRanking();
        emsDataSyncService.syncTimeShareConsumptionTrend();
        return "同步成功！";
    }

    @RequestMapping(value = "totalConsumptionTrend")
    @ResponseBody
    @ApiOperation(value = "总用电曲趋势", notes = "总用电曲趋势")
    public HomePageEntity totalConsumptionTrend(HomePageEntity homePageEntity) {
        HomePageEntity pageEntity = emsTotalConsumptionTrendSummaryService.dataHandle(homePageEntity);
        return pageEntity;
    }

    @RequestMapping(value = "todayCumulativeConsumption")
    @ResponseBody
    @ApiOperation(value = "今日累计用电统计", notes = "今日累计用电统计")
    public HomePageEntity todayCumulativeConsumption(HomePageEntity homePageEntity, HttpServletRequest request, HttpServletResponse response) {
        HomePageEntity homePage = emsElectricityConsumptionSummarService.dataHandle(homePageEntity,"1");
        return homePage;
    }

    @RequestMapping(value = "monthCumulativeConsumption")
    @ResponseBody
    @ApiOperation(value = "本月累计用电统计", notes = "本月累计用电统计")
    public HomePageEntity monthCumulativeConsumption(HomePageEntity homePageEntity, HttpServletRequest request, HttpServletResponse response) throws ParseException {
        HomePageEntity homePage = emsElectricityConsumptionSummarService.dataHandle(homePageEntity,"2");
        return homePage;
    }

    /**
     * 今年累计用电统计
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "yearCumulativeConsumption")
    @ResponseBody
    @ApiOperation(value = "今年累计用电统计", notes = "今年累计用电统计")
    public HomePageEntity yearCumulativeConsumption(HomePageEntity homePageEntity, HttpServletRequest request, HttpServletResponse response) {
        HomePageEntity homePage = emsElectricityConsumptionSummarService.dataHandle(homePageEntity,"3");
        return homePage;
    }

    @RequestMapping(value = "consumptionRanking")
    @ResponseBody
    @ApiOperation(value = "用电量排行榜", notes = "用电量排行榜")
    public HomePageEntity consumptionRanking(HomePageEntity homePageEntity) {
        return emsConsumptionRankingSummaryService.dataHandle(homePageEntity);
    }

    @RequestMapping(value = "timeShareConsumptionTrend")
    @ResponseBody
    @ApiOperation(value = "尖峰平谷用电趋势", notes = "尖峰平谷用电趋势")
    public HomePageEntity timeShareConsumptionTrend(HomePageEntity homePageEntity) {
        return emsTimeShareConsumptionTrendSummaryService.dataHandle(homePageEntity);
    }
}
