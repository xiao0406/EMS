package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsTimeShareAreaPowerConsumption;
import com.jeesite.modules.ems.entity.TimeSharePowerConsumptionEntity;
import com.jeesite.modules.ems.entity.TimeShareStatisticsEntity;

import java.util.List;

/**
 * 区域峰平谷电耗表数据基础表DAO接口
 * @author 李鹏
 * @version 2023-07-25
 */
@MyBatisDao
public interface EmsTimeShareAreaPowerConsumptionDao extends CrudDao<EmsTimeShareAreaPowerConsumption> {

    EmsTimeShareAreaPowerConsumption isStockedRec(EmsTimeShareAreaPowerConsumption entity);

    TimeShareStatisticsEntity timeShareStatistics(EmsTimeShareAreaPowerConsumption entity);

    List<TimeSharePowerConsumptionEntity> findTimeShareList(EmsTimeShareAreaPowerConsumption entity);
}