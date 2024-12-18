package com.jeesite.modules.ems.service;

import java.util.List;

import com.jeesite.modules.ems.entity.TimeSharePowerConsumptionEntity;
import com.jeesite.modules.ems.entity.TimeShareStatisticsEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.entity.EmsTimeShareAreaPowerConsumptionStatistics;
import com.jeesite.modules.ems.dao.EmsTimeShareAreaPowerConsumptionStatisticsDao;

/**
 * 区域峰平谷电耗表数据统计表Service
 * @author 李鹏
 * @version 2023-07-25
 */
@Service
@Transactional(readOnly=true)
public class EmsTimeShareAreaPowerConsumptionStatisticsService extends CrudService<EmsTimeShareAreaPowerConsumptionStatisticsDao, EmsTimeShareAreaPowerConsumptionStatistics> {
	
	/**
	 * 获取单条数据
	 * @param emsTimeShareAreaPowerConsumptionStatistics
	 * @return
	 */
	@Override
	public EmsTimeShareAreaPowerConsumptionStatistics get(EmsTimeShareAreaPowerConsumptionStatistics emsTimeShareAreaPowerConsumptionStatistics) {
		return super.get(emsTimeShareAreaPowerConsumptionStatistics);
	}
	
	/**
	 * 查询分页数据
	 * @param emsTimeShareAreaPowerConsumptionStatistics 查询条件
	 * @return
	 */
	@Override
	public Page<EmsTimeShareAreaPowerConsumptionStatistics> findPage(EmsTimeShareAreaPowerConsumptionStatistics emsTimeShareAreaPowerConsumptionStatistics) {
		return super.findPage(emsTimeShareAreaPowerConsumptionStatistics);
	}
	
	/**
	 * 查询列表数据
	 * @param emsTimeShareAreaPowerConsumptionStatistics
	 * @return
	 */
	@Override
	public List<EmsTimeShareAreaPowerConsumptionStatistics> findList(EmsTimeShareAreaPowerConsumptionStatistics emsTimeShareAreaPowerConsumptionStatistics) {
		return super.findList(emsTimeShareAreaPowerConsumptionStatistics);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param emsTimeShareAreaPowerConsumptionStatistics
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(EmsTimeShareAreaPowerConsumptionStatistics emsTimeShareAreaPowerConsumptionStatistics) {
		super.save(emsTimeShareAreaPowerConsumptionStatistics);
	}
	
	/**
	 * 更新状态
	 * @param emsTimeShareAreaPowerConsumptionStatistics
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(EmsTimeShareAreaPowerConsumptionStatistics emsTimeShareAreaPowerConsumptionStatistics) {
		super.updateStatus(emsTimeShareAreaPowerConsumptionStatistics);
	}
	
	/**
	 * 删除数据
	 * @param emsTimeShareAreaPowerConsumptionStatistics
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(EmsTimeShareAreaPowerConsumptionStatistics emsTimeShareAreaPowerConsumptionStatistics) {
		super.delete(emsTimeShareAreaPowerConsumptionStatistics);
	}

	public EmsTimeShareAreaPowerConsumptionStatistics isStockedRec(EmsTimeShareAreaPowerConsumptionStatistics entity) {
		return this.dao.isStockedRec(entity);
	}

	public TimeShareStatisticsEntity timeShareStatistics(EmsTimeShareAreaPowerConsumptionStatistics entity) {
		return this.dao.timeShareStatistics(entity);
	}

	public List<TimeSharePowerConsumptionEntity> findTimeShareList(EmsTimeShareAreaPowerConsumptionStatistics entity) {
		return this.dao.findTimeShareList(entity);
	}
}