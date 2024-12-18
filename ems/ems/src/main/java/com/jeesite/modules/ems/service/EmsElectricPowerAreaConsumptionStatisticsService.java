package com.jeesite.modules.ems.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.dao.EmsElectricPowerAreaConsumptionStatisticsDao;
import com.jeesite.modules.ems.entity.EmsElectricPowerAreaConsumptionStatistics;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 区域电耗表数据统计表Service
 *
 * @author 李鹏
 * @version 2023-07-06
 */
@Service
@Transactional(readOnly = true)
public class EmsElectricPowerAreaConsumptionStatisticsService extends CrudService<EmsElectricPowerAreaConsumptionStatisticsDao, EmsElectricPowerAreaConsumptionStatistics> {

    /**
     * 获取单条数据
     *
     * @param emsElectricPowerAreaConsumptionStatistics
     * @return
     */
    @Override
    public EmsElectricPowerAreaConsumptionStatistics get(EmsElectricPowerAreaConsumptionStatistics emsElectricPowerAreaConsumptionStatistics) {
        return super.get(emsElectricPowerAreaConsumptionStatistics);
    }

    /**
     * 查询分页数据
     *
     * @param emsElectricPowerAreaConsumptionStatistics 查询条件
     * @return
     */
    @Override
    public Page<EmsElectricPowerAreaConsumptionStatistics> findPage(EmsElectricPowerAreaConsumptionStatistics emsElectricPowerAreaConsumptionStatistics) {
        return super.findPage(emsElectricPowerAreaConsumptionStatistics);
    }

    /**
     * 查询列表数据
     *
     * @param emsElectricPowerAreaConsumptionStatistics
     * @return
     */
    @Override
    public List<EmsElectricPowerAreaConsumptionStatistics> findList(EmsElectricPowerAreaConsumptionStatistics emsElectricPowerAreaConsumptionStatistics) {
        return super.findList(emsElectricPowerAreaConsumptionStatistics);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param emsElectricPowerAreaConsumptionStatistics
     */
    @Override
    @Transactional(readOnly = false)
    public void save(EmsElectricPowerAreaConsumptionStatistics emsElectricPowerAreaConsumptionStatistics) {
        super.save(emsElectricPowerAreaConsumptionStatistics);
    }

    /**
     * 更新状态
     *
     * @param emsElectricPowerAreaConsumptionStatistics
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(EmsElectricPowerAreaConsumptionStatistics emsElectricPowerAreaConsumptionStatistics) {
        super.updateStatus(emsElectricPowerAreaConsumptionStatistics);
    }

    /**
     * 删除数据
     *
     * @param emsElectricPowerAreaConsumptionStatistics
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(EmsElectricPowerAreaConsumptionStatistics emsElectricPowerAreaConsumptionStatistics) {
        super.delete(emsElectricPowerAreaConsumptionStatistics);
    }

    public EmsElectricPowerAreaConsumptionStatistics isStockedRec(EmsElectricPowerAreaConsumptionStatistics areaConsumption) {
        return this.dao.isStockedRec(areaConsumption);
    }
}