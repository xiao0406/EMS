package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsTimeShareAreaPowerConsumptionStatistics;
import com.jeesite.modules.ems.entity.EmsTimeSharePowerConsumptionStatistics;
import com.jeesite.modules.ems.entity.TimeSharePowerConsumptionEntity;
import com.jeesite.modules.ems.entity.TimeShareStatisticsEntity;

import java.util.List;

/**
 * 峰平谷电耗表数据统计表DAO接口
 * @author 李鹏
 * @version 2023-06-20
 */
@MyBatisDao
public interface EmsTimeSharePowerConsumptionStatisticsDao extends CrudDao<EmsTimeSharePowerConsumptionStatistics> {

    EmsTimeSharePowerConsumptionStatistics isStockedRec(EmsTimeSharePowerConsumptionStatistics build);

    TimeShareStatisticsEntity timeShareStatistics(EmsTimeSharePowerConsumptionStatistics where);

    EmsTimeShareAreaPowerConsumptionStatistics getAreaStageCumulativeConsumption(EmsTimeSharePowerConsumptionStatistics entity);

    List<TimeSharePowerConsumptionEntity> findTimeShareList(EmsTimeSharePowerConsumptionStatistics entity);

    List<EmsTimeSharePowerConsumptionStatistics> findMeterSortList(EmsTimeSharePowerConsumptionStatistics emsTimeSharePowerConsumptionStatistics);
}