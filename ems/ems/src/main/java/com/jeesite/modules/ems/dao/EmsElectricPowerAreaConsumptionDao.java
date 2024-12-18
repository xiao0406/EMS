package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsElectricPowerAreaConsumption;
import com.jeesite.modules.ems.entity.EmsTimeSharePowerConsumption;

import java.util.Date;
import java.util.List;

/**
 * 区域电耗表数据基础表DAO接口
 * @author 李鹏
 * @version 2023-07-06
 */
@MyBatisDao
public interface EmsElectricPowerAreaConsumptionDao extends CrudDao<EmsElectricPowerAreaConsumption> {

    EmsElectricPowerAreaConsumption isStockedRec(EmsElectricPowerAreaConsumption areaConsumption);

    List<Date> getStockPendulum(EmsElectricPowerAreaConsumption params);
}