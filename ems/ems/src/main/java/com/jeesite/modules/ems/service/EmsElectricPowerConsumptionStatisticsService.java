package com.jeesite.modules.ems.service;

import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.dao.EmsElectricPowerConsumptionStatisticsDao;
import com.jeesite.modules.ems.entity.*;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.UserHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 电耗表数据统计表Service
 *
 * @author 李鹏
 * @version 2023-06-19
 */
@Service
@Transactional(readOnly = true)
public class EmsElectricPowerConsumptionStatisticsService extends CrudService<EmsElectricPowerConsumptionStatisticsDao, EmsElectricPowerConsumptionStatistics> {

    /**
     * 获取单条数据
     *
     * @param emsElectricPowerConsumptionStatistics
     * @return
     */
    @Override
    public EmsElectricPowerConsumptionStatistics get(EmsElectricPowerConsumptionStatistics emsElectricPowerConsumptionStatistics) {
        return super.get(emsElectricPowerConsumptionStatistics);
    }

    /**
     * 查询分页数据
     *
     * @param emsElectricPowerConsumptionStatistics 查询条件
     * @return
     */
    @Override
    public Page<EmsElectricPowerConsumptionStatistics> findPage(EmsElectricPowerConsumptionStatistics emsElectricPowerConsumptionStatistics) {
        return super.findPage(emsElectricPowerConsumptionStatistics);
    }

    /**
     * 查询列表数据
     *
     * @param emsElectricPowerConsumptionStatistics
     * @return
     */
    @Override
    public List<EmsElectricPowerConsumptionStatistics> findList(EmsElectricPowerConsumptionStatistics emsElectricPowerConsumptionStatistics) {
        return super.findList(emsElectricPowerConsumptionStatistics);
    }

    /**
     * 查询根据电表排序顺序的数据列表
     *
     * @param emsElectricPowerConsumptionStatistics
     * @return
     */
    public List<EmsElectricPowerConsumptionStatistics> findMeterSortList(EmsElectricPowerConsumptionStatistics emsElectricPowerConsumptionStatistics) {
        return this.dao.findMeterSortList(emsElectricPowerConsumptionStatistics);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param emsElectricPowerConsumptionStatistics
     */
    @Override
    @Transactional(readOnly = false)
    public void save(EmsElectricPowerConsumptionStatistics emsElectricPowerConsumptionStatistics) {
        super.save(emsElectricPowerConsumptionStatistics);
    }

    /**
     * 更新状态
     *
     * @param emsElectricPowerConsumptionStatistics
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(EmsElectricPowerConsumptionStatistics emsElectricPowerConsumptionStatistics) {
        super.updateStatus(emsElectricPowerConsumptionStatistics);
    }

    /**
     * 删除数据
     *
     * @param emsElectricPowerConsumptionStatistics
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(EmsElectricPowerConsumptionStatistics emsElectricPowerConsumptionStatistics) {
        super.delete(emsElectricPowerConsumptionStatistics);
    }

    public EmsElectricPowerConsumptionStatistics isStockedRec(EmsElectricPowerConsumptionStatistics emsElectricPowerConsumptionStatistics) {
        return this.dao.isStockedRec(emsElectricPowerConsumptionStatistics);
    }

    public List<EmsStatisticsEfficacyEntity> meterMonthEfficacy(EmsStatisticsReportEntity emsStatisticsReportEntity) {
        return this.dao.meterMonthEfficacy(emsStatisticsReportEntity);
    }

    public List<EmsStatisticsEfficacyEntity> meterYearEfficacy(EmsStatisticsReportEntity emsStatisticsReportEntity) {
        return this.dao.meterYearEfficacy(emsStatisticsReportEntity);
    }

    public Double getStageCumulativeConsumption(StageCumulativeQueryEntity entity) {
        if (StringUtils.isBlank(entity.getCompanyCode())){
            User user = UserHelper.getUser();
            Company company = EmsUserHelper.userCompany(true, "当前为租户管理员账号，非法业务操作");
            entity.setCompanyCode(company.getCompanyCode());
            entity.setCorpCode(user.getCorpCode());
        }
        return this.dao.getStageCumulativeConsumption(entity);
    }

    public List<EChartData> getStageConsumption2EChart(StageCumulativeQueryEntity entity) {
        if (StringUtils.isBlank(entity.getCompanyCode())){
            User user = UserHelper.getUser();
            Company company = EmsUserHelper.userCompany(true, "当前为租户管理员账号，非法业务操作");
            entity.setCompanyCode(company.getCompanyCode());
            entity.setCorpCode(user.getCorpCode());
        }
        return this.dao.getStageConsumption2EChart(entity);
    }

    public List<EChartData> consumptionRanking(EmsElectricPowerConsumptionStatistics entity) {
        if (StringUtils.isBlank(entity.getCompanyCode())){
            User user = UserHelper.getUser();
            Company company = EmsUserHelper.userCompany(true, "当前为租户管理员账号，非法业务操作");
            entity.setCompanyCode(company.getCompanyCode());
            entity.setCorpCode(user.getCorpCode());
        }
        return this.dao.consumptionRanking(entity);
    }

    public List<EmsElectricPowerConsumptionStatistics> activeEnergyEChart(EmsElectricPowerConsumptionStatistics params) {
        return this.dao.activeEnergyEChart(params);
    }

    public EmsElectricPowerAreaConsumptionStatistics getAreaStageCumulativeConsumption(EmsElectricPowerConsumptionStatistics params) {
        return this.dao.getAreaStageCumulativeConsumption(params);
    }

    public List<DeviceWorkEfficiencyEntity> workEfficiencyListData(DeviceWorkEfficiencyQryEntity params) {
        return this.dao.workEfficiencyListData(params);
    }
}