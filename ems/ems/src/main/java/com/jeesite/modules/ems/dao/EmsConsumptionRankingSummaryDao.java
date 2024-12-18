package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsConsumptionRankingSummary;

/**
 * 用电量排行DAO接口
 *
 * @author 范富华
 * @version 2024-05-14
 */
@MyBatisDao
public interface EmsConsumptionRankingSummaryDao extends CrudDao<EmsConsumptionRankingSummary> {

    EmsConsumptionRankingSummary findOne(EmsConsumptionRankingSummary emsConsumptionRankingSummary);
}