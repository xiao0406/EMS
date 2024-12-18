package com.jeesite.modules.ems.service;

import java.util.List;

import com.jeesite.modules.ems.entity.EmsTimeShareAreaPowerConsumptionStatistics;
import com.jeesite.modules.ems.entity.TimeSharePowerConsumptionEntity;
import com.jeesite.modules.ems.entity.TimeShareStatisticsEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.entity.EmsTimeSharePowerConsumptionStatistics;
import com.jeesite.modules.ems.dao.EmsTimeSharePowerConsumptionStatisticsDao;

/**
 * 峰平谷电耗表数据统计表Service
 * @author 李鹏
 * @version 2023-06-20
 */
@Service
@Transactional(readOnly=true)
public class EmsTimeSharePowerConsumptionStatisticsService extends CrudService<EmsTimeSharePowerConsumptionStatisticsDao, EmsTimeSharePowerConsumptionStatistics> {
	
	/**
	 * 获取单条数据
	 * @param emsTimeSharePowerConsumptionStatistics
	 * @return
	 */
	@Override
	public EmsTimeSharePowerConsumptionStatistics get(EmsTimeSharePowerConsumptionStatistics emsTimeSharePowerConsumptionStatistics) {
		return super.get(emsTimeSharePowerConsumptionStatistics);
	}
	
	/**
	 * 查询分页数据
	 * @param emsTimeSharePowerConsumptionStatistics 查询条件
	 * @return
	 */
	@Override
	public Page<EmsTimeSharePowerConsumptionStatistics> findPage(EmsTimeSharePowerConsumptionStatistics emsTimeSharePowerConsumptionStatistics) {
		return super.findPage(emsTimeSharePowerConsumptionStatistics);
	}
	
	/**
	 * 查询列表数据
	 * @param emsTimeSharePowerConsumptionStatistics
	 * @return
	 */
	@Override
	public List<EmsTimeSharePowerConsumptionStatistics> findList(EmsTimeSharePowerConsumptionStatistics emsTimeSharePowerConsumptionStatistics) {
		return super.findList(emsTimeSharePowerConsumptionStatistics);
	}

	/**
	 * 查询根据电表排序顺序的数据列表
	 * @param emsTimeSharePowerConsumptionStatistics
	 * @return
	 */
	public List<EmsTimeSharePowerConsumptionStatistics> findMeterSortList(EmsTimeSharePowerConsumptionStatistics emsTimeSharePowerConsumptionStatistics) {
		return this.dao.findMeterSortList(emsTimeSharePowerConsumptionStatistics);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param emsTimeSharePowerConsumptionStatistics
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(EmsTimeSharePowerConsumptionStatistics emsTimeSharePowerConsumptionStatistics) {
		super.save(emsTimeSharePowerConsumptionStatistics);
	}
	
	/**
	 * 更新状态
	 * @param emsTimeSharePowerConsumptionStatistics
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(EmsTimeSharePowerConsumptionStatistics emsTimeSharePowerConsumptionStatistics) {
		super.updateStatus(emsTimeSharePowerConsumptionStatistics);
	}
	
	/**
	 * 删除数据
	 * @param emsTimeSharePowerConsumptionStatistics
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(EmsTimeSharePowerConsumptionStatistics emsTimeSharePowerConsumptionStatistics) {
		super.delete(emsTimeSharePowerConsumptionStatistics);
	}

	public EmsTimeSharePowerConsumptionStatistics isStockedRec(EmsTimeSharePowerConsumptionStatistics build) {
		return this.dao.isStockedRec(build);
	}

	public TimeShareStatisticsEntity timeShareStatistics(EmsTimeSharePowerConsumptionStatistics where) {
		return this.dao.timeShareStatistics(where);
	}

	public EmsTimeShareAreaPowerConsumptionStatistics getAreaStageCumulativeConsumption(EmsTimeSharePowerConsumptionStatistics entity) {
		return this.dao.getAreaStageCumulativeConsumption(entity);
	}

	public List<TimeSharePowerConsumptionEntity> findTimeShareList(EmsTimeSharePowerConsumptionStatistics entity) {
		return this.dao.findTimeShareList(entity);
	}
}