package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsRankingSummaryInfo;

/**
 * 耗电量排行关联表DAO接口
 * @author 范富华
 * @version 2024-05-15
 */
@MyBatisDao
public interface EmsRankingSummaryInfoDao extends CrudDao<EmsRankingSummaryInfo> {

    void deleteBySummaryId(EmsRankingSummaryInfo emsRankingSummaryInfo);
}