package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsElectricPowerAreaConsumptionStatistics;

/**
 * 区域电耗表数据统计表DAO接口
 * @author 李鹏
 * @version 2023-07-06
 */
@MyBatisDao
public interface EmsElectricPowerAreaConsumptionStatisticsDao extends CrudDao<EmsElectricPowerAreaConsumptionStatistics> {

    EmsElectricPowerAreaConsumptionStatistics isStockedRec(EmsElectricPowerAreaConsumptionStatistics areaConsumption);
}