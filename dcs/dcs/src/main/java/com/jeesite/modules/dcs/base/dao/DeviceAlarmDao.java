package com.jeesite.modules.dcs.base.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.dcs.base.entity.DeviceAlarm;

import java.util.List;

/**
 * 设备信息DAO接口
 * @author ds
 * @version 2023-06-25
 */
@MyBatisDao
public interface DeviceAlarmDao extends CrudDao<DeviceAlarm> {

    List<DeviceAlarm> findDeviceAlarm(String deviceCode, int size);
}