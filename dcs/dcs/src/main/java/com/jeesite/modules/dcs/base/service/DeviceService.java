package com.jeesite.modules.dcs.base.service;

import java.util.List;

import com.cecec.api.redis.RedisKeyUtil;
import com.cscec.common.imir.dto.state.DeviceStatus;
import com.cecec.api.enumerate.RunStatus;
import com.jeesite.modules.dcs.base.dto.DeviceDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.dcs.base.entity.Device;
import com.jeesite.modules.dcs.base.dao.DeviceDao;

import javax.annotation.Resource;

/**
 * 设备信息Service
 * @author ds
 * @version 2023-06-25
 */
@Service
@Transactional(readOnly=true)
public class DeviceService extends CrudService<DeviceDao, Device> {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取单条数据
     * @param device
     * @return
     */
    @Override
    public Device get(Device device) {
        return super.get(device);
    }

    /**
     * 查询分页数据
     * @param device 查询条件
     * @return
     */
    @Override
    public Page<Device> findPage(Device device) {
        return super.findPage(device);
    }

    public List<DeviceDTO> findDeviceList(DeviceDTO device){
        List<DeviceDTO> deviceList = super.dao.findDeviceList(device);
        for (DeviceDTO d : deviceList) {
            d.setRunStatus(getState(d.getDeviceCode()));
        }
        return super.dao.findDeviceList(device);
    }

    public int getState(String deviceCode){
        Object o = redisTemplate.opsForValue().get(RedisKeyUtil.deviceStatus(deviceCode));
        if(o != null){
            return ((DeviceStatus)o).getRunStatus();
        }else {
            return RunStatus.Close.getIndex();
        }
    }

    /**
     * 查询列表数据
     * @param device
     * @return
     */
    @Override
    public List<Device> findList(Device device) {
        return super.findList(device);
    }

    /**
     * 保存数据（插入或更新）
     * @param device
     */
    @Override
    @Transactional(readOnly=false)
    public void save(Device device) {
        super.save(device);
    }

    /**
     * 更新状态
     * @param device
     */
    @Override
    @Transactional(readOnly=false)
    public void updateStatus(Device device) {
        super.updateStatus(device);
    }

    /**
     * 删除数据
     * @param device
     */
    @Override
    @Transactional(readOnly=false)
    public void delete(Device device) {
        super.delete(device);
    }

}