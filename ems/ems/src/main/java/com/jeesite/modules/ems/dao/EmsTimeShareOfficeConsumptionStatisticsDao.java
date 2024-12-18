package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsTimeShareOfficeConsumptionStatistics;

import java.util.List;

/**
 * 峰平谷部门电耗表数据统计表DAO接口
 * @author 李鹏
 * @version 2023-06-28
 */
@MyBatisDao
public interface EmsTimeShareOfficeConsumptionStatisticsDao extends CrudDao<EmsTimeShareOfficeConsumptionStatistics> {

    EmsTimeShareOfficeConsumptionStatistics isStockedRec(EmsTimeShareOfficeConsumptionStatistics build);

    List<EmsTimeShareOfficeConsumptionStatistics> findOfficeList(EmsTimeShareOfficeConsumptionStatistics emsTimeShareOfficeConsumptionStatistics);
}