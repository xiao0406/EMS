package com.jeesite.modules.ems.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.dao.EmsAreaMeterDao;
import com.jeesite.modules.ems.entity.*;
import org.apache.commons.lang3.ArrayUtils;
import org.beetl.ext.fn.ArrayUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

/**
 * 有功电量Service
 *
 * @author 李鹏
 * @version 2023-06-14
 */
@Service
@Transactional(readOnly = true)
public class EmsDeviceWorkEfficiencyService extends CrudService<EmsAreaMeterDao, EmsAreaMeter> {

    @Resource
    private EmsElectricPowerConsumptionStatisticsService emsElectricPowerConsumptionStatisticsService;

    public EChart workEfficiencyEChart(DeviceWorkEfficiencyQryEntity entity) {
        //查询数据
        List<DeviceWorkEfficiencyEntity> listData = workEfficiencyListData(entity);
        Collections.reverse(listData);
        //组装数据
        List<String> xAxes = ListUtils.newArrayList();
        List<Double> energyAxes = ListUtils.newArrayList();
        List<Double> yieldAxes = ListUtils.newArrayList();
        List<Double> efficiencyAxes = ListUtils.newArrayList();
        listData.stream().forEach(obj -> {
            String label = obj.getDateKey();
            //组装X轴
            xAxes.add(label);
            //组装Y轴
            energyAxes.add(obj.getEnergy());
            yieldAxes.add(obj.getYield());
            efficiencyAxes.add(obj.getEfficiency());
        });

        //组装EChart
        EChartBody body = new EChartBody();
        body.setX(xAxes);
        body.setY(ListUtils.newArrayList(
                new EChartItem("用电量（kwh）", energyAxes),
                new EChartItem("产量（吨）", yieldAxes),
                new EChartItem("工效（kwh/吨）", efficiencyAxes)));
        return EChart.builder().body(body).build();
    }

    public Page<DeviceWorkEfficiencyEntity> workEfficiencyPageList(DeviceWorkEfficiencyQryEntity entity, HttpServletRequest request, HttpServletResponse response) {
        Page<DeviceWorkEfficiencyEntity> page = new Page<>(request, response);
        entity.setPage(page);
        page.setList(workEfficiencyListData(entity));
        return page;
    }

    public List<DeviceWorkEfficiencyEntity> workEfficiencyListData(DeviceWorkEfficiencyQryEntity entity) {
        return emsElectricPowerConsumptionStatisticsService.workEfficiencyListData(entity);
    }
}