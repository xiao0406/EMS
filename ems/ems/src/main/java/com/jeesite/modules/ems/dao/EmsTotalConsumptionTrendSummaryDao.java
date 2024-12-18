package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsTotalConsumptionTrendSummary;

/**
 * 总用电量汇总DAO接口
 * @author 范富华
 * @version 2024-05-13
 */
@MyBatisDao
public interface EmsTotalConsumptionTrendSummaryDao extends CrudDao<EmsTotalConsumptionTrendSummary> {
	EmsTotalConsumptionTrendSummary findOne(EmsTotalConsumptionTrendSummary emsTotalConsumptionTrendSummary);
}