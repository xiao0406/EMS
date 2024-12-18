package com.jeesite.modules.ems.service;

import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.modules.ems.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EmsDataSyncService {

    @Resource
    private HomePageService homePageService;
    @Resource
    private EmsElectricityConsumptionSummarService emsElectricityConsumptionSummarService;
    @Resource
    private EmsTotalConsumptionTrendSummaryService emsTotalConsumptionTrendSummaryService;
    @Resource
    private EmsConsumptionRankingSummaryService emsConsumptionRankingSummaryService;
    @Resource
    private EmsRankingSummaryInfoService emsRankingSummaryInfoService;
    @Resource
    private EmsTimeShareConsumptionTrendSummaryService emsTimeShareConsumptionTrendSummaryService;
    @Resource
    private EmsEquipmentOnlineSummaryService emsEquipmentOnlineSummaryService;
    @Resource
    private EmsTimeSharePowerConsumptionService emsTimeSharePowerConsumptionService;


    /**
     * 用电量统计
     */
    @Transactional
    public void syncTodayElectricity() throws ParseException {
        //获取公司
        List<String> companyCodes = this.getCompanyCodes();
        HomePageEntity<Object, Object, Object> homePageEntity = new HomePageEntity<>();
        //遍历所有查询条件
        for (String companyCode : companyCodes) {
            homePageEntity.setCompanyCode(companyCode);
            this.todayCumulativeConsumption(homePageEntity);
            this.monthCumulativeConsumption(homePageEntity);
            this.yearCumulativeConsumption(homePageEntity);
        }
    }

    /**
     * 尖峰平谷用电趋势汇总
     */
    @Transactional
    public void syncTimeShareConsumptionTrend(){
        //获取公司
        List<String> companyCodes = this.getCompanyCodes();
        HomePageEntity<Object, Object, Object> homePageEntity = new HomePageEntity<>();
        //遍历所有查询条件
        for (String companyCode : companyCodes) {
            homePageEntity.setCompanyCode(companyCode);
            this.timeShareConsumptionTrend(homePageEntity);
        }
    }

    /**
     * 用电量排行
     */
    @Transactional
    public void synCconsumptionRanking(){
        //获取公司
        List<String> companyCodes = this.getCompanyCodes();
        HomePageEntity<Object, Object, Object> homePage = new HomePageEntity<>();
        List<TemporalGranularityEnum> temporalGranularitys = new ArrayList<>();
        temporalGranularitys.add(TemporalGranularityEnum.VD_Day);
        temporalGranularitys.add(TemporalGranularityEnum.VD_Month);
        temporalGranularitys.add(TemporalGranularityEnum.VD_Year);
        //遍历所有查询条件
        for (String companyCode : companyCodes) {
            homePage.setCompanyCode(companyCode);
            EChart<Object, Object> eChart = new EChart<>();
            for (TemporalGranularityEnum temporalGranularity : temporalGranularitys) {
                eChart.setTemporalGranularity(temporalGranularity);
                homePage.setEChart(eChart);
                this.consumptionRanking(homePage);
            }
        }
    }

    /**
     * 总用电趋势统计
     * @throws ParseException
     */
    public void syncTotal() throws ParseException {
        //获取公司
        List<String> companyCodes = this.getCompanyCodes();
        List<TemporalGranularityEnum> temporalGranularitys = new ArrayList<>();
        temporalGranularitys.add(TemporalGranularityEnum.VD_Quarter);
        temporalGranularitys.add(TemporalGranularityEnum.VD_Day);
        temporalGranularitys.add(TemporalGranularityEnum.VD_Month);
        HomePageEntity<Object, Object, Object> homePageEntity = new HomePageEntity<>();
        //遍历所有查询条件
        for (String companyCode : companyCodes) {
            homePageEntity.setCompanyCode(companyCode);
            EChart<Object, Object> eChart = new EChart<>();
            for (TemporalGranularityEnum temporalGranularity : temporalGranularitys) {
                eChart.setTemporalGranularity(temporalGranularity);
                homePageEntity.setEChart(eChart);
                this.totalConsumptionTrend(homePageEntity);
            }
        }
    }

    //只保存电表数据
    @Transactional
    public void equipmentOnlineStatistics(){
        //获取公司
        List<String> companyCodes = this.getCompanyCodes();
        for (String companyCode : companyCodes) {
            CompanyVo companyVo = new CompanyVo();
            companyVo.setCompanyCode(companyCode);
            List<EquipmentOnlineEntity> equipmentOnlineEntities = homePageService.equipmentOnlineStatistics(companyVo);
            for (EquipmentOnlineEntity equipmentOnlineEntity : equipmentOnlineEntities) {
                    equipmentOnlineSave(equipmentOnlineEntity,companyCode);
            }
        }
    }

    public void equipmentOnlineSave(EquipmentOnlineEntity equipmentOnlineEntity, String companyCode) {
        EmsEquipmentOnlineSummary where = new EmsEquipmentOnlineSummary();
        where.setCompanyCode(companyCode);
        where.setEquipmentType(equipmentOnlineEntity.getEquipmentType());
        List<EmsEquipmentOnlineSummary> summaries = emsEquipmentOnlineSummaryService.findList(where);
        EmsEquipmentOnlineSummary onlineSummary = new EmsEquipmentOnlineSummary();
        onlineSummary.setCompanyCode(companyCode);
        onlineSummary.setEquipmentType(equipmentOnlineEntity.getEquipmentType());
        onlineSummary.setOnlineNum(equipmentOnlineEntity.getOnlineNum());
        onlineSummary.setTotalnum(equipmentOnlineEntity.getTotalNum());
        if (CollectionUtils.isEmpty(summaries)) {
            onlineSummary.setIsNewRecord(true);
            emsEquipmentOnlineSummaryService.save(onlineSummary);
        } else {
            //有数据也只会查询出一条数据
            EmsEquipmentOnlineSummary emsEquipmentOnlineSummary = summaries.get(0);
            onlineSummary.setId(emsEquipmentOnlineSummary.getId());
            onlineSummary.setIsNewRecord(false);
            emsEquipmentOnlineSummaryService.save(onlineSummary);
        }

    }

    public void timeShareConsumptionTrend(HomePageEntity homePageEntity) {
        HomePageEntity homePage = homePageService.timeShareConsumptionTrend(homePageEntity);
        DataSummary dataSummary = this.dataSplit(homePage);
        EmsTimeShareConsumptionTrendSummary where = new EmsTimeShareConsumptionTrendSummary();
        where.setCompanyCode(homePageEntity.getCompanyCode());
        List<EmsTimeShareConsumptionTrendSummary> list = emsTimeShareConsumptionTrendSummaryService.findList(where);
        EmsTimeShareConsumptionTrendSummary trendSummary = new EmsTimeShareConsumptionTrendSummary();
        trendSummary.setCompanyCode(homePage.getCompanyCode());
        trendSummary.setXaxis(dataSummary.getXaxis());
        trendSummary.setYaxis(dataSummary.getYaxis());
        trendSummary.setLable(dataSummary.getLable());
        if (CollectionUtils.isEmpty(list)){
            emsTimeShareConsumptionTrendSummaryService.insert(trendSummary);
        }else {
            trendSummary.setId(list.get(0).getId());
            emsTimeShareConsumptionTrendSummaryService.update(trendSummary);
        }
    }

    public void consumptionRanking(HomePageEntity homePageEntity) {
        HomePageEntity homePage = homePageService.consumptionRanking(homePageEntity);
        EChart eChart = homePage.getEChart();
        int i=1;
        List<EChartData> body = (List<EChartData>) eChart.getBody();
        EmsConsumptionRankingSummary where = new EmsConsumptionRankingSummary();
        where.setCompanyCode(homePageEntity.getCompanyCode());
        where.setTemporalgranularity(homePageEntity.getEChart().getTemporalGranularity().getCode());
        EmsConsumptionRankingSummary rankingSummary = emsConsumptionRankingSummaryService.findOne(where);
        if (rankingSummary == null){
            String nextId = IdGen.nextId();
            where.setId(nextId);
            emsConsumptionRankingSummaryService.insert(where);
            ArrayList<EmsRankingSummaryInfo> summaryInfos = new ArrayList<>();
            for (EChartData chartData : body) {
                EmsRankingSummaryInfo summaryInfo = new EmsRankingSummaryInfo();
                summaryInfo.setLabel(chartData.getLabel());
                summaryInfo.setValue(chartData.getValue());
                summaryInfo.setRankingSummaryId(nextId);
                summaryInfo.setSort(String.valueOf(i++));
                summaryInfos.add(summaryInfo);
            }
            if (!CollectionUtils.isEmpty(summaryInfos)){
                emsRankingSummaryInfoService.insertBatch(summaryInfos);
            }
        }else {
            EmsConsumptionRankingSummary update = new EmsConsumptionRankingSummary();
            update.setId(rankingSummary.getId());
            update.setUpdateDate(new Date());
            emsConsumptionRankingSummaryService.update(update);
            //先删除关联记录
            EmsRankingSummaryInfo deleteWhere = new EmsRankingSummaryInfo();
            deleteWhere.setRankingSummaryId(rankingSummary.getId());
            emsRankingSummaryInfoService.deleteBySummaryId(deleteWhere);
            ArrayList<EmsRankingSummaryInfo> summaryInfos = new ArrayList<>();
            for (EChartData chartData : body) {
                EmsRankingSummaryInfo summaryInfo = new EmsRankingSummaryInfo();
                summaryInfo.setLabel(chartData.getLabel());
                summaryInfo.setValue(chartData.getValue());
                summaryInfo.setRankingSummaryId(rankingSummary.getId());
                summaryInfo.setSort(String.valueOf(i++));
                summaryInfos.add(summaryInfo);
            }
            if (!CollectionUtils.isEmpty(summaryInfos)){
                emsRankingSummaryInfoService.insertBatch(summaryInfos);
            }
        }

    }

    @Transactional
    public void todayCumulativeConsumption(HomePageEntity homePageEntity) {
        HomePageEntity homePage = homePageService.todayCumulativeConsumption(homePageEntity);
        DataSummary dataSummary = this.dataSplit(homePage);
        TodayCumulativeEntity data = (TodayCumulativeEntity) homePage.getData();
        EmsElectricityConsumptionSummar where = new EmsElectricityConsumptionSummar();
        where.setCompanyCode(homePageEntity.getCompanyCode());
        where.setFindType("1");
        EmsElectricityConsumptionSummar summar = emsElectricityConsumptionSummarService.findOne(where);
        if (summar == null){
            todayData(dataSummary, data, where);
            emsElectricityConsumptionSummarService.insert(where);
        }else {
            EmsElectricityConsumptionSummar consumptionSummar = new EmsElectricityConsumptionSummar();
            consumptionSummar.setLable(dataSummary.getLable());
            consumptionSummar.setXaxis(dataSummary.getXaxis());
            consumptionSummar.setYaxis(dataSummary.getYaxis());
            consumptionSummar.setCumulative(data.getTodayCumulative());
            consumptionSummar.setLastqoq(data.getLastWeekQoQ());
            consumptionSummar.setYesterdayqoq(data.getYesterdayQoQ());
            consumptionSummar.setModeltopic(data.getModelTopic());
            consumptionSummar.setFindType("1");
            consumptionSummar.setCompanyCode(homePageEntity.getCompanyCode());
            consumptionSummar.setId(summar.getId());
            emsElectricityConsumptionSummarService.update(consumptionSummar);
        }
    }
    public void monthCumulativeConsumption(HomePageEntity homePageEntity) throws ParseException {
        HomePageEntity homePage = homePageService.monthCumulativeConsumption(homePageEntity);
        DataSummary dataSummary = this.dataSplit(homePage);
        MonthCumulativeEntity data = (MonthCumulativeEntity) homePage.getData();
        EmsElectricityConsumptionSummar where = new EmsElectricityConsumptionSummar();
        where.setCompanyCode(homePageEntity.getCompanyCode());
        where.setFindType("2");
        EmsElectricityConsumptionSummar summar = emsElectricityConsumptionSummarService.findOne(where);
        if (summar == null){
            where.setLable(dataSummary.getLable());
            where.setXaxis(dataSummary.getXaxis());
            where.setYaxis(dataSummary.getYaxis());
            where.setCumulative(data.getMonthCumulative());
            where.setLastqoq(data.getLastMonthQoQ());
            where.setModeltopic(data.getModelTopic());
            emsElectricityConsumptionSummarService.insert(where);
        }else {
            EmsElectricityConsumptionSummar consumptionSummar = new EmsElectricityConsumptionSummar();
            consumptionSummar.setLable(dataSummary.getLable());
            consumptionSummar.setXaxis(dataSummary.getXaxis());
            consumptionSummar.setYaxis(dataSummary.getYaxis());
            consumptionSummar.setCumulative(data.getMonthCumulative());
            consumptionSummar.setLastqoq(data.getLastMonthQoQ());
            consumptionSummar.setModeltopic(data.getModelTopic());
            consumptionSummar.setFindType("2");
            consumptionSummar.setCompanyCode(homePageEntity.getCompanyCode());
            consumptionSummar.setId(summar.getId());
            emsElectricityConsumptionSummarService.update(consumptionSummar);
        }
    }

    public void yearCumulativeConsumption(HomePageEntity homePageEntity) throws ParseException{
        HomePageEntity homePage = homePageService.yearCumulativeConsumption(homePageEntity);
        DataSummary dataSummary = this.dataSplit(homePage);
        YearCumulativeEntity data = (YearCumulativeEntity) homePage.getData();
        EmsElectricityConsumptionSummar where = new EmsElectricityConsumptionSummar();
        where.setCompanyCode(homePageEntity.getCompanyCode());
        where.setFindType("3");
        EmsElectricityConsumptionSummar summar = emsElectricityConsumptionSummarService.findOne(where);
        if (summar == null){
            where.setLable(dataSummary.getLable());
            where.setXaxis(dataSummary.getXaxis());
            where.setYaxis(dataSummary.getYaxis());
            where.setCumulative(data.getYearCumulative());
            where.setLastqoq(data.getLastYearQoQ());
            where.setModeltopic(data.getModelTopic());
            emsElectricityConsumptionSummarService.insert(where);
        }else {
            EmsElectricityConsumptionSummar consumptionSummar = new EmsElectricityConsumptionSummar();
            consumptionSummar.setLable(dataSummary.getLable());
            consumptionSummar.setXaxis(dataSummary.getXaxis());
            consumptionSummar.setYaxis(dataSummary.getYaxis());
            consumptionSummar.setCumulative(data.getYearCumulative());
            consumptionSummar.setLastqoq(data.getLastYearQoQ());
            consumptionSummar.setModeltopic(data.getModelTopic());
            consumptionSummar.setFindType("3");
            consumptionSummar.setCompanyCode(homePageEntity.getCompanyCode());
            consumptionSummar.setId(summar.getId());
            emsElectricityConsumptionSummarService.update(consumptionSummar);
        }
    }

    @Transactional
    public void totalConsumptionTrend(HomePageEntity homePageEntity) throws ParseException {
        HomePageEntity homePage = homePageService.totalConsumptionTrend(homePageEntity);
        DataSummary dataSummary = this.dataSplit(homePage);
        EmsTotalConsumptionTrendSummary where = new EmsTotalConsumptionTrendSummary();
        where.setCompanyCode(homePageEntity.getCompanyCode());
        where.setTemporalgranularity(homePageEntity.getEChart().getTemporalGranularity().getCode());
        EmsTotalConsumptionTrendSummary one = emsTotalConsumptionTrendSummaryService.findOne(where);
        if (one == null) {
            EmsTotalConsumptionTrendSummary emsTotalConsumptionTrendSummary = new EmsTotalConsumptionTrendSummary();
            emsTotalConsumptionTrendSummary.setCompanyCode(homePageEntity.getCompanyCode());
            emsTotalConsumptionTrendSummary.setTemporalgranularity(homePageEntity.getEChart().getTemporalGranularity().getCode());
            emsTotalConsumptionTrendSummary.setXaxis(dataSummary.getXaxis());
            emsTotalConsumptionTrendSummary.setYaxis(dataSummary.getYaxis());
            emsTotalConsumptionTrendSummary.setLable(dataSummary.getLable());
            emsTotalConsumptionTrendSummaryService.insert(emsTotalConsumptionTrendSummary);
        } else {
            one.setXaxis(dataSummary.getXaxis());
            one.setYaxis(dataSummary.getYaxis());
            one.setLable(dataSummary.getLable());
            emsTotalConsumptionTrendSummaryService.update(one);
        }
    }

    public DataSummary dataSplit(HomePageEntity homePage) {
        EChart eChart = homePage.getEChart();
        EChartBody eChartBody = (EChartBody) eChart.getBody();
        List x = eChartBody.getX();
        List y = eChartBody.getY();
        //转换为字符串
        StringBuffer stringBufferX = new StringBuffer();
        for (int i = 0; i < x.size(); i++) {
            stringBufferX.append(x.get(i));
            if (i != x.size() - 1) {
                stringBufferX.append(",");
            }
        }
        StringBuffer stringBufferY = new StringBuffer();
        StringBuffer stringBufferLable = new StringBuffer();
        for (int i = 0; i < y.size(); i++) {
            EChartItem o = (EChartItem) y.get(i);
            stringBufferLable.append(o.getLable());
            stringBufferLable.append(",");
            List values = (List) o.getValue();
            for (int i1 = 0; i1 < values.size(); i1++) {
                stringBufferY.append(values.get(i1));
                if (i1 != values.size() - 1) {
                    stringBufferY.append(",");
                }
            }
            stringBufferY.append("Y");
        }
        DataSummary dataSummary = new DataSummary();
        dataSummary.setXaxis(stringBufferX.toString());
        dataSummary.setYaxis(stringBufferY.toString());
        dataSummary.setLable(stringBufferLable.toString());
        return dataSummary;
    }

    private List<String> getCompanyCodes(){
        List<String> companyCodes = new ArrayList<>();
        //江苏
        companyCodes.add("0001A110000000002ZMI");
        //广东
        companyCodes.add("0001A110000000002ZWA");
        //武汉
        companyCodes.add("0001A11000000000DWAB");
        //四川
        companyCodes.add("0001A11000000000DWBB");
        //天津
        companyCodes.add("0001A11000000000DWF3");
        return companyCodes;
    }

    private void todayData(DataSummary dataSummary, TodayCumulativeEntity data, EmsElectricityConsumptionSummar consumptionSummar) {
        consumptionSummar.setLable(dataSummary.getLable());
        consumptionSummar.setXaxis(dataSummary.getXaxis());
        consumptionSummar.setYaxis(dataSummary.getYaxis());
        consumptionSummar.setCumulative(data.getTodayCumulative());
        consumptionSummar.setLastqoq(data.getLastWeekQoQ());
        consumptionSummar.setYesterdayqoq(data.getYesterdayQoQ());
        consumptionSummar.setModeltopic(data.getModelTopic());
    }
}
