package com.jeesite.modules.ems.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.NumberUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.dao.EmsTimeSharePowerConsumptionDao;
import com.jeesite.modules.ems.entity.*;
import com.jeesite.modules.ems.entity.enums.EnergyGroupEnum;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.UserHelper;
import com.jeesite.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * 峰平谷电耗表数据基础表Service
 *
 * @author 李鹏
 * @version 2023-06-20
 */
@Service
@Transactional(readOnly = true)
public class EmsTimeSharePowerConsumptionService extends CrudService<EmsTimeSharePowerConsumptionDao, EmsTimeSharePowerConsumption> {

    @Resource
    private EmsTimeSharePowerConsumptionStatisticsService emsTimeSharePowerConsumptionStatisticsService;
    @Resource
    private EmsTimeShareAreaPowerConsumptionService emsTimeShareAreaPowerConsumptionService;
    @Resource
    private EmsTimeShareAreaPowerConsumptionStatisticsService emsTimeShareAreaPowerConsumptionStatisticsService;

    /**
     * 获取单条数据
     *
     * @param emsTimeSharePowerConsumption
     * @return
     */
    @Override
    public EmsTimeSharePowerConsumption get(EmsTimeSharePowerConsumption emsTimeSharePowerConsumption) {
        return super.get(emsTimeSharePowerConsumption);
    }

    /**
     * 查询分页数据
     *
     * @param emsTimeSharePowerConsumption 查询条件
     * @return
     */
    @Override
    public Page<EmsTimeSharePowerConsumption> findPage(EmsTimeSharePowerConsumption emsTimeSharePowerConsumption) {
        return super.findPage(emsTimeSharePowerConsumption);
    }

    /**
     * 查询列表数据
     *
     * @param emsTimeSharePowerConsumption
     * @return
     */
    @Override
    public List<EmsTimeSharePowerConsumption> findList(EmsTimeSharePowerConsumption emsTimeSharePowerConsumption) {
        return super.findList(emsTimeSharePowerConsumption);
    }

    /**
     * 查询根据电表排序顺序的数据列表
     *
     * @param emsTimeSharePowerConsumption
     * @return
     */
    public List<EmsTimeSharePowerConsumption> findMeterSortList(EmsTimeSharePowerConsumption emsTimeSharePowerConsumption) {
        return this.dao.findMeterSortList(emsTimeSharePowerConsumption);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param emsTimeSharePowerConsumption
     */
    @Override
    @Transactional(readOnly = false)
    public void save(EmsTimeSharePowerConsumption emsTimeSharePowerConsumption) {
        super.save(emsTimeSharePowerConsumption);
    }

    /**
     * 更新状态
     *
     * @param emsTimeSharePowerConsumption
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(EmsTimeSharePowerConsumption emsTimeSharePowerConsumption) {
        super.updateStatus(emsTimeSharePowerConsumption);
    }

    /**
     * 删除数据
     *
     * @param emsTimeSharePowerConsumption
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(EmsTimeSharePowerConsumption emsTimeSharePowerConsumption) {
        super.delete(emsTimeSharePowerConsumption);
    }

    public EmsTimeSharePowerConsumption isStockedRec(EmsTimeSharePowerConsumption build) {
        return this.dao.isStockedRec(build);
    }

    public EmsTimeSharePowerConsumption findMeterDailyConsumption(EmsTimeSharePowerConsumption where) {
        return this.dao.findMeterDailyConsumption(where);
    }

    public List<EmsTimeSharePowerConsumption> getStageConsumption(EmsTimeSharePowerConsumption entity) {
        if (StringUtils.isBlank(entity.getCompanyCode())){
            User user = UserHelper.getUser();
            Company company = EmsUserHelper.userCompany(true, "当前为租户管理员账号，非法业务操作");
            entity.setCompanyCode(company.getCompanyCode());
            entity.setCorpCode(user.getCorpCode());
        }
        return this.dao.getStageConsumption(entity);
    }

    public TimeShareStatisticsEntity timeShareStatistics(TimeSharePowerQueryEntity timeSharePowerQueryEntity) {
        User loginUser = UserUtils.getUser();
        EnergyGroupEnum energyGroup = timeSharePowerQueryEntity.getEnergyGroup();
        TimeShareStatisticsEntity rlt = null;
        switch (energyGroup) {
            case GROUP_Area:
                rlt = timeShareAreaStatistics(loginUser, timeSharePowerQueryEntity);
                break;
            case GROUP_Device:
                rlt = timeShareDeviceStatistics(loginUser, timeSharePowerQueryEntity);
                break;
        }
        return rlt;
    }

    private TimeShareStatisticsEntity timeShareDeviceStatistics(User loginUser, TimeSharePowerQueryEntity timeSharePowerQueryEntity) {
        TemporalGranularityEnum temporalGranularity = timeSharePowerQueryEntity.getTemporalGranularity();
        //根据查询维度路由逻辑分支
        TimeShareStatisticsEntity statistics = null;
        switch (temporalGranularity) {
            case VD_Day:
                EmsTimeSharePowerConsumption dayWhere = new EmsTimeSharePowerConsumption();
                dayWhere.setDeviceId(timeSharePowerQueryEntity.getDeviceId());
                dayWhere.setDataType(temporalGranularity.getCode());
                dayWhere.setDataDateStart(DateUtils.parseDate(timeSharePowerQueryEntity.getQryStartTime()));
                dayWhere.setDataDateEnd(DateUtils.parseDate(timeSharePowerQueryEntity.getQryEndTime()));
                statistics = this.dao.timeShareStatistics(dayWhere);
                break;
            case VD_Month:
            case VD_Year:
                EmsTimeSharePowerConsumptionStatistics monthWhere = new EmsTimeSharePowerConsumptionStatistics();
                monthWhere.setDeviceId(timeSharePowerQueryEntity.getDeviceId());
                monthWhere.setDataType(temporalGranularity.getCode());
                monthWhere.setQryStartTime(timeSharePowerQueryEntity.getQryStartTime());
                monthWhere.setQryEndTime(timeSharePowerQueryEntity.getQryEndTime());
                statistics = emsTimeSharePowerConsumptionStatisticsService.timeShareStatistics(monthWhere);
                break;
        }
        if (Objects.nonNull(statistics)) {
            //百分比计算
            Double totalEnergy = statistics.getTotalEnergy();
            statistics.setCuspTimeEnergyPercent(NumberUtils.mul(NumberUtils.div(statistics.getCuspTimeEnergy(), totalEnergy, 2), 100));
            statistics.setPeakTimeEnergyPercent(NumberUtils.mul(NumberUtils.div(statistics.getPeakTimeEnergy(), totalEnergy, 2), 100));
            statistics.setFairTimeEnergyPercent(NumberUtils.mul(NumberUtils.div(statistics.getFairTimeEnergy(), totalEnergy, 2), 100));
            statistics.setValleyTimeEnergyPercent(NumberUtils.mul(NumberUtils.div(statistics.getValleyTimeEnergy(), totalEnergy, 2), 100));
        }
        return statistics;
    }

    private TimeShareStatisticsEntity timeShareAreaStatistics(User loginUser, TimeSharePowerQueryEntity timeSharePowerQueryEntity) {
        TemporalGranularityEnum temporalGranularity = timeSharePowerQueryEntity.getTemporalGranularity();
        //根据查询维度路由逻辑分支
        TimeShareStatisticsEntity statistics = null;
        switch (temporalGranularity) {
            case VD_Day:
                EmsTimeShareAreaPowerConsumption dayWhere = new EmsTimeShareAreaPowerConsumption();
                dayWhere.setAreaCode(timeSharePowerQueryEntity.getAreaCode());
                dayWhere.setDataType(temporalGranularity.getCode());
                dayWhere.setDataDateStart(DateUtils.parseDate(timeSharePowerQueryEntity.getQryStartTime()));
                dayWhere.setDataDateEnd(DateUtils.parseDate(timeSharePowerQueryEntity.getQryEndTime()));
                statistics = emsTimeShareAreaPowerConsumptionService.timeShareStatistics(dayWhere);
                break;
            case VD_Month:
            case VD_Year:
                EmsTimeShareAreaPowerConsumptionStatistics monthWhere = new EmsTimeShareAreaPowerConsumptionStatistics();
                monthWhere.setAreaCode(timeSharePowerQueryEntity.getAreaCode());
                monthWhere.setDataType(temporalGranularity.getCode());
                monthWhere.setQryStartTime(timeSharePowerQueryEntity.getQryStartTime());
                monthWhere.setQryEndTime(timeSharePowerQueryEntity.getQryEndTime());
                statistics = emsTimeShareAreaPowerConsumptionStatisticsService.timeShareStatistics(monthWhere);
                break;
        }
        if (Objects.nonNull(statistics)) {
            //百分比计算
            Double totalEnergy = statistics.getTotalEnergy();
            statistics.setCuspTimeEnergyPercent(NumberUtils.mul(NumberUtils.div(statistics.getCuspTimeEnergy(), totalEnergy, 2), 100));
            statistics.setPeakTimeEnergyPercent(NumberUtils.mul(NumberUtils.div(statistics.getPeakTimeEnergy(), totalEnergy, 2), 100));
            statistics.setFairTimeEnergyPercent(NumberUtils.mul(NumberUtils.div(statistics.getFairTimeEnergy(), totalEnergy, 2), 100));
            statistics.setValleyTimeEnergyPercent(NumberUtils.mul(NumberUtils.div(statistics.getValleyTimeEnergy(), totalEnergy, 2), 100));
        }
        return statistics;
    }

    public EChart timeShareEChart(TimeSharePowerQueryEntity timeSharePowerQueryEntity) {
        User loginUser = UserUtils.getUser();
        EnergyGroupEnum energyGroup = timeSharePowerQueryEntity.getEnergyGroup();
        EChart rlt = null;
        switch (energyGroup) {
            case GROUP_Area:
                rlt = timeShareAreaEChart(loginUser, timeSharePowerQueryEntity);
                break;
            case GROUP_Device:
                rlt = timeShareDeviceEChart(loginUser, timeSharePowerQueryEntity);
                break;
        }
        return rlt;


    }

    private EChart timeShareDeviceEChart(User loginUser, TimeSharePowerQueryEntity timeSharePowerQueryEntity) {
        TemporalGranularityEnum temporalGranularity = timeSharePowerQueryEntity.getTemporalGranularity();

        //根据查询维度路由逻辑分支
        List<String> xAxes = ListUtils.newArrayList();
        List<Double> cuspAxes = ListUtils.newArrayList();
        List<Double> peakAxes = ListUtils.newArrayList();
        List<Double> fairAxes = ListUtils.newArrayList();
        List<Double> valleyAxes = ListUtils.newArrayList();
        switch (temporalGranularity) {
            case VD_Day:
                EmsTimeSharePowerConsumption dayWhere = new EmsTimeSharePowerConsumption();
                dayWhere.setDeviceId(timeSharePowerQueryEntity.getDeviceId());
                dayWhere.setDataType(temporalGranularity.getCode());
                dayWhere.setDataDateStart(DateUtils.parseDate(timeSharePowerQueryEntity.getQryStartTime()));
                dayWhere.setDataDateEnd(DateUtils.parseDate(timeSharePowerQueryEntity.getQryEndTime()));
                List<EmsTimeSharePowerConsumption> dayData = this.findList(dayWhere);
                dayData.stream().forEach(obj -> {
                    String label = DateUtils.formatDate(obj.getDataDate());
                    //组装X轴
                    xAxes.add(label);
                    //组装Y轴
                    cuspAxes.add(obj.getCuspTimeEnergy());
                    peakAxes.add(obj.getPeakTimeEnergy());
                    fairAxes.add(obj.getFairTimeEnergy());
                    valleyAxes.add(obj.getValleyTimeEnergy());
                });
                break;
            case VD_Month:
            case VD_Year:
                EmsTimeSharePowerConsumptionStatistics monthWhere = new EmsTimeSharePowerConsumptionStatistics();
                monthWhere.setDeviceId(timeSharePowerQueryEntity.getDeviceId());
                monthWhere.setDataType(temporalGranularity.getCode());
                monthWhere.setQryStartTime(timeSharePowerQueryEntity.getQryStartTime());
                monthWhere.setQryEndTime(timeSharePowerQueryEntity.getQryEndTime());
                List<EmsTimeSharePowerConsumptionStatistics> monthData = emsTimeSharePowerConsumptionStatisticsService.findList(monthWhere);
                monthData.stream().forEach(obj -> {
                    String label = obj.getDataDateKey();
                    //组装X轴
                    xAxes.add(label);
                    //组装Y轴
                    cuspAxes.add(obj.getCuspTimeEnergy());
                    peakAxes.add(obj.getPeakTimeEnergy());
                    fairAxes.add(obj.getFairTimeEnergy());
                    valleyAxes.add(obj.getValleyTimeEnergy());
                });
                break;
        }

        //组装数据
        EChartBody body = new EChartBody();
        body.setX(xAxes);
        body.setY(ListUtils.newArrayList(
                new EChartItem("尖", cuspAxes),
                new EChartItem("峰", peakAxes),
                new EChartItem("平", fairAxes),
                new EChartItem("谷", valleyAxes)));
        return EChart.builder().body(body).build();
    }

    private EChart timeShareAreaEChart(User loginUser, TimeSharePowerQueryEntity timeSharePowerQueryEntity) {
        TemporalGranularityEnum temporalGranularity = timeSharePowerQueryEntity.getTemporalGranularity();

        //根据查询维度路由逻辑分支
        List<String> xAxes = ListUtils.newArrayList();
        List<Double> cuspAxes = ListUtils.newArrayList();
        List<Double> peakAxes = ListUtils.newArrayList();
        List<Double> fairAxes = ListUtils.newArrayList();
        List<Double> valleyAxes = ListUtils.newArrayList();
        switch (temporalGranularity) {
            case VD_Day:
                EmsTimeShareAreaPowerConsumption dayWhere = new EmsTimeShareAreaPowerConsumption();
                dayWhere.setAreaCode(timeSharePowerQueryEntity.getAreaCode());
                dayWhere.setDataType(temporalGranularity.getCode());
                dayWhere.setDataDateStart(DateUtils.parseDate(timeSharePowerQueryEntity.getQryStartTime()));
                dayWhere.setDataDateEnd(DateUtils.parseDate(timeSharePowerQueryEntity.getQryEndTime()));
                dayWhere.getSqlMap().getOrder().setOrderBy("a.data_date");
                List<EmsTimeShareAreaPowerConsumption> dayData = emsTimeShareAreaPowerConsumptionService.findList(dayWhere);
                dayData.stream().forEach(obj -> {
                    String label = DateUtils.formatDate(obj.getDataDate());
                    //组装X轴
                    xAxes.add(label);
                    //组装Y轴
                    cuspAxes.add(obj.getCuspTimeEnergy());
                    peakAxes.add(obj.getPeakTimeEnergy());
                    fairAxes.add(obj.getFairTimeEnergy());
                    valleyAxes.add(obj.getValleyTimeEnergy());
                });
                break;
            case VD_Month:
            case VD_Year:
                EmsTimeShareAreaPowerConsumptionStatistics monthWhere = new EmsTimeShareAreaPowerConsumptionStatistics();
                monthWhere.setAreaCode(timeSharePowerQueryEntity.getAreaCode());
                monthWhere.setDataType(temporalGranularity.getCode());
                monthWhere.setQryStartTime(timeSharePowerQueryEntity.getQryStartTime());
                monthWhere.setQryEndTime(timeSharePowerQueryEntity.getQryEndTime());
                monthWhere.getSqlMap().getOrder().setOrderBy("a.data_date_key");
                List<EmsTimeShareAreaPowerConsumptionStatistics> monthData = emsTimeShareAreaPowerConsumptionStatisticsService.findList(monthWhere);
                monthData.stream().forEach(obj -> {
                    String label = obj.getDataDateKey();
                    //组装X轴
                    xAxes.add(label);
                    //组装Y轴
                    cuspAxes.add(obj.getCuspTimeEnergy());
                    peakAxes.add(obj.getPeakTimeEnergy());
                    fairAxes.add(obj.getFairTimeEnergy());
                    valleyAxes.add(obj.getValleyTimeEnergy());
                });
                break;
        }

        //组装数据
        EChartBody body = new EChartBody();
        body.setX(xAxes);
        body.setY(ListUtils.newArrayList(
                new EChartItem("尖", cuspAxes),
                new EChartItem("峰", peakAxes),
                new EChartItem("平", fairAxes),
                new EChartItem("谷", valleyAxes)));
        return EChart.builder().body(body).build();
    }

    public EmsTimeShareAreaPowerConsumption getAreaStageCumulativeConsumption(EmsTimeSharePowerConsumption entity) {
        return this.dao.getAreaStageCumulativeConsumption(entity);
    }

    public Page<TimeSharePowerConsumptionEntity> findTimeSharePage(TimeSharePowerQueryEntity timeSharePowerQueryEntity, HttpServletRequest request, HttpServletResponse response) {
        EnergyGroupEnum energyGroup = timeSharePowerQueryEntity.getEnergyGroup();
        Page<TimeSharePowerConsumptionEntity> rlt = null;
        switch (energyGroup) {
            case GROUP_Area:
                rlt = findAreaTimeSharePage(timeSharePowerQueryEntity, request, response);
                break;
            case GROUP_Device:
                rlt = findDeviceTimeSharePage(timeSharePowerQueryEntity, request, response);
                break;
        }
        return rlt;
    }

    private Page<TimeSharePowerConsumptionEntity> findDeviceTimeSharePage(TimeSharePowerQueryEntity timeSharePowerQueryEntity, HttpServletRequest request, HttpServletResponse response) {
        TemporalGranularityEnum temporalGranularity = timeSharePowerQueryEntity.getTemporalGranularity();

        Page<TimeSharePowerConsumptionEntity> page = new Page<>(request, response);
        switch (temporalGranularity) {
            case VD_Day:
                EmsTimeSharePowerConsumption dayWhere = new EmsTimeSharePowerConsumption();
                dayWhere.setDeviceId(timeSharePowerQueryEntity.getDeviceId());
                dayWhere.setDataType(temporalGranularity.getCode());
                dayWhere.setDataDateStart(DateUtils.parseDate(timeSharePowerQueryEntity.getQryStartTime()));
                dayWhere.setDataDateEnd(DateUtils.parseDate(timeSharePowerQueryEntity.getQryEndTime()));
                dayWhere.setPage(page);
                page.setList(this.findTimeShareList(dayWhere));
                break;
            case VD_Month:
            case VD_Year:
                EmsTimeSharePowerConsumptionStatistics monthWhere = new EmsTimeSharePowerConsumptionStatistics();
                monthWhere.setDeviceId(timeSharePowerQueryEntity.getDeviceId());
                monthWhere.setDataType(temporalGranularity.getCode());
                monthWhere.setQryStartTime(timeSharePowerQueryEntity.getQryStartTime());
                monthWhere.setQryEndTime(timeSharePowerQueryEntity.getQryEndTime());
                monthWhere.setPage(page);
                page.setList(emsTimeSharePowerConsumptionStatisticsService.findTimeShareList(monthWhere));
                break;
        }
        return page;
    }

    private Page<TimeSharePowerConsumptionEntity> findAreaTimeSharePage(TimeSharePowerQueryEntity timeSharePowerQueryEntity, HttpServletRequest request, HttpServletResponse response) {
        TemporalGranularityEnum temporalGranularity = timeSharePowerQueryEntity.getTemporalGranularity();
        Page<TimeSharePowerConsumptionEntity> page = new Page<>(request, response);
        switch (temporalGranularity) {
            case VD_Day:
                EmsTimeShareAreaPowerConsumption dayWhere = new EmsTimeShareAreaPowerConsumption();
                dayWhere.setAreaCode(timeSharePowerQueryEntity.getAreaCode());
                dayWhere.setDataType(temporalGranularity.getCode());
                dayWhere.setDataDateStart(DateUtils.parseDate(timeSharePowerQueryEntity.getQryStartTime()));
                dayWhere.setDataDateEnd(DateUtils.parseDate(timeSharePowerQueryEntity.getQryEndTime()));
                dayWhere.setPage(page);
                page.setList(emsTimeShareAreaPowerConsumptionService.findTimeShareList(dayWhere));
                break;
            case VD_Month:
            case VD_Year:
                EmsTimeShareAreaPowerConsumptionStatistics monthWhere = new EmsTimeShareAreaPowerConsumptionStatistics();
                monthWhere.setAreaCode(timeSharePowerQueryEntity.getAreaCode());
                monthWhere.setDataType(temporalGranularity.getCode());
                monthWhere.setQryStartTime(timeSharePowerQueryEntity.getQryStartTime());
                monthWhere.setQryEndTime(timeSharePowerQueryEntity.getQryEndTime());
                monthWhere.setPage(page);
                page.setList(emsTimeShareAreaPowerConsumptionStatisticsService.findTimeShareList(monthWhere));
                break;
        }
        return page;
    }

    private List<TimeSharePowerConsumptionEntity> findTimeShareList(EmsTimeSharePowerConsumption entity) {
        return this.dao.findTimeShareList(entity);
    }

    public List<TimeSharePowerConsumptionEntity> findTimeShareList(TimeSharePowerQueryEntity timeSharePowerQueryEntity) {
        EnergyGroupEnum energyGroup = timeSharePowerQueryEntity.getEnergyGroup();
        List<TimeSharePowerConsumptionEntity> rlt = null;
        switch (energyGroup) {
            case GROUP_Area:
                rlt = findAreaTimeShareList(timeSharePowerQueryEntity);
                break;
            case GROUP_Device:
                rlt = findDeviceTimeShareList(timeSharePowerQueryEntity);
                break;
        }
        return rlt;
    }

    private List<TimeSharePowerConsumptionEntity> findDeviceTimeShareList(TimeSharePowerQueryEntity timeSharePowerQueryEntity) {
        TemporalGranularityEnum temporalGranularity = timeSharePowerQueryEntity.getTemporalGranularity();

        List<TimeSharePowerConsumptionEntity> list = ListUtils.newArrayList();
        switch (temporalGranularity) {
            case VD_Day:
                EmsTimeSharePowerConsumption dayWhere = new EmsTimeSharePowerConsumption();
                dayWhere.setDeviceId(timeSharePowerQueryEntity.getDeviceId());
                dayWhere.setDataType(temporalGranularity.getCode());
                dayWhere.setDataDateStart(DateUtils.parseDate(timeSharePowerQueryEntity.getQryStartTime()));
                dayWhere.setDataDateEnd(DateUtils.parseDate(timeSharePowerQueryEntity.getQryEndTime()));
                list = this.findTimeShareList(dayWhere);
                break;
            case VD_Month:
            case VD_Year:
                EmsTimeSharePowerConsumptionStatistics monthWhere = new EmsTimeSharePowerConsumptionStatistics();
                monthWhere.setDeviceId(timeSharePowerQueryEntity.getDeviceId());
                monthWhere.setDataType(temporalGranularity.getCode());
                monthWhere.setQryStartTime(timeSharePowerQueryEntity.getQryStartTime());
                monthWhere.setQryEndTime(timeSharePowerQueryEntity.getQryEndTime());
                list = emsTimeSharePowerConsumptionStatisticsService.findTimeShareList(monthWhere);
                break;
        }
        return list;
    }

    private List<TimeSharePowerConsumptionEntity> findAreaTimeShareList(TimeSharePowerQueryEntity timeSharePowerQueryEntity) {
        TemporalGranularityEnum temporalGranularity = timeSharePowerQueryEntity.getTemporalGranularity();
        List<TimeSharePowerConsumptionEntity> list = ListUtils.newArrayList();
        switch (temporalGranularity) {
            case VD_Day:
                EmsTimeShareAreaPowerConsumption dayWhere = new EmsTimeShareAreaPowerConsumption();
                dayWhere.setAreaCode(timeSharePowerQueryEntity.getAreaCode());
                dayWhere.setDataType(temporalGranularity.getCode());
                dayWhere.setDataDateStart(DateUtils.parseDate(timeSharePowerQueryEntity.getQryStartTime()));
                dayWhere.setDataDateEnd(DateUtils.parseDate(timeSharePowerQueryEntity.getQryEndTime()));
                list = emsTimeShareAreaPowerConsumptionService.findTimeShareList(dayWhere);
                break;
            case VD_Month:
            case VD_Year:
                EmsTimeShareAreaPowerConsumptionStatistics monthWhere = new EmsTimeShareAreaPowerConsumptionStatistics();
                monthWhere.setAreaCode(timeSharePowerQueryEntity.getAreaCode());
                monthWhere.setDataType(temporalGranularity.getCode());
                monthWhere.setQryStartTime(timeSharePowerQueryEntity.getQryStartTime());
                monthWhere.setQryEndTime(timeSharePowerQueryEntity.getQryEndTime());
                list = emsTimeShareAreaPowerConsumptionStatisticsService.findTimeShareList(monthWhere);
                break;
        }
        return list;
    }
}