package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsTimeShareDeviceRuntime;

import java.util.List;

/**
 * 峰平谷设备运行时长表DAO接口
 * @author 李鹏
 * @version 2023-06-27
 */
@MyBatisDao
public interface EmsTimeShareDeviceRuntimeDao extends CrudDao<EmsTimeShareDeviceRuntime> {
    /**
     * 查询存量数据
     * @param build
     * @return
     */
    EmsTimeShareDeviceRuntime isStockedRec(EmsTimeShareDeviceRuntime build);

    List<EmsTimeShareDeviceRuntime> findMeterSortList(EmsTimeShareDeviceRuntime emsTimeShareDeviceRuntime);
}