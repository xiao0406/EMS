package com.jeesite.modules.ems.service;

import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.dao.EmsElectricPowerAreaConsumptionDao;
import com.jeesite.modules.ems.entity.EmsElectricPowerAreaConsumption;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 区域电耗表数据基础表Service
 *
 * @author 李鹏
 * @version 2023-07-06
 */
@Service
@Transactional(readOnly = true)
public class EmsElectricPowerAreaConsumptionService extends CrudService<EmsElectricPowerAreaConsumptionDao, EmsElectricPowerAreaConsumption> {

    /**
     * 获取单条数据
     *
     * @param emsElectricPowerAreaConsumption
     * @return
     */
    @Override
    public EmsElectricPowerAreaConsumption get(EmsElectricPowerAreaConsumption emsElectricPowerAreaConsumption) {
        return super.get(emsElectricPowerAreaConsumption);
    }

    /**
     * 查询分页数据
     *
     * @param emsElectricPowerAreaConsumption 查询条件
     * @return
     */
    @Override
    public Page<EmsElectricPowerAreaConsumption> findPage(EmsElectricPowerAreaConsumption emsElectricPowerAreaConsumption) {
        return super.findPage(emsElectricPowerAreaConsumption);
    }

    /**
     * 查询列表数据
     *
     * @param emsElectricPowerAreaConsumption
     * @return
     */
    @Override
    public List<EmsElectricPowerAreaConsumption> findList(EmsElectricPowerAreaConsumption emsElectricPowerAreaConsumption) {
        return super.findList(emsElectricPowerAreaConsumption);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param emsElectricPowerAreaConsumption
     */
    @Override
    @Transactional(readOnly = false)
    public void save(EmsElectricPowerAreaConsumption emsElectricPowerAreaConsumption) {
        super.save(emsElectricPowerAreaConsumption);
    }

    /**
     * 更新状态
     *
     * @param emsElectricPowerAreaConsumption
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(EmsElectricPowerAreaConsumption emsElectricPowerAreaConsumption) {
        super.updateStatus(emsElectricPowerAreaConsumption);
    }

    /**
     * 删除数据
     *
     * @param emsElectricPowerAreaConsumption
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(EmsElectricPowerAreaConsumption emsElectricPowerAreaConsumption) {
        super.delete(emsElectricPowerAreaConsumption);
    }

    public EmsElectricPowerAreaConsumption isStockedRec(EmsElectricPowerAreaConsumption areaConsumption) {
        return this.dao.isStockedRec(areaConsumption);
    }

    public List<Date> getStockPendulum(String execDate, TemporalGranularityEnum temporalGranularityEnum) {
        EmsElectricPowerAreaConsumption params = new EmsElectricPowerAreaConsumption();
        params.setDataDate(DateUtils.parseDate(execDate));
        params.setDataType(temporalGranularityEnum.getCode());
        return this.dao.getStockPendulum(params);
    }
}