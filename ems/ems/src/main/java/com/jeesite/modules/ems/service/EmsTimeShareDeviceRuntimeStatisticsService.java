package com.jeesite.modules.ems.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.NumberUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.dao.EmsTimeShareDeviceRuntimeStatisticsDao;
import com.jeesite.modules.ems.entity.*;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 峰平谷设备运行时长统计表Service
 *
 * @author 李鹏
 * @version 2023-06-27
 */
@Service
@Transactional(readOnly = true)
public class EmsTimeShareDeviceRuntimeStatisticsService extends CrudService<EmsTimeShareDeviceRuntimeStatisticsDao, EmsTimeShareDeviceRuntimeStatistics> {


    public static final String HOUR_STRING = "小时";
    public static final String MIN_STRING = "分钟";

    @Resource
    private EmsTimeShareDeviceRuntimeService emsTimeShareDeviceRuntimeService;
    @Resource
    private EmsElectricPowerConsumptionStatisticsService emsElectricPowerConsumptionStatisticsService;
    @Resource
    private EmsElectricPowerConsumptionService emsElectricPowerConsumptionService;



    /**
     * 获取单条数据
     *
     * @param emsTimeShareDeviceRuntimeStatistics
     * @return
     */
    @Override
    public EmsTimeShareDeviceRuntimeStatistics get(EmsTimeShareDeviceRuntimeStatistics emsTimeShareDeviceRuntimeStatistics) {
        return super.get(emsTimeShareDeviceRuntimeStatistics);
    }

    /**
     * 查询分页数据
     *
     * @param emsTimeShareDeviceRuntimeStatistics 查询条件
     * @return
     */
    @Override
    public Page<EmsTimeShareDeviceRuntimeStatistics> findPage(EmsTimeShareDeviceRuntimeStatistics emsTimeShareDeviceRuntimeStatistics) {
        return super.findPage(emsTimeShareDeviceRuntimeStatistics);
    }

    /**
     * 查询列表数据
     *
     * @param emsTimeShareDeviceRuntimeStatistics
     * @return
     */
    @Override
    public List<EmsTimeShareDeviceRuntimeStatistics> findList(EmsTimeShareDeviceRuntimeStatistics emsTimeShareDeviceRuntimeStatistics) {
        return super.findList(emsTimeShareDeviceRuntimeStatistics);
    }

    /**
     * 查询根据电表排序顺序的数据列表
     * @param emsTimeShareDeviceRuntimeStatistics
     * @return
     */
    public List<EmsTimeShareDeviceRuntimeStatistics> findMeterSortList(EmsTimeShareDeviceRuntimeStatistics emsTimeShareDeviceRuntimeStatistics) {
        return this.dao.findMeterSortList(emsTimeShareDeviceRuntimeStatistics);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param emsTimeShareDeviceRuntimeStatistics
     */
    @Override
    @Transactional(readOnly = false)
    public void save(EmsTimeShareDeviceRuntimeStatistics emsTimeShareDeviceRuntimeStatistics) {
        super.save(emsTimeShareDeviceRuntimeStatistics);
    }

    /**
     * 更新状态
     *
     * @param emsTimeShareDeviceRuntimeStatistics
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(EmsTimeShareDeviceRuntimeStatistics emsTimeShareDeviceRuntimeStatistics) {
        super.updateStatus(emsTimeShareDeviceRuntimeStatistics);
    }

    /**
     * 删除数据
     *
     * @param emsTimeShareDeviceRuntimeStatistics
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(EmsTimeShareDeviceRuntimeStatistics emsTimeShareDeviceRuntimeStatistics) {
        super.delete(emsTimeShareDeviceRuntimeStatistics);
    }

    public EmsTimeShareDeviceRuntimeStatistics isStockedRec(EmsTimeShareDeviceRuntimeStatistics build) {
        return this.dao.isStockedRec(build);
    }

    /**
     * 设备按天统计利用率
     *
     * @param deviceStatisticsParam
     * @return
     */
    public EmsDailyStatisticsEntity deviceRateStatistics(DeviceStatisticsParam deviceStatisticsParam) {
        EmsDailyStatisticsEntity entity = new EmsDailyStatisticsEntity();
        // 查询该设备所有月份类的数据
        EmsTimeShareDeviceRuntime runtimeParam = new EmsTimeShareDeviceRuntime();
        runtimeParam.setDeviceId(deviceStatisticsParam.getDeviceId());
        runtimeParam.setDataDateStart(deviceStatisticsParam.getStart());
        runtimeParam.setDataDateEnd(deviceStatisticsParam.getEnd());
        runtimeParam.setDataType(TemporalGranularityEnum.VD_Day.getCode());
        List<EmsTimeShareDeviceRuntime> deviceRuntimeStatistics = emsTimeShareDeviceRuntimeService.findList(runtimeParam);

        if (CollectionUtils.isEmpty(deviceRuntimeStatistics)) {
            return new EmsDailyStatisticsEntity();
        }
        // 结果数据组装
        ArrayList<Double> totalRunningList = new ArrayList<>();
        ArrayList<Double> totalNoLoadList = new ArrayList<>();
        ArrayList<Double> totalStopList = new ArrayList<>();
        deviceRuntimeStatistics.forEach(o -> {
            totalRunningList.add(o.getTotalRunning());
            totalNoLoadList.add(o.getTotalNoLoad());
            totalStopList.add(o.getTotalStop());
        });
        Double totalRunning = NumberUtils.addAll(totalRunningList.toArray(new Double[0]));
        Double totalNoLoad = NumberUtils.addAll(totalNoLoadList.toArray(new Double[0]));
        Double totalStop = NumberUtils.addAll(totalStopList.toArray(new Double[0]));
        Double totalTime = totalRunning + totalNoLoad + totalStop;
        int totalTimeInt = (int) totalTime.doubleValue();
        // 设备ID
        entity.setDeviceId(deviceStatisticsParam.getDeviceId());
        // 总时长
        entity.setTotalTime(totalTimeInt / 60 + HOUR_STRING + totalTimeInt % 60 + MIN_STRING);
        // 停机时常
        int totalStopInt = (int) totalStop.doubleValue();
        entity.setShutDownTime(totalStopInt / 60 + HOUR_STRING + totalStopInt % 60 + MIN_STRING);
        // 停机率
        double shutDownRate = (double) totalStopInt / totalTimeInt;
        shutDownRate = new BigDecimal(shutDownRate * 100).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        DecimalFormat df = new DecimalFormat("0.0");
        entity.setShutDownRate(totalTimeInt == 0 ? "0" : df.format(shutDownRate));
        // 空载时常
        int totalNoLoadInt = (int) totalNoLoad.doubleValue();
        entity.setUnloadedTime(totalNoLoadInt / 60 + HOUR_STRING + totalNoLoadInt % 60 + MIN_STRING);
        // 空载率
        double unloadRate = (double) totalNoLoadInt / totalTimeInt;
        unloadRate = new BigDecimal(unloadRate * 100).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        entity.setUnloadedRate(totalTimeInt == 0 ? "0" : df.format(unloadRate));
        // 运行时常
        int operation = (int) totalRunning.doubleValue();
        entity.setOperationTime(operation / 60 + HOUR_STRING + operation % 60 + MIN_STRING);
        // 运行率
        double operationRate = (double) operation / totalTimeInt;
        operationRate = new BigDecimal(operationRate * 100).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        entity.setOperationRate(totalTimeInt == 0 ? "0" : df.format(operationRate));
        return entity;
    }

    /**
     * 单日设备统计-按天时间段内所有历史记录
     *
     * @param deviceStatisticsParam
     * @return
     */
    public HomePageEntity dayRecord(DeviceStatisticsParam deviceStatisticsParam) {
        HomePageEntity homePageEntity = new HomePageEntity();
        // 查询该设备月内每天能耗数据
        EmsTimeShareDeviceRuntime runtimeParam = new EmsTimeShareDeviceRuntime();
        runtimeParam.setDeviceId(deviceStatisticsParam.getDeviceId());
        runtimeParam.setDataDateStart(deviceStatisticsParam.getStart());
        runtimeParam.setDataDateEnd(deviceStatisticsParam.getEnd());
        runtimeParam.setDataType(TemporalGranularityEnum.VD_Day.getCode());
        runtimeParam.getSqlMap().getOrder().setOrderBy("a.data_date asc");
        List<EmsTimeShareDeviceRuntime> deviceRuntimeList = emsTimeShareDeviceRuntimeService.findList(runtimeParam);
        if (CollectionUtils.isEmpty(deviceRuntimeList)) {
            return homePageEntity;
        }
        // 日期节点
        List<String> dateList = ListUtils.newArrayList();
        List<Double> stopAxes = ListUtils.newArrayList();
        List<Double> noLoadAxes = ListUtils.newArrayList();
        List<Double> runAxes = ListUtils.newArrayList();
        deviceRuntimeList.stream().forEach(obj -> {
            String label = DateUtils.formatDate(obj.getDataDate());
            //组装X轴
            dateList.add(label);
            //组装Y轴
            stopAxes.add(obj.getTotalStop() / 60);
            noLoadAxes.add(obj.getTotalNoLoad() / 60);
            runAxes.add(obj.getTotalRunning() / 60);
        });
        //组装数据
        EChartBody body = new EChartBody();
        body.setX(dateList);
        body.setY(ListUtils.newArrayList(
                new EChartItem("停机", stopAxes),
                new EChartItem("空载", noLoadAxes),
                new EChartItem("运行", runAxes)));
        EChart eChart = EChart.builder().body(body).build();
        homePageEntity.setEChart(eChart);
        return homePageEntity;
    }

    /**
     * 单台设备月利用率
     *
     * @param deviceStatisticsParam
     * @return
     */
    public EmsDailyStatisticsEntity deviceRateStatisticsYear(DeviceStatisticsParam deviceStatisticsParam) {
        EmsDailyStatisticsEntity entity = new EmsDailyStatisticsEntity();
        // 查询该设备所有月份类的数据
        EmsTimeShareDeviceRuntimeStatistics runtimeParam = new EmsTimeShareDeviceRuntimeStatistics();
        runtimeParam.setDeviceId(deviceStatisticsParam.getDeviceId());
        runtimeParam.setQryStartTime(DateUtils.formatDate(deviceStatisticsParam.getStart(), "yyyy-MM"));
        runtimeParam.setQryEndTime(DateUtils.formatDate(deviceStatisticsParam.getEnd(), "yyyy-MM"));
        runtimeParam.setDataType(TemporalGranularityEnum.VD_Month.getCode());
        List<EmsTimeShareDeviceRuntimeStatistics> deviceRuntimeStatistics = this.findList(runtimeParam);

        if (CollectionUtils.isEmpty(deviceRuntimeStatistics)) {
            return new EmsDailyStatisticsEntity();
        }

        // 结果数据组装
        ArrayList<Double> totalRunningList = new ArrayList<>();
        ArrayList<Double> totalNoLoadList = new ArrayList<>();
        ArrayList<Double> totalStopList = new ArrayList<>();
        deviceRuntimeStatistics.forEach(o -> {
            totalRunningList.add(o.getTotalRunning());
            totalNoLoadList.add(o.getTotalNoLoad());
            totalStopList.add(o.getTotalStop());
        });
        Double totalRunning = NumberUtils.addAll(totalRunningList.toArray(new Double[0]));
        Double totalNoLoad = NumberUtils.addAll(totalNoLoadList.toArray(new Double[0]));
        Double totalStop = NumberUtils.addAll(totalStopList.toArray(new Double[0]));
        Double totalTime = NumberUtils.addAll(totalRunning, totalNoLoad, totalStop);

        int totalTimeInt = totalTime.intValue();
        // 设备ID
        entity.setDeviceId(deviceStatisticsParam.getDeviceId());
        // 总时长
        entity.setTotalTime(totalTimeInt / 60 + HOUR_STRING + totalTimeInt % 60 + MIN_STRING);

        // 停机时常
        int totalStopInt = totalStop.intValue();
        entity.setShutDownTime(totalStopInt / 60 + HOUR_STRING + totalStopInt % 60 + MIN_STRING);
        // 停机率
        Double shutDownRate = NumberUtils.div(NumberUtils.mul(totalStopInt, 100), totalTimeInt, 1);
        entity.setShutDownRate(shutDownRate.toString());

        // 空载时常
        int totalNoLoadInt = totalNoLoad.intValue();
        entity.setUnloadedTime(totalNoLoadInt / 60 + HOUR_STRING + totalNoLoadInt % 60 + MIN_STRING);
        // 空载率
        Double unloadRate = NumberUtils.div(NumberUtils.mul(totalNoLoadInt, 100), totalTimeInt, 1);
        entity.setUnloadedRate(unloadRate.toString());

        // 运行时常
        int operation = totalRunning.intValue();
        entity.setOperationTime(operation / 60 + HOUR_STRING + operation % 60 + MIN_STRING);
        // 运行率
        Double operationRate = NumberUtils.div(NumberUtils.mul(operation, 100), totalTimeInt, 1);
        entity.setOperationRate(operationRate.toString());

        return entity;

    }

    /**
     * 分页获取年份内所有月份的能耗记录
     *
     * @param deviceStatisticsParam
     * @return
     */
    public Page<EmsDailyStatisticsListEntity> monthRecordList(DeviceStatisticsParam deviceStatisticsParam, HttpServletRequest request, HttpServletResponse response) {
        // 查询时长信息
        EmsTimeShareDeviceRuntimeStatistics params = new EmsTimeShareDeviceRuntimeStatistics();
        params.setDeviceId(deviceStatisticsParam.getDeviceId());
        params.setQryStartTime(DateUtils.formatDate(deviceStatisticsParam.getStart(), "yyyy-MM"));
        params.setQryEndTime(DateUtils.formatDate(deviceStatisticsParam.getEnd(), "yyyy-MM"));
        params.setDataType(TemporalGranularityEnum.VD_Month.getCode());
        params.setPage(new Page<EmsTimeShareDeviceRuntime>(request, response));
        params.getSqlMap().getOrder().setOrderBy("a.data_date_key desc");
        Page<EmsTimeShareDeviceRuntimeStatistics> page = this.findPage(params);

        List<EmsTimeShareDeviceRuntimeStatistics> list = page.getList();
        if (CollectionUtils.isEmpty(list)) {
            return new Page<>();
        }

        // 查询用电信息
        // 查询总的能耗
        EmsElectricPowerConsumptionStatistics consumptionParams = new EmsElectricPowerConsumptionStatistics();
        consumptionParams.setDeviceId(deviceStatisticsParam.getDeviceId());
        consumptionParams.setQryStartTime(DateUtils.formatDate(deviceStatisticsParam.getStart(), "yyyy-MM"));
        consumptionParams.setQryEndTime(DateUtils.formatDate(deviceStatisticsParam.getEnd(), "yyyy-MM"));
        consumptionParams.setDataType(TemporalGranularityEnum.VD_Month.getCode());
        consumptionParams.setPage(new Page<EmsElectricPowerConsumption>(request, response));
        Page<EmsElectricPowerConsumptionStatistics> consumptionPage = emsElectricPowerConsumptionStatisticsService.findPage(consumptionParams);
        List<EmsElectricPowerConsumptionStatistics> consList = consumptionPage.getList();
        Map<String, EmsElectricPowerConsumptionStatistics> consMap = consList.stream().collect(
                Collectors.toMap(
                        EmsElectricPowerConsumptionStatistics::getDataDateKey,
                        EmsElectricPowerConsumptionStatistics -> EmsElectricPowerConsumptionStatistics,
                        (key1, key2) -> key2,
                        LinkedHashMap::new));

        // 数据组装
        Page<EmsDailyStatisticsListEntity> resultPage = new Page();
        resultPage.setPageNo(page.getPageNo());
        resultPage.setPageSize(page.getPageSize());
        resultPage.setCount(page.getCount());
        List<EmsDailyStatisticsListEntity> resultList = new ArrayList<>();
        for (EmsTimeShareDeviceRuntimeStatistics runtime : list) {
            String dataDate = runtime.getDataDateKey();

            EmsDailyStatisticsListEntity entity = new EmsDailyStatisticsListEntity();
            entity.setDeviceName(runtime.getDeviceName());
            entity.setDataDate(dataDate);
            entity.setTotalTime(runtime.getTotalNoLoad() + runtime.getTotalRunning() + runtime.getTotalStop());
            entity.setShutDownTime(runtime.getTotalStop());
            entity.setUnloadedTime(runtime.getTotalNoLoad());
            entity.setOperationTime(runtime.getTotalRunning());
            EmsElectricPowerConsumptionStatistics emsElectricPowerConsumptionStatistics = consMap.get(dataDate);
            if(Objects.nonNull(emsElectricPowerConsumptionStatistics)){
                entity.setPositiveActiveEnergy(emsElectricPowerConsumptionStatistics.getPositiveActiveEnergy());
            }
            resultList.add(entity);
        }

        resultPage.setList(resultList);
        return resultPage;
    }

    public HomePageEntity monthRecord(DeviceStatisticsParam deviceStatisticsParam) {

        HomePageEntity homePageEntity = new HomePageEntity();
        // 查询该设备年内每月能耗数据
        EmsTimeShareDeviceRuntimeStatistics runtimeParam = new EmsTimeShareDeviceRuntimeStatistics();
        runtimeParam.setDeviceId(deviceStatisticsParam.getDeviceId());
        runtimeParam.setQryStartTime(DateUtils.formatDate(deviceStatisticsParam.getStart(), "yyyy-MM"));
        runtimeParam.setQryEndTime(DateUtils.formatDate(deviceStatisticsParam.getEnd(), "yyyy-MM"));
        runtimeParam.setDataType(TemporalGranularityEnum.VD_Month.getCode());
        runtimeParam.getSqlMap().getOrder().setOrderBy("a.data_date_key asc");
        List<EmsTimeShareDeviceRuntimeStatistics> deviceRuntimeList = this.findList(runtimeParam);
        if (CollectionUtils.isEmpty(deviceRuntimeList)) {
            return homePageEntity;
        }
        // 日期节点
        List<String> dateList = ListUtils.newArrayList();
        List<Double> stopAxes = ListUtils.newArrayList();
        List<Double> noLoadAxes = ListUtils.newArrayList();
        List<Double> runAxes = ListUtils.newArrayList();
        deviceRuntimeList.stream().forEach(obj -> {
            String label = obj.getDataDateKey();
            //组装X轴
            dateList.add(label);
            //组装Y轴
            stopAxes.add(obj.getTotalStop() / 60);
            noLoadAxes.add(obj.getTotalNoLoad() / 60);
            runAxes.add(obj.getTotalRunning() / 60);
        });
        //组装数据
        EChartBody body = new EChartBody();
        body.setX(dateList);
        body.setY(ListUtils.newArrayList(
                new EChartItem("停机", stopAxes),
                new EChartItem("空载", noLoadAxes),
                new EChartItem("运行", runAxes)));
        EChart eChart = EChart.builder().body(body).build();
        homePageEntity.setEChart(eChart);
        return homePageEntity;
    }

    /**
     * 月份内每天设备能耗记录导出列表
     *
     * @param deviceStatisticsParam
     * @return
     */
    public List<EmsTimeShareDeviceRuntime> dayExport(DeviceStatisticsParam deviceStatisticsParam) {
        List<EmsTimeShareDeviceRuntime> reportList = new ArrayList<>();
        // 查询时长信息
        EmsTimeShareDeviceRuntime params = new EmsTimeShareDeviceRuntime();
        params.setDeviceId(deviceStatisticsParam.getDeviceId());
        params.setDataDateStart(deviceStatisticsParam.getStart());
        params.setDataDateEnd(deviceStatisticsParam.getEnd());
        params.setDataType(TemporalGranularityEnum.VD_Day.getCode());
        params.getSqlMap().getOrder().setOrderBy("a.data_date desc");
        List<EmsTimeShareDeviceRuntime> list = emsTimeShareDeviceRuntimeService.findList(params);
        if (CollectionUtils.isEmpty(list)) {
            return reportList;
        }
        Collections.reverse(list);
        // 查询总的能耗
        EmsElectricPowerConsumption consumptionParams = new EmsElectricPowerConsumption();
        consumptionParams.setDeviceId(deviceStatisticsParam.getDeviceId());
        consumptionParams.setQryStartTime(deviceStatisticsParam.getStart());
        consumptionParams.setQryEndTime(deviceStatisticsParam.getEnd());
        consumptionParams.setDataType(TemporalGranularityEnum.VD_Day.getCode());
        List<EmsElectricPowerConsumption> consumptionList = emsElectricPowerConsumptionService.findList(consumptionParams);
        Map<Date, EmsElectricPowerConsumption> consMap = consumptionList.stream().collect(
                Collectors.toMap(
                        EmsElectricPowerConsumption::getDataDate,
                        EmsElectricPowerConsumption -> EmsElectricPowerConsumption,
                        (key1, key2) -> key2,
                        LinkedHashMap::new));

        // 赋值总能耗和总时长
        for (int i = 0; i < list.size(); i++) {
            EmsTimeShareDeviceRuntime runtime = list.get(i);
            Date dataDate = runtime.getDataDate();
            EmsElectricPowerConsumption emsElectricPowerConsumption = consMap.get(dataDate);
            if (Objects.nonNull(emsElectricPowerConsumption)) {
                runtime.setPositiveActiveEnergy(emsElectricPowerConsumption.getPositiveActiveEnergy());
            }
            runtime.setPositiveActiveEnergy(runtime.getPositiveActiveEnergy());
            runtime.setTotalTime((runtime.getTotalNoLoad() + runtime.getTotalStop() + runtime.getTotalRunning()) / 60);
            runtime.setTotalRunning(new BigDecimal(runtime.getTotalRunning() / 60).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            runtime.setTotalStop(new BigDecimal(runtime.getTotalStop() / 60).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            runtime.setTotalNoLoad(new BigDecimal(runtime.getTotalNoLoad() / 60).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        reportList = list;
        return reportList;
    }

    /**
     * 年份内每月设备能耗记录导出列表
     *
     * @param deviceStatisticsParam
     * @return
     */
    public List<EmsTimeShareDeviceRuntimeStatistics> monthExport(DeviceStatisticsParam deviceStatisticsParam) {

        List<EmsTimeShareDeviceRuntimeStatistics> reportList = new ArrayList<>();
        // 查询时长信息
        EmsTimeShareDeviceRuntimeStatistics params = new EmsTimeShareDeviceRuntimeStatistics();
        params.setDeviceId(deviceStatisticsParam.getDeviceId());
        params.setQryStartTime(DateUtils.formatDate(deviceStatisticsParam.getStart(), "yyyy-MM"));
        params.setQryEndTime(DateUtils.formatDate(deviceStatisticsParam.getEnd(), "yyyy-MM"));
        params.setDataType(TemporalGranularityEnum.VD_Month.getCode());
        params.getSqlMap().getOrder().setOrderBy("a.data_date_key desc");
        List<EmsTimeShareDeviceRuntimeStatistics> list = this.findList(params);
        if (CollectionUtils.isEmpty(list)) {
            return reportList;
        }
        Collections.reverse(list);
        // 查询总的能耗
        EmsElectricPowerConsumptionStatistics consumptionParams = new EmsElectricPowerConsumptionStatistics();
        consumptionParams.setDeviceId(deviceStatisticsParam.getDeviceId());
        consumptionParams.setQryStartTime(DateUtils.formatDate(deviceStatisticsParam.getStart()));
        consumptionParams.setQryEndTime(DateUtils.formatDate(deviceStatisticsParam.getEnd()));
        consumptionParams.setDataType(TemporalGranularityEnum.VD_Month.getCode());
        List<EmsElectricPowerConsumptionStatistics> consumptionList = emsElectricPowerConsumptionStatisticsService.findList(consumptionParams);
        Map<String, EmsElectricPowerConsumptionStatistics> consMap = consumptionList.stream().collect(
                Collectors.toMap(
                        EmsElectricPowerConsumptionStatistics::getDataDateKey,
                        EmsElectricPowerConsumptionStatistics -> EmsElectricPowerConsumptionStatistics,
                        (key1, key2) -> key2,
                        LinkedHashMap::new));

        for (int i = 0; i < list.size(); i++) {
            EmsTimeShareDeviceRuntimeStatistics runtime = list.get(i);
            String dataDate = runtime.getDataDateKey();
            runtime.setTotalTime((runtime.getTotalNoLoad() + runtime.getTotalStop() + runtime.getTotalRunning()) / 60);
            runtime.setTotalRunning(new BigDecimal(runtime.getTotalRunning() / 60).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            runtime.setTotalStop(new BigDecimal(runtime.getTotalStop() / 60).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            runtime.setTotalNoLoad(new BigDecimal(runtime.getTotalNoLoad() / 60).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            EmsElectricPowerConsumptionStatistics emsElectricPowerConsumptionStatistics = consMap.get(dataDate);
            if(Objects.nonNull(emsElectricPowerConsumptionStatistics)){
                runtime.setPositiveActiveEnergy(emsElectricPowerConsumptionStatistics.getPositiveActiveEnergy());
            }
        }
        reportList = list;
        return reportList;
    }
}