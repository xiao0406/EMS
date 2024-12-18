package com.jeesite.modules.ems.service;

import cn.hutool.core.util.ObjectUtil;
import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.NumberUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.dao.EmsTimeShareDeviceRuntimeDao;
import com.jeesite.modules.ems.entity.*;
import com.jeesite.modules.ems.entity.enums.DeviceStatusEnum;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 峰平谷设备运行时长表Service
 *
 * @author 李鹏
 * @version 2023-06-27
 */
@Service
@Transactional(readOnly=true)
public class EmsTimeShareDeviceRuntimeService extends CrudService<EmsTimeShareDeviceRuntimeDao, EmsTimeShareDeviceRuntime> {

    public static final String HOUR_STRING = "小时";
    public static final String MIN_STRING = "分钟";

	@Autowired
	private EmsElectricPowerConsumptionService emsElectricPowerConsumptionService;

	@Autowired
	private EmsMeterService emsMeterService;
	/**
	 * 获取单条数据
	 * @param emsTimeShareDeviceRuntime
	 * @return
	 */
	@Override
	public EmsTimeShareDeviceRuntime get(EmsTimeShareDeviceRuntime emsTimeShareDeviceRuntime) {
		return super.get(emsTimeShareDeviceRuntime);
	}
	
	/**
	 * 查询分页数据
	 * @param emsTimeShareDeviceRuntime 查询条件
	 * @return
	 */
	@Override
	public Page<EmsTimeShareDeviceRuntime> findPage(EmsTimeShareDeviceRuntime emsTimeShareDeviceRuntime) {
		return super.findPage(emsTimeShareDeviceRuntime);
	}
	
	/**
	 * 查询列表数据
	 * @param emsTimeShareDeviceRuntime
	 * @return
	 */
	@Override
	public List<EmsTimeShareDeviceRuntime> findList(EmsTimeShareDeviceRuntime emsTimeShareDeviceRuntime) {
		return super.findList(emsTimeShareDeviceRuntime);
	}

    /**
     * 查询根据电表排序顺序的数据列表
     * @param emsTimeShareDeviceRuntime
     * @return
     */
    public List<EmsTimeShareDeviceRuntime> findMeterSortList(EmsTimeShareDeviceRuntime emsTimeShareDeviceRuntime) {
        return this.dao.findMeterSortList(emsTimeShareDeviceRuntime);
    }
	
	/**
	 * 保存数据（插入或更新）
	 * @param emsTimeShareDeviceRuntime
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(EmsTimeShareDeviceRuntime emsTimeShareDeviceRuntime) {
		super.save(emsTimeShareDeviceRuntime);
	}
	
	/**
	 * 更新状态
	 * @param emsTimeShareDeviceRuntime
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(EmsTimeShareDeviceRuntime emsTimeShareDeviceRuntime) {
		super.updateStatus(emsTimeShareDeviceRuntime);
	}
	
	/**
	 * 删除数据
	 * @param emsTimeShareDeviceRuntime
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(EmsTimeShareDeviceRuntime emsTimeShareDeviceRuntime) {
		super.delete(emsTimeShareDeviceRuntime);
	}

	public EmsTimeShareDeviceRuntime isStockedRec(EmsTimeShareDeviceRuntime build) {
		return this.dao.isStockedRec(build);
	}

	/**
	 * 单日设备利用率
	 * @param dailyStatisticsParam
	 * @return
	 */
    public EmsDailyStatisticsEntity dailyStatistics(DailyStatisticsParam dailyStatisticsParam) {
        // 查询设备单日用电数据
        EmsTimeShareDeviceRuntime runtimeParam = new EmsTimeShareDeviceRuntime();
        runtimeParam.setDeviceId(dailyStatisticsParam.getDeviceId());
        runtimeParam.setDataDate(dailyStatisticsParam.getDate());
        runtimeParam.setDataType(TemporalGranularityEnum.VD_Day.getCode());
        EmsTimeShareDeviceRuntime deviceRuntime = this.dao.getByEntity(runtimeParam);

        if (ObjectUtil.isEmpty(deviceRuntime)) {
            return new EmsDailyStatisticsEntity();
        }
        // 结果数据组装
        EmsDailyStatisticsEntity entity = new EmsDailyStatisticsEntity();
        Double totalTime = NumberUtils.addAll(deviceRuntime.getTotalNoLoad(), deviceRuntime.getTotalStop(), deviceRuntime.getTotalRunning());
        int totalTimeInt = totalTime.intValue();
        // 设备ID
        entity.setDeviceId(dailyStatisticsParam.getDeviceId());
        // 总时长
        entity.setTotalTime(totalTimeInt / 60 + HOUR_STRING + totalTimeInt % 60 + MIN_STRING);
        // 停机时常
        int totalStop = deviceRuntime.getTotalStop().intValue();
        entity.setShutDownTime(totalStop / 60 + HOUR_STRING + totalStop % 60 + MIN_STRING);
        // 停机率
        Double shutDownRate = NumberUtils.div(NumberUtils.mul(totalStop, 100), totalTimeInt, 1);
        entity.setShutDownRate(shutDownRate.toString());
        // 空载时常
        int totalNoLoad = deviceRuntime.getTotalNoLoad().intValue();
        entity.setUnloadedTime(totalNoLoad / 60 + HOUR_STRING + totalNoLoad % 60 + MIN_STRING);
        // 空载率
        Double unloadRate = NumberUtils.div(NumberUtils.mul(totalNoLoad, 100), totalTimeInt, 1);
        entity.setUnloadedRate(unloadRate.toString());
        // 运行时常
        int operation = deviceRuntime.getTotalRunning().intValue();
        entity.setOperationTime(operation / 60 + HOUR_STRING + operation % 60 + MIN_STRING);
        // 运行率
        Double operationRate = NumberUtils.div(NumberUtils.mul(operation, 100), totalTimeInt, 1);
        entity.setOperationRate(operationRate.toString());

        return entity;
    }

    /**
     * 单日设备利用率，历史记录
     *
     * @param emsTimeShareDeviceRuntime
     * @param request
     * @param response
     * @return
     */
    public Page<EmsDailyStatisticsListEntity> pageList(EmsTimeShareDeviceRuntime emsTimeShareDeviceRuntime, HttpServletRequest request, HttpServletResponse response) {
        // 查询时长信息
        EmsTimeShareDeviceRuntime params = new EmsTimeShareDeviceRuntime();
        params.setDeviceId(emsTimeShareDeviceRuntime.getDeviceId());
        params.setDataDateStart(emsTimeShareDeviceRuntime.getDataDateStart());
        params.setDataDateEnd(emsTimeShareDeviceRuntime.getDataDateEnd());
        params.setDataType(TemporalGranularityEnum.VD_Day.getCode());
        params.setPage(new Page<EmsTimeShareDeviceRuntime>(request, response));
        params.getSqlMap().getOrder().setOrderBy("a.data_date desc");
        Page<EmsTimeShareDeviceRuntime> page = this.findPage(params);

        List<EmsTimeShareDeviceRuntime> list = page.getList();
        if (CollectionUtils.isEmpty(list)) {
            return new Page<>();
        }
        // 查询总的能耗
        EmsElectricPowerConsumption consumptionParams = new EmsElectricPowerConsumption();
        consumptionParams.setDeviceId(emsTimeShareDeviceRuntime.getDeviceId());
        consumptionParams.setQryStartTime(emsTimeShareDeviceRuntime.getDataDateStart());
        consumptionParams.setQryEndTime(emsTimeShareDeviceRuntime.getDataDateEnd());
        consumptionParams.setDataType(TemporalGranularityEnum.VD_Day.getCode());
        List<EmsElectricPowerConsumption> consList = emsElectricPowerConsumptionService.findList(consumptionParams);
        Map<Date, EmsElectricPowerConsumption> consMap = consList.stream().collect(
                Collectors.toMap(
                        EmsElectricPowerConsumption::getDataDate,
                        EmsElectricPowerConsumption -> EmsElectricPowerConsumption,
                        (key1, key2) -> key2,
                        LinkedHashMap::new));

        // 数据组装
        Page<EmsDailyStatisticsListEntity> resultPage = new Page();
        resultPage.setPageNo(page.getPageNo());
        resultPage.setPageSize(page.getPageSize());
        resultPage.setCount(page.getCount());
        List<EmsDailyStatisticsListEntity> resultList = new ArrayList<>();
        for (EmsTimeShareDeviceRuntime runtime : list) {
            Date dataDate = runtime.getDataDate();

            EmsDailyStatisticsListEntity entity = new EmsDailyStatisticsListEntity();
            entity.setDeviceName(runtime.getDeviceName());
            entity.setDataDate(DateUtils.formatDate(dataDate));
            double totalTime = runtime.getTotalNoLoad() + runtime.getTotalRunning() + runtime.getTotalStop();
            entity.setTotalTime(new BigDecimal(totalTime / 60).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            entity.setShutDownTime(new BigDecimal(runtime.getTotalStop() / 60).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            entity.setUnloadedTime(new BigDecimal(runtime.getTotalNoLoad() / 60).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            entity.setOperationTime(new BigDecimal(runtime.getTotalRunning() / 60).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            EmsElectricPowerConsumption emsElectricPowerConsumption = consMap.get(dataDate);
            if (Objects.nonNull(emsElectricPowerConsumption)) {
                entity.setPositiveActiveEnergy(emsElectricPowerConsumption.getPositiveActiveEnergy());
            }
            resultList.add(entity);
        }
        resultPage.setList(resultList);
        return resultPage;
    }

    /**
     * 查询当前设备的运行和空载阈值
     *
     * @param emsMeterThresholdEntity
     * @param request
     * @param response
     * @return
     */
    public EmsMeterThresholdEntity thresholdValue(EmsMeterThresholdEntity emsMeterThresholdEntity, HttpServletRequest request, HttpServletResponse response) {
        EmsMeter meter = new EmsMeter();
        meter.setMeterCode(emsMeterThresholdEntity.getDeviceId());
        EmsMeter emsMeter = emsMeterService.getByEntity(meter);
        EmsMeterThresholdEntity result = new EmsMeterThresholdEntity();
        result.setDeviceId(emsMeterThresholdEntity.getDeviceId());
        result.setNoLoadThreshold(emsMeter.getNoLoadThreshold());
        result.setOperationThreshold(emsMeter.getOperationTreshold());
        return result;
    }

    /**
     * @param dailyStatisticsParam
     * @param request
     * @param response
     * @return
     */
    public EmsDailyStatisticsEchartEntity chartValue(DailyStatisticsParam dailyStatisticsParam, HttpServletRequest request, HttpServletResponse response) {
        EmsDailyStatisticsEchartEntity chart = new EmsDailyStatisticsEchartEntity();
        // step 1: 按照日期找到当前日期的所有刻度节点
        // 查询总的能耗
        EmsElectricPowerConsumption consumptionParams = new EmsElectricPowerConsumption();
        consumptionParams.setDeviceId(dailyStatisticsParam.getDeviceId());
        consumptionParams.setDataType(TemporalGranularityEnum.VD_Quarter.getCode());
        consumptionParams.setQryStartTime(DateUtils.parseDate(DateUtils.formatDate(dailyStatisticsParam.getDate(),"yyyy-MM-dd 00:00:10")));
        consumptionParams.setQryEndTime(DateUtils.parseDate(DateUtils.formatDate(DateUtils.calculateDay(dailyStatisticsParam.getDate(),+1),"yyyy-MM-dd 00:00:10")));

        List<EmsElectricPowerConsumption> powerList = emsElectricPowerConsumptionService.findList(consumptionParams);
        if (ObjectUtil.isEmpty(powerList)) {
            return chart;
        }

        // 当前设备的综合倍率 阈值信息
        EmsMeter meter = new EmsMeter();
        meter.setMeterCode(dailyStatisticsParam.getDeviceId());
        EmsMeter emsMeter = emsMeterService.getByEntity(meter);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        List<EChartData> consumptionDataList = new ArrayList<>();
        List<EChartData> statusDataList = new ArrayList<>();
        for (int i = 0; i < powerList.size(); i++) {
            EmsElectricPowerConsumption powerConsumption = powerList.get(i);
            EChartData consumptionData = new EChartData();
            EChartData statusData = new EChartData();
            // 能耗
            consumptionData.setLabel(format.format(powerConsumption.getDataDateTime()));
            consumptionData.setValue(String.valueOf(powerConsumption.getPositiveActiveEnergy()));
            consumptionDataList.add(consumptionData);
            // 状态
            statusData.setLabel(format.format(powerConsumption.getDataDateTime()));
            statusData.setValue(getStatusByPower(powerConsumption, emsMeter));
            statusDataList.add(statusData);
        }
        chart.setConsumptionData(consumptionDataList);
        chart.setStatusData(statusDataList);
        return chart;
    }

    /**
     * 根据能耗计算当前设备状态
     *
     * @param powerConsumption
     * @param emsMeter
     * @return
     */
    private String getStatusByPower(EmsElectricPowerConsumption powerConsumption, EmsMeter emsMeter) {
        Double positiveActiveEnergy = powerConsumption.getPositiveActiveEnergy();
        Double noLoadThreshold = emsMeter.getNoLoadThreshold();
        Double operationTreshold = emsMeter.getOperationTreshold();
        if (positiveActiveEnergy != null && noLoadThreshold != null && operationTreshold != null) {
            if (positiveActiveEnergy.compareTo(noLoadThreshold) <= 0) {
                return DeviceStatusEnum.STATUS_STOP.getCode();
            }
            if (positiveActiveEnergy.compareTo(noLoadThreshold) > 0 && positiveActiveEnergy.compareTo(operationTreshold) <= 0) {
                return DeviceStatusEnum.STATUS_NO_LOAD.getCode();
            }
            if (positiveActiveEnergy.compareTo(operationTreshold) > 0) {
                return DeviceStatusEnum.STATUS_RUNNING.getCode();
            }
        }

        return DeviceStatusEnum.STATUS_UNKNOWN.getCode();
    }
}