package com.jeesite.modules.ems.web;

import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.entity.CompanyVo;
import com.jeesite.modules.ems.entity.EquipmentOnlineEntity;
import com.jeesite.modules.ems.entity.HomePageEntity;
import com.jeesite.modules.ems.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;

/**
 * 首页 Controller
 *
 * @author 李鹏
 * @version 2023-06-12
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/home")
@Api(value = "首页接口", tags = "首页接口")
public class HomePageController extends BaseController {

    @Resource
    private HomePageService homePageService;
    @Resource
    private EmsElectricityConsumptionSummarService emsElectricityConsumptionSummarService;
    @Resource
    private EmsTotalConsumptionTrendSummaryService emsTotalConsumptionTrendSummaryService;
    @Resource
    private EmsConsumptionRankingSummaryService emsConsumptionRankingSummaryService;
    @Resource
    private EmsTimeShareConsumptionTrendSummaryService emsTimeShareConsumptionTrendSummaryService;

    /**
     * 今日累计用电统计
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "todayCumulativeConsumption")
    @ResponseBody
    @ApiOperation(value = "今日累计用电统计", notes = "今日累计用电统计")
    public HomePageEntity todayCumulativeConsumption(HomePageEntity homePageEntity, HttpServletRequest request, HttpServletResponse response) {
        HomePageEntity homePage = emsElectricityConsumptionSummarService.dataHandle(homePageEntity,"1");
//        return homePageService.todayCumulativeConsumption(homePageEntity);
        return homePage;
    }


    /**
     * 本月累计用电统计
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "monthCumulativeConsumption")
    @ResponseBody
    @ApiOperation(value = "本月累计用电统计", notes = "本月累计用电统计")
    public HomePageEntity monthCumulativeConsumption(HomePageEntity homePageEntity, HttpServletRequest request, HttpServletResponse response) throws ParseException {
        HomePageEntity homePage = emsElectricityConsumptionSummarService.dataHandle(homePageEntity,"2");
//         homePageService.monthCumulativeConsumption(homePageEntity);
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
//         homePageService.yearCumulativeConsumption(homePageEntity);
        return homePage;
    }

    /**
     * 总用电曲趋势
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "totalConsumptionTrend")
    @ResponseBody
    @ApiOperation(value = "总用电曲趋势", notes = "总用电曲趋势")
    public HomePageEntity totalConsumptionTrend(HomePageEntity homePageEntity, HttpServletRequest request, HttpServletResponse response) throws ParseException {
//        homePageService.totalConsumptionTrend(homePageEntity);
        return emsTotalConsumptionTrendSummaryService.dataHandle(homePageEntity);
    }

    /**
     * 尖峰平谷用电趋势
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "timeShareConsumptionTrend")
    @ResponseBody
    @ApiOperation(value = "尖峰平谷用电趋势", notes = "尖峰平谷用电趋势")
    public HomePageEntity timeShareConsumptionTrend(HomePageEntity homePageEntity, HttpServletRequest request, HttpServletResponse response) {
//        return homePageService.timeShareConsumptionTrend(homePageEntity);
        return emsTimeShareConsumptionTrendSummaryService.dataHandle(homePageEntity);
    }

    /**
     * 用电量排行榜
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "consumptionRanking")
    @ResponseBody
    @ApiOperation(value = "用电量排行榜", notes = "用电量排行榜")
    public HomePageEntity consumptionRanking(HomePageEntity homePageEntity, HttpServletRequest request, HttpServletResponse response) {
//        return homePageService.consumptionRanking(homePageEntity);
        return emsConsumptionRankingSummaryService.dataHandle(homePageEntity);
    }


    /**
     * 设备在线统计
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "equipmentOnlineStatistics")
    @ResponseBody
    @ApiOperation(value = "设备在线统计", notes = "设备在线统计")
    public List<EquipmentOnlineEntity> equipmentOnlineStatistics(CompanyVo vo , HttpServletRequest request, HttpServletResponse response) {
        return homePageService.equipmentOnlineStatistics(vo);
    }

    /**
     * 预警信息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "warningInformation")
    @ResponseBody
    @ApiOperation(value = "预警信息", notes = "预警信息")
    public HomePageEntity warningInformation(HomePageEntity homePageEntity, HttpServletRequest request, HttpServletResponse response) {
        return homePageService.warningInformation(homePageEntity);
    }


}