package com.jeesite.modules.dcs.base.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.dcs.base.dto.DeviceDTO;
import com.jeesite.modules.dcs.base.entity.Device;

import java.util.List;

/**
 * 设备信息DAO接口
 * @author ds
 * @version 2023-06-25
 */
@MyBatisDao
public interface DeviceDao extends CrudDao<Device> {

    List<DeviceDTO> findDeviceList(DeviceDTO device);
}