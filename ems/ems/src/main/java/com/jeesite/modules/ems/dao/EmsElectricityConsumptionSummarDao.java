package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsElectricityConsumptionSummar;

/**
 * 用电量汇总DAO接口
 * @author 范富华
 * @version 2024-05-13
 */
@MyBatisDao
public interface EmsElectricityConsumptionSummarDao extends CrudDao<EmsElectricityConsumptionSummar> {
	EmsElectricityConsumptionSummar findOne(EmsElectricityConsumptionSummar emsElectricityConsumptionSummar);
}