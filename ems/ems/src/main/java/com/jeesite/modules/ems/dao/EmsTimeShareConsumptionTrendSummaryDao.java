package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsTimeShareConsumptionTrendSummary;

/**
 * 尖峰平谷用电趋势汇总DAO接口
 * @author 范富华
 * @version 2024-05-17
 */
@MyBatisDao
public interface EmsTimeShareConsumptionTrendSummaryDao extends CrudDao<EmsTimeShareConsumptionTrendSummary> {
	
}