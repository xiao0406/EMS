package com.jeesite.modules.dcs.yh.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jeesite.common.entity.Page;

import com.jeesite.common.service.CrudService;
import com.jeesite.modules.dcs.yh.dao.YhCwProdDao;
import com.jeesite.modules.dcs.yh.dto.DeviceCapacityDTO;
import com.jeesite.modules.dcs.yh.entity.YhCwProd;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 * 部件焊接单元生产数据记录表Service
 * @author ds
 * @version 2023-06-25
 */
@Service
@Transactional(readOnly=true)
public class YhCwProdService extends CrudService<YhCwProdDao, YhCwProd> {

    public static final Integer DATE_RANGE = 7;

    @Transactional(readOnly=false)
    public void updateData(YhCwProd data){
        this.dao.updateData(data);
    }

    /**
     * 设备运行时间图表，默认查询7天数据
     */
    public List<YhCwProd> deviceRunTime(String deviceCode){
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(DATE_RANGE - 1);
        List<YhCwProd> dtoList = super.dao.deviceRunTime(deviceCode, startDate, endDate);
        if(dtoList.size() == DATE_RANGE){
            return dtoList;
        }
        Map<String, Integer> dtoMap = new HashMap<>();
        for (int i = 0; i < dtoList.size(); i++) {
            dtoMap.put(dtoList.get(i).getRecordDate().toString(), i);
        }
        List<YhCwProd> newDataList = new ArrayList<>();
        for (int i = 0; i < DATE_RANGE; i++) {
            String date = startDate.plusDays(i).toString();
            if(dtoMap.containsKey(date)){
                Integer index = dtoMap.get(date);
                newDataList.add(dtoList.get(index));
            }else {
                YhCwProd data = new YhCwProd();
                data.setRecordDate(LocalDate.parse(date));
                data.setDayRunTime(0d);
                data.setNightRunTime(0d);
                newDataList.add(data);
            }
        }
        return newDataList;
    }

    /**
     * 设备焊丝用量图表，默认查询7天数据
     */
    public List<YhCwProd> deviceWire(String deviceCode){
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(DATE_RANGE - 1);
        List<YhCwProd> dtoList = super.dao.deviceWire(deviceCode, startDate, endDate);
        if(dtoList.size() == DATE_RANGE){
            return dtoList;
        }
        Map<String, Integer> dtoMap = new HashMap<>();
        for (int i = 0; i < dtoList.size(); i++) {
            dtoMap.put(dtoList.get(i).getRecordDate().toString(), i);
        }
        List<YhCwProd> newDataList = new ArrayList<>();
        for (int i = 0; i < DATE_RANGE; i++) {
            String date = startDate.plusDays(i).toString();
            if(dtoMap.containsKey(date)){
                Integer index = dtoMap.get(date);
                newDataList.add(dtoList.get(index));
            }else {
                YhCwProd dto = new YhCwProd();
                dto.setRecordDate(LocalDate.parse(date));
                dto.setWireLength(0d);
                dto.setWireWeight(0d);
                newDataList.add(dto);
            }
        }
        return newDataList;
    }


    /**
     * 设备产量图表，默认查询7天数据
     */
    public List<DeviceCapacityDTO> deviceCapacity(String deviceCode){
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(DATE_RANGE - 1);
        List<DeviceCapacityDTO> dtoList = super.dao.deviceCapacity(deviceCode, startDate, endDate);
        if(dtoList.size() == DATE_RANGE){
            return dtoList;
        }
        Map<String, Integer> dtoMap = new HashMap<>();
        for (int i = 0; i < dtoList.size(); i++) {
            dtoMap.put(dtoList.get(i).getRecordDate().toString(), i);
        }
        List<DeviceCapacityDTO> newDtoList = new ArrayList<>();
        for (int i = 0; i < DATE_RANGE; i++) {
            String date = startDate.plusDays(i).toString();
            if(dtoMap.containsKey(date)){
                Integer index = dtoMap.get(date);
                newDtoList.add(dtoList.get(index));
            }else {
                DeviceCapacityDTO dto = new DeviceCapacityDTO();
                dto.setRecordDate(LocalDate.parse(date));
                dto.setCapacity(0d);
                newDtoList.add(dto);
            }
        }
        return newDtoList;
    }

    public YhCwProd deviceUtilization(String deviceCode){
        return super.dao.deviceUtilization(deviceCode, LocalDate.now());
    }


    /**
     * 获取单条数据
     * @param yhCwProd
     * @return
     */
    @Override
    public YhCwProd get(YhCwProd yhCwProd) {
        return super.get(yhCwProd);
    }

    /**
     * 查询分页数据
     * @param yhCwProd 查询条件
     * @return
     */
    @Override
    public Page<YhCwProd> findPage(YhCwProd yhCwProd) {
        return super.findPage(yhCwProd);
    }

    /**
     * 查询列表数据
     * @param yhCwProd
     * @return
     */
    @Override
    public List<YhCwProd> findList(YhCwProd yhCwProd) {
        return super.findList(yhCwProd);
    }

    /**
     * 保存数据（插入或更新）
     * @param yhCwProd
     */
    @Override
    @Transactional(readOnly=false)
    public void save(YhCwProd yhCwProd) {
        super.save(yhCwProd);
    }

    /**
     * 更新状态
     * @param yhCwProd
     */
    @Override
    @Transactional(readOnly=false)
    public void updateStatus(YhCwProd yhCwProd) {
        super.updateStatus(yhCwProd);
    }

    /**
     * 删除数据
     * @param yhCwProd
     */
    @Override
    @Transactional(readOnly=false)
    public void delete(YhCwProd yhCwProd) {
        super.delete(yhCwProd);
    }

}