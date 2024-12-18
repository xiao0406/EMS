package com.jeesite.modules.dcs.base.service;


import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.dcs.base.dao.DeviceAlarmDao;
import com.jeesite.modules.dcs.base.entity.DeviceAlarm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 设备报警表Service
 * @author ds
 * @version 2023-06-29
 */
@Service
@Transactional(readOnly=true)
public class DeviceAlarmService extends CrudService<DeviceAlarmDao, DeviceAlarm> {

    /**
     * 获取最新报警记录
     */
    public List<DeviceAlarm> findDeviceAlarm(String deviceCode){
        return super.dao.findDeviceAlarm(deviceCode, 5);
    }

    /**
     * 获取单条数据
     * @param deviceAlarm
     * @return
     */
    @Override
    public DeviceAlarm get(DeviceAlarm deviceAlarm) {
        return super.get(deviceAlarm);
    }

    /**
     * 查询分页数据
     * @param deviceAlarm 查询条件
     * @return
     */
    @Override
    public Page<DeviceAlarm> findPage(DeviceAlarm deviceAlarm) {
        return super.findPage(deviceAlarm);
    }

    /**
     * 查询列表数据
     * @param deviceAlarm
     * @return
     */
    @Override
    public List<DeviceAlarm> findList(DeviceAlarm deviceAlarm) {
        return super.findList(deviceAlarm);
    }

    /**
     * 保存数据（插入或更新）
     * @param deviceAlarm
     */
    @Override
    @Transactional(readOnly=false)
    public void save(DeviceAlarm deviceAlarm) {
        super.save(deviceAlarm);
    }

    /**
     * 更新状态
     * @param deviceAlarm
     */
    @Override
    @Transactional(readOnly=false)
    public void updateStatus(DeviceAlarm deviceAlarm) {
        super.updateStatus(deviceAlarm);
    }

    /**
     * 删除数据
     * @param deviceAlarm
     */
    @Override
    @Transactional(readOnly=false)
    public void delete(DeviceAlarm deviceAlarm) {
        super.delete(deviceAlarm);
    }

}