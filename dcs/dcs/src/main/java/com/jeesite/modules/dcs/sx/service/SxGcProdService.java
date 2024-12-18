package com.jeesite.modules.dcs.sx.service;

import com.jeesite.common.service.CrudService;
import com.jeesite.modules.dcs.sx.dao.SxGcProdDao;
import com.jeesite.modules.dcs.sx.dto.RunRecordDTO;
import com.jeesite.modules.dcs.sx.dto.WorkEfficiencyDTO;
import com.jeesite.modules.dcs.sx.dto.YieldDTO;
import com.jeesite.modules.dcs.sx.entity.SxGcProd;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 坡口切割单元生产数据记录表Service
 * @author ds
 * @version 2023-06-29
 */
@Service
@Transactional(readOnly=true)
public class SxGcProdService extends CrudService<SxGcProdDao, SxGcProd> {

    public static final Integer DATE_RANGE = 7;

    /**
     * 设备运行记录，默认查询7天数据
     */
    public List<RunRecordDTO> deviceRunRecord(String deviceCode){
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(DATE_RANGE - 1);
        return super.dao.deviceRunRecord(deviceCode, startDate, endDate);
    }

    /**
     * 设备运行时间图表，默认查询7天数据
     */
    public List<SxGcProd> deviceRunTime(String deviceCode){
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(DATE_RANGE - 1);
        List<SxGcProd> dataList = super.dao.deviceRunTime(deviceCode, startDate, endDate);
        if(dataList.size() == DATE_RANGE){
            return dataList;
        }
        Map<String, Integer> dataMap = new HashMap<>();
        for (int i = 0; i < dataList.size(); i++) {
            dataMap.put(dataList.get(i).getRecordDate().toString(), i);
        }
        List<SxGcProd> newDataList = new ArrayList<>();
        for (int i = 0; i < DATE_RANGE; i++) {
            String date = startDate.plusDays(i).toString();
            if(dataMap.containsKey(date)){
                Integer index = dataMap.get(date);
                newDataList.add(dataList.get(index));
            }else {
                SxGcProd data = new SxGcProd();
                data.setRecordDate(LocalDate.parse(date));
                data.setRunTimeDay(0d);
                data.setRunTimeNight(0d);
                newDataList.add(data);
            }
        }
        return newDataList;
    }

    /**
     * 设备稼动率
     */
    public SxGcProd deviceUtilization(String deviceCode){
        return super.dao.deviceUtilization(deviceCode, LocalDate.now());
    }

    /**
     * 设备工效统计，默认查询7天数据
     */
    public List<WorkEfficiencyDTO> deviceWorkEfficiency(String deviceCode){
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(DATE_RANGE - 1);
        List<WorkEfficiencyDTO> dtoList = super.dao.deviceWorkEfficiency(deviceCode, startDate, endDate);
        if(dtoList.size() == DATE_RANGE){
            return dtoList;
        }
        Map<String, Integer> dtoMap = new HashMap<>();
        for (int i = 0; i < dtoList.size(); i++) {
            dtoMap.put(dtoList.get(i).getRecordDate().toString(), i);
        }
        List<WorkEfficiencyDTO> newDataList = new ArrayList<>();
        for (int i = 0; i < DATE_RANGE; i++) {
            String date = startDate.plusDays(i).toString();
            if(dtoMap.containsKey(date)){
                Integer index = dtoMap.get(date);
                newDataList.add(dtoList.get(index));
            }else {
                WorkEfficiencyDTO dto = new WorkEfficiencyDTO();
                dto.setRecordDate(LocalDate.parse(date));
                dto.setWorkEfficiency(BigDecimal.ZERO);
                newDataList.add(dto);
            }
        }
        return newDataList;
    }


    /**
     * 切割米数图表，默认查询7天数据
     */
    public List<SxGcProd> deviceCuttingMeters(String deviceCode){
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(DATE_RANGE - 1);
        List<SxGcProd> dataList = super.dao.deviceCuttingMeters(deviceCode, startDate, endDate);
        if(dataList.size() == DATE_RANGE){
            return dataList;
        }
        Map<String, Integer> dataMap = new HashMap<>();
        for (int i = 0; i < dataList.size(); i++) {
            dataMap.put(dataList.get(i).getRecordDate().toString(), i);
        }
        List<SxGcProd> newDataList = new ArrayList<>();
        for (int i = 0; i < DATE_RANGE; i++) {
            String date = startDate.plusDays(i).toString();
            if(dataMap.containsKey(date)){
                Integer index = dataMap.get(date);
                newDataList.add(dataList.get(index));
            }else {
                SxGcProd data = new SxGcProd();
                data.setRecordDate(LocalDate.parse(date));
                data.setMetersDay(0d);
                data.setMetersNight(0d);
                newDataList.add(data);
            }
        }
        return newDataList;
    }

    /**
     * 设备产量，默认查询7天数据
     */
    public List<YieldDTO> deviceYield(String deviceCode){
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(DATE_RANGE - 1);
        List<YieldDTO> dtoList = super.dao.deviceYield(deviceCode, startDate, endDate);
        if(dtoList.size() == DATE_RANGE){
            return dtoList;
        }
        Map<String, Integer> dataMap = new HashMap<>();
        for (int i = 0; i < dtoList.size(); i++) {
            dataMap.put(dtoList.get(i).getRecordDate().toString(), i);
        }
        List<YieldDTO> newDataList = new ArrayList<>();
        for (int i = 0; i < DATE_RANGE; i++) {
            String date = startDate.plusDays(i).toString();
            if(dataMap.containsKey(date)){
                Integer index = dataMap.get(date);
                newDataList.add(dtoList.get(index));
            }else {
                YieldDTO dto = new YieldDTO();
                dto.setRecordDate(LocalDate.parse(date));
                dto.setYield(BigDecimal.ZERO);
                newDataList.add(dto);
            }
        }
        return newDataList;
    }
}