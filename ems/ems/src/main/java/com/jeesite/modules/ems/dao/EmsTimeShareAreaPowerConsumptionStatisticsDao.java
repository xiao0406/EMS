package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsTimeShareAreaPowerConsumptionStatistics;
import com.jeesite.modules.ems.entity.TimeSharePowerConsumptionEntity;
import com.jeesite.modules.ems.entity.TimeShareStatisticsEntity;

import java.util.List;

/**
 * 区域峰平谷电耗表数据统计表DAO接口
 * @author 李鹏
 * @version 2023-07-25
 */
@MyBatisDao
public interface EmsTimeShareAreaPowerConsumptionStatisticsDao extends CrudDao<EmsTimeShareAreaPowerConsumptionStatistics> {

    EmsTimeShareAreaPowerConsumptionStatistics isStockedRec(EmsTimeShareAreaPowerConsumptionStatistics entity);

    TimeShareStatisticsEntity timeShareStatistics(EmsTimeShareAreaPowerConsumptionStatistics entity);

    List<TimeSharePowerConsumptionEntity> findTimeShareList(EmsTimeShareAreaPowerConsumptionStatistics entity);
}