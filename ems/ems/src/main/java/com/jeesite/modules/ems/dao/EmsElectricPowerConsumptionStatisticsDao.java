package com.jeesite.modules.ems.dao;

import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.*;

import java.util.Date;
import java.util.List;

/**
 * 电耗表数据统计表DAO接口
 *
 * @author 李鹏
 * @version 2023-06-19
 */
@MyBatisDao
public interface EmsElectricPowerConsumptionStatisticsDao extends CrudDao<EmsElectricPowerConsumptionStatistics> {

    /**
     * 查询存量数据
     *
     * @param emsElectricPowerConsumptionStatistics
     * @return
     */
    EmsElectricPowerConsumptionStatistics isStockedRec(EmsElectricPowerConsumptionStatistics emsElectricPowerConsumptionStatistics);

    /**
     * 设备工效统计
     *
     * @param emsStatisticsReportEntity
     * @return
     */
    List<EmsStatisticsEfficacyEntity> meterMonthEfficacy(EmsStatisticsReportEntity emsStatisticsReportEntity);

    /**
     * 设备工效统计
     *
     * @param emsStatisticsReportEntity
     * @return
     */
    List<EmsStatisticsEfficacyEntity> meterYearEfficacy(EmsStatisticsReportEntity emsStatisticsReportEntity);

    /**
     * 汇总查询
     *
     * @param entity
     * @return
     */
    Double getStageCumulativeConsumption(StageCumulativeQueryEntity entity);

    /**
     * echart数据查询
     *
     * @param entity
     * @return
     */
    List<EChartData> getStageConsumption2EChart(StageCumulativeQueryEntity entity);

    List<EChartData> consumptionRanking(EmsElectricPowerConsumptionStatistics params);

    List<EmsElectricPowerConsumptionStatistics> activeEnergyEChart(EmsElectricPowerConsumptionStatistics params);

    EmsElectricPowerAreaConsumptionStatistics getAreaStageCumulativeConsumption(EmsElectricPowerConsumptionStatistics params);

    List<DeviceWorkEfficiencyEntity> workEfficiencyListData(DeviceWorkEfficiencyQryEntity params);

    List<EmsElectricPowerConsumptionStatistics> findMeterSortList(EmsElectricPowerConsumptionStatistics emsElectricPowerConsumptionStatistics);
}