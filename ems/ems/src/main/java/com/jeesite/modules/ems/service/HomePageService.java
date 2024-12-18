package com.jeesite.modules.ems.service;

import com.cecec.api.redis.RedisKeyUtil;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.constant.DefaultConstant;
import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.cache.service.RedisService;
import com.jeesite.modules.ems.dao.EmsTerminalDao;
import com.jeesite.modules.ems.entity.*;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.service.support.adapt.GlobalCalculateAdapter;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 首页Service
 *
 * @author 李鹏
 * @version 2023-06-14
 */
@Service
@Transactional(readOnly = true)
public class HomePageService extends CrudService<EmsTerminalDao, EmsTerminal> {

    @Resource
    private GlobalCalculateAdapter calculateAdapter;
    @Resource
    private EmsElectricPowerConsumptionService emsElectricPowerConsumptionService;
    @Resource
    private EmsElectricPowerConsumptionStatisticsService emsElectricPowerConsumptionStatisticsService;
    @Resource
    private EmsTimeSharePowerConsumptionService emsTimeSharePowerConsumptionService;
    @Resource
    private RedisService redisService;
    @Resource
    private EmsTerminalService emsTerminalService;

    public HomePageEntity todayCumulativeConsumption(HomePageEntity homePageEntity) {
        //获取当前公司
        String companyCode = homePageEntity.getCompanyCode();
        TemporalGranularityEnum temporalGranularity = TemporalGranularityEnum.VD_Hour;
        List<String> meterMarkList = EmsUserHelper.getMeterMarkList();

        //当前时间
        Date today = new Date();

        GlobalCalculateAdapter.OfFirst_Last ofDayFirstLast = calculateAdapter.getOfDayFirst_Last();
        //今天最早时间
        Date ofDayFirst = ofDayFirstLast.getOfDateFirst();
        Date ofDayLast = ofDayFirstLast.getOfDateLast();

        Double todayCumulative = emsElectricPowerConsumptionService.getStageCumulativeConsumption(StageCumulativeQueryEntity.builder()
                .startTime(ofDayFirst)
                .endTime(today)
                .temporalGranularity(TemporalGranularityEnum.VD_Quarter)
                .meterMarkList(meterMarkList)
                .companyCode(companyCode)
                .build());
        //查询今日此刻电耗数据
        List<EChartData> todayEChartData = emsElectricPowerConsumptionService.getStageConsumption2EChart(StageCumulativeQueryEntity.builder()
                .startTime(ofDayFirst)
                .endTime(ofDayLast)
                .temporalGranularity(temporalGranularity)
                .format("%H:%i")
                .meterMarkList(meterMarkList)
                .companyCode(companyCode)
                .build());
        Map<String, EChartData> todayMap = todayEChartData.stream().collect(Collectors.toMap(EChartData::getLabel, EChartData -> EChartData, (key1, key2) -> key2, LinkedHashMap::new));

        //昨日同时段电耗统计
        Date ofYesterdayFirst = DateUtils.calculateDay(ofDayFirst, -1);
        Date ofYesterdayLast = DateUtils.calculateDay(ofDayLast, -1);
        Date yesterday = DateUtils.calculateDay(today, -1);
        Double yesterdayCumulative = emsElectricPowerConsumptionService.getStageCumulativeConsumption(StageCumulativeQueryEntity.builder()
                .startTime(ofYesterdayFirst)
                .endTime(yesterday)
                .temporalGranularity(TemporalGranularityEnum.VD_Quarter)
                .meterMarkList(meterMarkList)
                .companyCode(companyCode)
                .build());
        Double yesterdayQoQ = calculateQoQ(todayCumulative, yesterdayCumulative);
        //查询昨日同时段电耗数据
        List<EChartData> yesterdayEChartData = emsElectricPowerConsumptionService.getStageConsumption2EChart(StageCumulativeQueryEntity.builder()
                .startTime(ofYesterdayFirst)
                .endTime(ofYesterdayLast)
                .temporalGranularity(temporalGranularity)
                .format("%H:%i")
                .meterMarkList(meterMarkList)
                .companyCode(companyCode)
                .build());
        Map<String, EChartData> yesterdayMap = yesterdayEChartData.stream().collect(Collectors.toMap(EChartData::getLabel, EChartData -> EChartData, (key1, key2) -> key2, LinkedHashMap::new));

        //上周同时段电耗统计
        Date lastWeek = DateUtils.calculateDay(today, -7);
        Date ofLastWeekFirst = DateUtils.calculateDay(ofDayFirst, -7);
        Double lastWeekCumulative = emsElectricPowerConsumptionService.getStageCumulativeConsumption(StageCumulativeQueryEntity.builder()
                .startTime(ofLastWeekFirst)
                .endTime(lastWeek)
                .temporalGranularity(TemporalGranularityEnum.VD_Quarter)
                .meterMarkList(meterMarkList)
                .companyCode(companyCode)
                .build());
        Double lastWeekQoQ = calculateQoQ(todayCumulative, lastWeekCumulative);

        String modelTopic = DateUtils.formatDate(ofDayFirst, "HH:mm") + " 至 " + DateUtils.formatDate(today, "HH:mm");
        TodayCumulativeEntity data = TodayCumulativeEntity.builder()
                .modelTopic(modelTopic)
                .todayCumulative(todayCumulative)
                .lastWeekQoQ(lastWeekQoQ)
                .yesterdayQoQ(yesterdayQoQ)
                .build();

        String dayKey = DateUtils.formatDate(today, "MM-dd");
        String lastDayKey = DateUtils.formatDate(yesterday, "MM-dd");
        Map<String,List<String>> clockMap = clockMap(temporalGranularity, calculateAdapter.getOfDayFirst_Last(), dayKey, lastDayKey);
        //组装echart数据
        EChart eChart = buildEChartBody(temporalGranularity, dayKey, todayMap, lastDayKey, yesterdayMap, clockMap);
        //初始化模块数据
        homePageEntity.setData(data);
        //初始化报表数据
        homePageEntity.setEChart(eChart);
        return homePageEntity;
    }

    /**
     * 计算环比
     *
     * @param atPresent
     * @param historical
     * @return
     */
    private Double calculateQoQ(Double atPresent, Double historical) {
        if (Objects.isNull(atPresent) || Objects.isNull(historical)) {
            return DefaultConstant.DOUBLE_DEFAULT;
        }
        //转换成BigDecimal计算
        BigDecimal atPresentDcl = new BigDecimal(atPresent);
        BigDecimal historicalDcl = new BigDecimal(historical);
        if (BigDecimal.ZERO.compareTo(historicalDcl) == 0) {
            return DefaultConstant.DOUBLE_DEFAULT;
        }
        return atPresentDcl.subtract(historicalDcl)
                .divide(historicalDcl, 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100))
                .doubleValue();
    }

    public HomePageEntity monthCumulativeConsumption(HomePageEntity homePageEntity) throws ParseException {
        //获取公司code
        String companyCode = homePageEntity.getCompanyCode();
        TemporalGranularityEnum temporalGranularity = TemporalGranularityEnum.VD_Day;
        List<String> meterMarkList = EmsUserHelper.getMeterMarkList();

        //当前时间
        GlobalCalculateAdapter.OfFirst_Last ofMonthFirstLast = calculateAdapter.getOfMonthFirst_Last();
        String monthKey = ofMonthFirstLast.getBusiKey();
        Date ofMonthFirst = ofMonthFirstLast.getOfDateFirst();
        Date ofMonthEnd = new Date();
        Double thisCumulative = emsElectricPowerConsumptionService.getStageCumulativeConsumption(StageCumulativeQueryEntity.builder()
                .startTime(ofMonthFirst)
                .endTime(ofMonthEnd)
                .temporalGranularity(temporalGranularity)
                .meterMarkList(meterMarkList)
                .companyCode(companyCode)
                .build());
        //查询今日此刻电耗数据
        List<EChartData> thisEChartData = emsElectricPowerConsumptionService.getStageConsumption2EChart(StageCumulativeQueryEntity.builder()
                .startTime(ofMonthFirst)
                .endTime(ofMonthEnd)
                .temporalGranularity(temporalGranularity)
                .format("%m-%d")
                .meterMarkList(meterMarkList)
                .companyCode(companyCode)
                .build());
        Map<String, EChartData> thisMap = thisEChartData.stream().collect(Collectors.toMap(EChartData::getLabel, EChartData -> EChartData, (key1, key2) -> key2, LinkedHashMap::new));

        //上月同时段电耗统计
        GlobalCalculateAdapter.OfFirst_Last lastMonth = calculateAdapter.appointOfMonthFirst_Last(Integer.parseInt(DateUtils.getMonth(DateUtils.parseDate(monthKey, "yyyy-MM"))) - 1);
        String lastMonthKey = lastMonth.getBusiKey();
        Date lastMonthFirst = lastMonth.getOfDateFirst();
        Date lastMonthEnd = DateUtils.calculateMonth(ofMonthEnd, -1);
        Date lastMonthLast = lastMonth.getOfDateLast();
        Double lastCumulative = emsElectricPowerConsumptionService.getStageCumulativeConsumption(StageCumulativeQueryEntity.builder()
                .startTime(lastMonthFirst)
                .endTime(lastMonthEnd)
                .temporalGranularity(temporalGranularity)
                .meterMarkList(meterMarkList)
                .companyCode(companyCode)
                .build());
        Double lastQoQ = calculateQoQ(thisCumulative, lastCumulative);
        //查询上月同时段电耗数据
        List<EChartData> lastEChartData = emsElectricPowerConsumptionService.getStageConsumption2EChart(StageCumulativeQueryEntity.builder()
                .startTime(lastMonthFirst)
                .endTime(lastMonthLast)
                .temporalGranularity(temporalGranularity)
                .format("%m-%d")
                .meterMarkList(meterMarkList)
                .companyCode(companyCode)
                .build());
        Map<String, EChartData> lastMap = lastEChartData.stream().collect(Collectors.toMap(EChartData::getLabel, EChartData -> EChartData, (key1, key2) -> key2, LinkedHashMap::new));

        String modelTopic = DateUtils.formatDate(ofMonthFirst, "MM-dd") + " 至 " + DateUtils.formatDate(new Date(), "MM-dd");
        MonthCumulativeEntity data = MonthCumulativeEntity.builder()
                .modelTopic(modelTopic)
                .monthCumulative(thisCumulative)
                .lastMonthQoQ(lastQoQ)
                .build();

        Map<String,List<String>> clockMap = clockMap(temporalGranularity, ofMonthFirstLast, monthKey, lastMonthKey);
        //组装echart数据
        EChart eChart = buildEChartBody(temporalGranularity, monthKey, thisMap, lastMonthKey, lastMap, clockMap);
        //初始化模块数据
        homePageEntity.setData(data);
        //初始化报表数据
        homePageEntity.setEChart(eChart);
        return homePageEntity;
    }

    public HomePageEntity yearCumulativeConsumption(HomePageEntity homePageEntity) {
        //获取前端传过来的公司code
        String companyCode = homePageEntity.getCompanyCode();
        TemporalGranularityEnum temporalGranularity = TemporalGranularityEnum.VD_Month;
        List<String> meterMarkList = EmsUserHelper.getMeterMarkList();

        //当前时间
        GlobalCalculateAdapter.OfFirst_Last ofYearFirstLast = calculateAdapter.getOfYearFirst_Last();
        String yearKey = ofYearFirstLast.getBusiKey();
//        Date ofYearFirst = ofYearFirstLast.getOfDateFirst();
        Date ofYearEnd = ofYearFirstLast.getOfDateLast();
        int year = LocalDateTime.now().getYear();
        //得到今年第一天
        LocalDate localDate = LocalDate.ofYearDay(year, 1);
        //转date
        Date ofYearFirst = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Double thisCumulative = emsElectricPowerConsumptionStatisticsService.getStageCumulativeConsumption(StageCumulativeQueryEntity.builder()
                .startKey(DateUtils.formatDate(ofYearFirst, "yyyy-MM"))
                .endKey(DateUtils.formatDate(ofYearEnd, "yyyy-MM"))
                .temporalGranularity(temporalGranularity)
                .meterMarkList(meterMarkList)
                .companyCode(companyCode)
                .build());
        //查询今日此刻电耗数据
        List<EChartData> thisEChartData = emsElectricPowerConsumptionStatisticsService.getStageConsumption2EChart(StageCumulativeQueryEntity.builder()
                .startKey(DateUtils.formatDate(ofYearFirst, "yyyy-MM"))
                .endKey(DateUtils.formatDate(ofYearEnd, "yyyy-MM"))
                .temporalGranularity(temporalGranularity)
                .meterMarkList(meterMarkList)
                .companyCode(companyCode)
                .build());
        Map<String, EChartData> thisMap = thisEChartData.stream().collect(Collectors.toMap(EChartData::getLabel, EChartData -> EChartData, (key1, key2) -> key2, LinkedHashMap::new));

        //上月同时段电耗统计
        GlobalCalculateAdapter.OfFirst_Last lastYearFirstLast = calculateAdapter.getOfYearFirst_Last(DateUtils.calculateYear(ofYearEnd, -1));
        String lastYearKey = lastYearFirstLast.getBusiKey();
        Date lastYearFirst = lastYearFirstLast.getOfDateFirst();
        Date lastYearEnd = DateUtils.calculateYear(ofYearEnd, -1);
        Date lastYearLast = lastYearFirstLast.getOfDateLast();
        Double lastCumulative = emsElectricPowerConsumptionStatisticsService.getStageCumulativeConsumption(StageCumulativeQueryEntity.builder()
                .startKey(DateUtils.formatDate(lastYearFirst, "yyyy-MM"))
                .endKey(DateUtils.formatDate(lastYearEnd, "yyyy-MM"))
                .temporalGranularity(temporalGranularity)
                .meterMarkList(meterMarkList)
                .companyCode(companyCode)
                .build());
        Double lastQoQ = calculateQoQ(thisCumulative, lastCumulative);
        //查询昨日同时段电耗数据
        List<EChartData> lastEChartData = emsElectricPowerConsumptionStatisticsService.getStageConsumption2EChart(StageCumulativeQueryEntity.builder()
                .startKey(DateUtils.formatDate(lastYearFirst, "yyyy-MM"))
                .endKey(DateUtils.formatDate(lastYearLast, "yyyy-MM"))
                .temporalGranularity(temporalGranularity)
                .meterMarkList(meterMarkList)
                .companyCode(companyCode)
                .build());
        Map<String, EChartData> lastMap = lastEChartData.stream().collect(Collectors.toMap(EChartData::getLabel, EChartData -> EChartData, (key1, key2) -> key2, LinkedHashMap::new));

        String modelTopic = DateUtils.formatDate(ofYearFirst, "MM-dd") + " 至 " + DateUtils.formatDate(new Date(), "MM-dd");
        YearCumulativeEntity data = YearCumulativeEntity.builder()
                .modelTopic(modelTopic)
                .yearCumulative(thisCumulative)
                .lastYearQoQ(lastQoQ)
                .build();

        Map<String,List<String>> clockMap = clockMap(temporalGranularity, ofYearFirstLast, yearKey, lastYearKey);
        //组装echart数据
        EChart eChart = buildEChartBody(temporalGranularity, yearKey, thisMap, lastYearKey, lastMap, clockMap);
        //初始化模块数据
        homePageEntity.setData(data);
        //初始化报表数据
        homePageEntity.setEChart(eChart);
        return homePageEntity;
    }

    private List<String> clockList(TemporalGranularityEnum temporalGranularity, Date startDate, Date endDate){
        List<String> l = new ArrayList<>();
        LocalDateTime startTime = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endTime = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        long days = Duration.between(startTime, endTime).toDays();
        switch (temporalGranularity){
            case VD_Quarter:
                for (int i = 15; i <= 24 * 60; i+=15) {
                    l.add(startTime.plusMinutes(i).format(DateTimeFormatter.ofPattern("HH:mm")));
                }
                break;
            case VD_Hour:
                for (int i = 1; i <= 24 * 60; i+=60) {
                    l.add(startTime.plusMinutes(i).format(DateTimeFormatter.ofPattern("HH:mm")));
                }
                break;
            case VD_Day:
                for (int i = 0; i < days; i++) {
                    l.add(startTime.plusDays(i).format(DateTimeFormatter.ofPattern("MM-dd")));
                }
                break;
            case VD_Month:
                for (int i = 1; i <= 12; i++) {
                    l.add(startTime.plusMonths(i).format(DateTimeFormatter.ofPattern("yyyy-MM")));
                }
                break;
        }
        return l;
    }

    private Map<String, List<String>> clockMap(TemporalGranularityEnum temporalGranularity, GlobalCalculateAdapter.OfFirst_Last calculate, String thisKey, String lastKey){

        Date ofDateFirst = calculate.getOfDateFirst();
        Date ofDateLast = calculate.getOfDateLast();

        LocalDateTime startTime = ofDateFirst.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endTime = ofDateLast.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();


        Map<String, List<String>> map = new LinkedHashMap<>();
        map.put(thisKey, new ArrayList<>());
        map.put(lastKey, new ArrayList<>());
        switch (temporalGranularity){
            case VD_Quarter:
                for (int i = 15; i <= 24 * 60; i += 15) {
                    map.get(thisKey).add(startTime.plusMinutes(i).format(DateTimeFormatter.ofPattern("HH:mm")));
                }
                map.put(lastKey, map.get(thisKey));
                break;
            case VD_Hour:
                for (int i = 1; i <= 24; i ++) {
                    map.get(thisKey).add(startTime.plusHours(i).format(DateTimeFormatter.ofPattern("HH:mm")));
                }
                map.put(lastKey, map.get(thisKey));
                break;
            case VD_Day:
                long days = Duration.between(startTime, endTime).toDays();
                for (int i = 0; i < days; i++) {
                    LocalDateTime thisDate = startTime.plusDays(i);
                    String thisDateStr = thisDate.format(DateTimeFormatter.ofPattern("MM-dd"));
                    map.get(thisKey).add(thisDateStr);
                    LocalDateTime lastDate = thisDate.minusMonths(1);
                    String monthValue = lastDate.format(DateTimeFormatter.ofPattern("MM"));
                    String dateValue = thisDateStr.substring(2);
                    map.get(lastKey).add(monthValue + dateValue);
                }
                break;
            case VD_Month:
                for (int i = 1; i <= 12; i++) {
                    map.get(thisKey).add(startTime.plusMonths(i).format(DateTimeFormatter.ofPattern("yyyy-MM")));
                }
                LocalDateTime lastYearTime = startTime.minusYears(1);
                for (int i = 1; i <= 12; i++) {
                    map.get(lastKey).add(lastYearTime.plusMonths(i).format(DateTimeFormatter.ofPattern("yyyy-MM")));
                }
                break;
        }
        return map;
    }

    private EChart buildEChartBody(TemporalGranularityEnum temporalGranularity, String thisKey, Map<String, EChartData> thisMap, String lastKey, Map<String, EChartData> lastMap, Map<String, List<String>> clockMap) {
        ArrayList<String> yAxes0 = ListUtils.newArrayList();
        ArrayList<String> yAxes1 = ListUtils.newArrayList();
        ArrayList<String> xAxes = ListUtils.newArrayList();
        for (String s : clockMap.get(thisKey)) {
            EChartData t = thisMap.get(s);//今天数据
            yAxes0.add(Objects.nonNull(t) ? t.getValue() : "-");
            xAxes.add(s.equals("00:00") ? "24:00" : s);
        }
        for (String s : clockMap.get(lastKey)) {
            EChartData y = lastMap.get(s);//昨天数据
            yAxes1.add(Objects.nonNull(y) ? y.getValue() : "-");
        }
        EChartBody body = new EChartBody();
        body.setX(xAxes);
        body.setY(ListUtils.newArrayList(new EChartItem(thisKey, yAxes0), new EChartItem(lastKey, yAxes1)));
        //组装最后返回结果
        return EChart.builder()
                .temporalGranularity(temporalGranularity)
                .body(body)
                .build();
    }

    public HomePageEntity totalConsumptionTrend(HomePageEntity homePageEntity) throws ParseException {
        //获取前端传过来的公司code
        String companyCode = homePageEntity.getCompanyCode();
        EChart eChart = null;
        //获取查询维度
        TemporalGranularityEnum temporalGranularity = homePageEntity.getEChart().getTemporalGranularity();
        List<String> meterMarkList = EmsUserHelper.getMeterMarkList();
        Map<String, List<String>> clockMap;
        switch (temporalGranularity) {
            case VD_Quarter:
            case VD_Hour:
                GlobalCalculateAdapter.OfFirst_Last ofDayFirstLast = calculateAdapter.getOfDayFirst_Last();
                Date ofDayFirst = ofDayFirstLast.getOfDateFirst();
                Date ofDayLast = ofDayFirstLast.getOfDateLast();
                //查询今日此刻电耗数据
                List<EChartData> todayEChartData = emsElectricPowerConsumptionService.getStageConsumption2EChart(StageCumulativeQueryEntity.builder()
                        .startTime(ofDayFirst)
                        .endTime(ofDayLast)
                        .temporalGranularity(temporalGranularity)
                        .format("%H:%i")
                        .meterMarkList(meterMarkList)
                        .companyCode(companyCode)
                        .build());
                Map<String, EChartData> todayMap = new HashMap<>();
                if(!CollectionUtils.isEmpty(todayEChartData)) {
                    todayMap = todayEChartData.stream().collect(Collectors.toMap(EChartData::getLabel, EChartData -> EChartData, (key1, key2) -> key2, LinkedHashMap::new));
                }
                Date ofYesterdayFirst = DateUtils.calculateDay(ofDayFirst, -1);
                Date ofYesterdayLast = DateUtils.calculateDay(ofDayLast, -1);
                //查询昨日同时段电耗数据
                List<EChartData> yesterdayEChartData = emsElectricPowerConsumptionService.getStageConsumption2EChart(StageCumulativeQueryEntity.builder()
                        .startTime(ofYesterdayFirst)
                        .endTime(ofYesterdayLast)
                        .temporalGranularity(temporalGranularity)
                        .format("%H:%i")
                        .meterMarkList(meterMarkList)
                        .companyCode(companyCode)
                        .build());
                Map<String, EChartData> yesterdayMap = new HashMap<>();
                if(!CollectionUtils.isEmpty(yesterdayEChartData)) {
                    yesterdayMap = yesterdayEChartData.stream().collect(Collectors.toMap(EChartData::getLabel, EChartData -> EChartData, (key1, key2) -> key2, LinkedHashMap::new));
                }
                String dayKey = DateUtils.formatDate(ofDayFirst, "MM-dd");
                String lastDayKey = DateUtils.formatDate(ofYesterdayFirst, "MM-dd");
                clockMap = clockMap(temporalGranularity, ofDayFirstLast, dayKey, lastDayKey);
                //组装echart数据
                eChart = buildEChartBody(temporalGranularity, dayKey, todayMap, lastDayKey, yesterdayMap, clockMap);
                break;
            case VD_Day:
                GlobalCalculateAdapter.OfFirst_Last ofMonthFirstLast = calculateAdapter.getOfMonthFirst_Last();
                String monthKey = ofMonthFirstLast.getBusiKey();
                Date ofMonthFirst = ofMonthFirstLast.getOfDateFirst();
                Date ofMonthLast = ofMonthFirstLast.getOfDateLast();
                List<EChartData> dayEChartData = emsElectricPowerConsumptionService.getStageConsumption2EChart(StageCumulativeQueryEntity.builder()
                        .startTime(ofMonthFirst)
                        .endTime(ofMonthLast)
                        .temporalGranularity(temporalGranularity)
                        .format("%m-%d")
                        .meterMarkList(meterMarkList)
                        .companyCode(companyCode)
                        .build());
                Map<String, EChartData> dayMap = new HashMap<>();
                if(!CollectionUtils.isEmpty(dayEChartData)) {
                    dayMap = dayEChartData.stream().collect(Collectors.toMap(EChartData::getLabel, EChartData -> EChartData, (key1, key2) -> key2, LinkedHashMap::new));
                }
                GlobalCalculateAdapter.OfFirst_Last lastMonth = calculateAdapter.appointOfMonthFirst_Last(Integer.parseInt(DateUtils.getMonth(DateUtils.parseDate(monthKey, "yyyy-MM"))) - 1);
                String lastMonthKey = lastMonth.getBusiKey();
                Date lastMonthFirst = lastMonth.getOfDateFirst();
                Date lastMonthLast = lastMonth.getOfDateLast();
                List<EChartData> lastDayEChartData = emsElectricPowerConsumptionService.getStageConsumption2EChart(StageCumulativeQueryEntity.builder()
                        .startTime(lastMonthFirst)
                        .endTime(lastMonthLast)
                        .temporalGranularity(temporalGranularity)
                        .format("%m-%d")
                        .meterMarkList(meterMarkList)
                        .companyCode(companyCode)
                        .build());
                Map<String, EChartData> lastDayMap = new HashMap<>();
                if(!CollectionUtils.isEmpty(lastDayEChartData)) {
                     lastDayMap = lastDayEChartData.stream().collect(Collectors.toMap(EChartData::getLabel, EChartData -> EChartData, (key1, key2) -> key2, LinkedHashMap::new));
                }
                clockMap = clockMap(temporalGranularity, ofMonthFirstLast, monthKey, lastMonthKey);
                //组装echart数据
                eChart = buildEChartBody(temporalGranularity, monthKey, dayMap, lastMonthKey, lastDayMap, clockMap);
                break;
            case VD_Month:
                GlobalCalculateAdapter.OfFirst_Last ofYearFirstLast = calculateAdapter.getOfYearFirst_Last();
                String yearKey = ofYearFirstLast.getBusiKey();
                Date ofYearFirst = ofYearFirstLast.getOfDateFirst();
                Date ofYearLast = ofYearFirstLast.getOfDateLast();
                //查询今日此刻电耗数据
                List<EChartData> yearEChartData = emsElectricPowerConsumptionStatisticsService.getStageConsumption2EChart(StageCumulativeQueryEntity.builder()
                        .startKey(DateUtils.formatDate(ofYearFirst, "yyyy-MM"))
                        .endKey(DateUtils.formatDate(ofYearLast, "yyyy-MM"))
                        .temporalGranularity(temporalGranularity)
                        .meterMarkList(meterMarkList)
                        .companyCode(companyCode)
                        .build());
                Map<String, EChartData> yearMap = new HashMap<>();
                if(!CollectionUtils.isEmpty(yearEChartData)) {
                    yearMap = yearEChartData.stream().collect(Collectors.toMap(EChartData::getLabel, EChartData -> EChartData, (key1, key2) -> key2, LinkedHashMap::new));
                }
                //上月同时段电耗统计
                GlobalCalculateAdapter.OfFirst_Last lastYearFirstLast = calculateAdapter.getOfYearFirst_Last(DateUtils.calculateYear(ofYearLast, -1));
                String lastYearKey = lastYearFirstLast.getBusiKey();
                Date lastYearFirst = lastYearFirstLast.getOfDateFirst();
                Date lastYearLast = lastYearFirstLast.getOfDateLast();
                //查询昨日同时段电耗数据
                List<EChartData> lastEChartData = emsElectricPowerConsumptionStatisticsService.getStageConsumption2EChart(StageCumulativeQueryEntity.builder()
                        .startKey(DateUtils.formatDate(lastYearFirst, "yyyy-MM"))
                        .endKey(DateUtils.formatDate(lastYearLast, "yyyy-MM"))
                        .temporalGranularity(temporalGranularity)
                        .meterMarkList(meterMarkList)
                        .companyCode(companyCode)
                        .build());
                Map<String, EChartData> lastMap = new HashMap<>();
                if(!CollectionUtils.isEmpty(lastEChartData)) {
                    lastMap = lastEChartData.stream().collect(Collectors.toMap(EChartData::getLabel, EChartData -> EChartData, (key1, key2) -> key2, LinkedHashMap::new));
                }
                clockMap = clockMap(temporalGranularity, ofYearFirstLast, yearKey, lastYearKey);
                //组装echart数据
                eChart = buildEChartBody(temporalGranularity, yearKey, yearMap, lastYearKey, lastMap, clockMap);
                break;
        }
        homePageEntity.setEChart(eChart);
        return homePageEntity;
    }

    public HomePageEntity timeShareConsumptionTrend(HomePageEntity homePageEntity) {
        //获取公司code
        String companyCode = homePageEntity.getCompanyCode();
        TemporalGranularityEnum temporalGranularity = TemporalGranularityEnum.VD_Day;
        List<String> meterMarkList = EmsUserHelper.getMeterMarkList();
        GlobalCalculateAdapter.OfFirst_Last ofMonthFirstLast = calculateAdapter.getOfMonthFirst_Last();
        Date ofMonthFirst = ofMonthFirstLast.getOfDateFirst();
        Date ofMonthLast = ofMonthFirstLast.getOfDateLast();

        //查询
        EmsTimeSharePowerConsumption where = new EmsTimeSharePowerConsumption();
        where.setDataDateStart(ofMonthFirst);
        where.setDataDateEnd(ofMonthLast);
        where.setDataType(temporalGranularity.getCode());
        where.setMeterMarkList(meterMarkList);
        where.setCompanyCode(companyCode);
        List<EmsTimeSharePowerConsumption> dayData = emsTimeSharePowerConsumptionService.getStageConsumption(where);

        List<String> xAxes = clockList(temporalGranularity, ofMonthFirst, ofMonthLast);//组装X轴
        List<String> cuspAxes = ListUtils.newArrayList();
        List<String> peakAxes = ListUtils.newArrayList();
        List<String> fairAxes = ListUtils.newArrayList();
        List<String> valleyAxes = ListUtils.newArrayList();


        Map<String, EmsTimeSharePowerConsumption> clockMap = new LinkedHashMap<>();
        for (EmsTimeSharePowerConsumption obj : dayData) {
            clockMap.put(DateUtils.formatDate(obj.getDataDate(), "MM-dd"), obj);
        }

        for (String xAxe : xAxes) {
            EmsTimeSharePowerConsumption t = clockMap.get(xAxe);
            cuspAxes.add(Objects.nonNull(t) ? String.valueOf(t.getCuspTimeEnergy()) : "-");
            peakAxes.add(Objects.nonNull(t) ? String.valueOf(t.getPeakTimeEnergy()): "-");
            fairAxes.add(Objects.nonNull(t) ? String.valueOf(t.getFairTimeEnergy()): "-");
            valleyAxes.add(Objects.nonNull(t) ? String.valueOf(t.getValleyTimeEnergy()): "-");
        }

        //组装数据
        EChartBody body = new EChartBody();
        body.setX(xAxes);
        body.setY(ListUtils.newArrayList(
                new EChartItem("尖", cuspAxes),
                new EChartItem("峰", peakAxes),
                new EChartItem("平", fairAxes),
                new EChartItem("谷", valleyAxes)));
        EChart eChart = EChart.builder().body(body).build();
        homePageEntity.setEChart(eChart);
        return homePageEntity;
    }

    public HomePageEntity consumptionRanking(HomePageEntity homePageEntity) {
        //获取公司code
        String companyCode = homePageEntity.getCompanyCode();
        EChart eChart = homePageEntity.getEChart();
        //获取查询维度
        TemporalGranularityEnum temporalGranularity = eChart.getTemporalGranularity();
        List<EChartData> eChartData = null;
        switch (temporalGranularity) {
            case VD_Day:
                //组装查询条件
                Date today = new Date();
                EmsElectricPowerConsumption dayParams = new EmsElectricPowerConsumption();
                dayParams.setDataDateTime(DateUtils.getOfDayFirst(today));
                dayParams.setDataDate(DateUtils.getOfDayFirst(today));
                dayParams.setDataType(temporalGranularity.getCode());
                dayParams.setCompanyCode(companyCode);
                eChartData = emsElectricPowerConsumptionService.consumptionRanking(dayParams);
                break;
            case VD_Month:
                GlobalCalculateAdapter.OfFirst_Last ofMonthFirstLast = calculateAdapter.getOfMonthFirst_Last();
                //组装查询条件
                EmsElectricPowerConsumptionStatistics monthParams = new EmsElectricPowerConsumptionStatistics();
                monthParams.setDataDateKey(ofMonthFirstLast.getBusiKey());
                monthParams.setDataType(temporalGranularity.getCode());
                monthParams.setCompanyCode(companyCode);
                eChartData = emsElectricPowerConsumptionStatisticsService.consumptionRanking(monthParams);
                break;
            case VD_Year:
                GlobalCalculateAdapter.OfFirst_Last ofYearFirstLast = calculateAdapter.getOfYearFirst_Last();
                //组装查询条件
                EmsElectricPowerConsumptionStatistics yearParams = new EmsElectricPowerConsumptionStatistics();
                yearParams.setDataDateKey(ofYearFirstLast.getBusiKey());
                yearParams.setDataType(temporalGranularity.getCode());
                yearParams.setCompanyCode(companyCode);
                eChartData = emsElectricPowerConsumptionStatisticsService.consumptionRanking(yearParams);
                break;
        }
        eChart.setBody(eChartData);
        return homePageEntity;
    }

    public List<EquipmentOnlineEntity> equipmentOnlineStatistics(CompanyVo vo) {
        ArrayList<EquipmentOnlineEntity> rlt = ListUtils.newArrayList();
        //查询所有终端编号
        EmsTerminal emsTerminal = new EmsTerminal();
        emsTerminal.setCompanyCode(vo.getCompanyCode());
        List<String> terminalList = emsTerminalService.findCodes(emsTerminal);
        List<String> terminalKeys = RedisKeyUtil.terminalStatus(terminalList);
        //从redis中查询是否存在缓存，若存在则，在线，若不存在，则不在线
        int onlineNum = 0;
        int totalNum = terminalKeys.size();
        for (String terminalKey : terminalKeys) {
            if (redisService.hasKey(terminalKey)) {
                onlineNum++;
            }
        }
        rlt.add(new EquipmentOnlineEntity("终端", onlineNum, totalNum));
        rlt.add(new EquipmentOnlineEntity("电表", onlineNum, totalNum));

        return rlt;
    }

    public HomePageEntity warningInformation(HomePageEntity homePageEntity) {
        //初始化模块数据
        homePageEntity.setData(null);
        //初始化报表数据
        homePageEntity.setEChart(null);
        return homePageEntity;
    }
}