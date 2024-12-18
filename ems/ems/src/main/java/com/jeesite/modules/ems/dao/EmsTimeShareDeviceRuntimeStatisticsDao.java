package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsTimeShareDeviceRuntimeStatistics;

import java.util.List;

/**
 * 峰平谷设备运行时长统计表DAO接口
 * @author 李鹏
 * @version 2023-06-27
 */
@MyBatisDao
public interface EmsTimeShareDeviceRuntimeStatisticsDao extends CrudDao<EmsTimeShareDeviceRuntimeStatistics> {

    EmsTimeShareDeviceRuntimeStatistics isStockedRec(EmsTimeShareDeviceRuntimeStatistics build);

    List<EmsTimeShareDeviceRuntimeStatistics> findMeterSortList(EmsTimeShareDeviceRuntimeStatistics emsTimeShareDeviceRuntimeStatistics);
}