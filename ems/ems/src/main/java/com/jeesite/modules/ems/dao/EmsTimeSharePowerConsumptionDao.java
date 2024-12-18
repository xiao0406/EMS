package com.jeesite.modules.ems.dao;

import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.*;

import java.util.Date;
import java.util.List;

/**
 * 峰平谷电耗表数据基础表DAO接口
 * @author 李鹏
 * @version 2023-06-20
 */
@MyBatisDao
public interface EmsTimeSharePowerConsumptionDao extends CrudDao<EmsTimeSharePowerConsumption> {

    EmsTimeSharePowerConsumption isStockedRec(EmsTimeSharePowerConsumption build);

    EmsTimeSharePowerConsumption findMeterDailyConsumption(EmsTimeSharePowerConsumption where);

    List<EmsTimeSharePowerConsumption> getStageConsumption(EmsTimeSharePowerConsumption where);

    TimeShareStatisticsEntity timeShareStatistics(EmsTimeSharePowerConsumption where);

    EmsTimeShareAreaPowerConsumption getAreaStageCumulativeConsumption(EmsTimeSharePowerConsumption entity);

    List<TimeSharePowerConsumptionEntity> findTimeShareList(EmsTimeSharePowerConsumption entity);

    List<EmsTimeSharePowerConsumption> findMeterSortList(EmsTimeSharePowerConsumption emsTimeSharePowerConsumption);
}