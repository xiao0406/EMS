package com.jeesite.modules.ems.dao;

import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EChartData;
import com.jeesite.modules.ems.entity.EmsElectricPowerAreaConsumption;
import com.jeesite.modules.ems.entity.EmsElectricPowerConsumption;
import com.jeesite.modules.ems.entity.StageCumulativeQueryEntity;

import java.util.Date;
import java.util.List;

/**
 * 电耗表DAO接口
 *
 * @author 李鹏
 * @version 2023-05-25
 */
@MyBatisDao
public interface EmsElectricPowerConsumptionDao extends CrudDao<EmsElectricPowerConsumption> {

    List<Date> getStockPendulum(EmsElectricPowerConsumption params);

    EmsElectricPowerConsumption isStockedRec(EmsElectricPowerConsumption eepc);

    Double getStageCumulativeConsumption(StageCumulativeQueryEntity entity);

    List<EChartData> getStageConsumption2EChart(StageCumulativeQueryEntity entity);

    List<EChartData> consumptionRanking(EmsElectricPowerConsumption params);

    List<EmsElectricPowerConsumption> activeEnergyEChart(EmsElectricPowerConsumption quarter);

    EmsElectricPowerAreaConsumption getAreaStageCumulativeConsumption(EmsElectricPowerConsumption params);

    EmsElectricPowerConsumption getLastRecord(EmsElectricPowerConsumption params);

    List<EmsElectricPowerConsumption> findMeterSortList(EmsElectricPowerConsumption emsElectricPowerConsumption);
}