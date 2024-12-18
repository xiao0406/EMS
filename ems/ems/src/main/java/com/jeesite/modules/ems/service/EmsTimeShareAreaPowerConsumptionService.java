package com.jeesite.modules.ems.service;

import java.util.List;

import com.jeesite.modules.ems.entity.TimeSharePowerConsumptionEntity;
import com.jeesite.modules.ems.entity.TimeShareStatisticsEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.entity.EmsTimeShareAreaPowerConsumption;
import com.jeesite.modules.ems.dao.EmsTimeShareAreaPowerConsumptionDao;

/**
 * 区域峰平谷电耗表数据基础表Service
 * @author 李鹏
 * @version 2023-07-25
 */
@Service
@Transactional(readOnly=true)
public class EmsTimeShareAreaPowerConsumptionService extends CrudService<EmsTimeShareAreaPowerConsumptionDao, EmsTimeShareAreaPowerConsumption> {
	
	/**
	 * 获取单条数据
	 * @param emsTimeShareAreaPowerConsumption
	 * @return
	 */
	@Override
	public EmsTimeShareAreaPowerConsumption get(EmsTimeShareAreaPowerConsumption emsTimeShareAreaPowerConsumption) {
		return super.get(emsTimeShareAreaPowerConsumption);
	}
	
	/**
	 * 查询分页数据
	 * @param emsTimeShareAreaPowerConsumption 查询条件
	 * @return
	 */
	@Override
	public Page<EmsTimeShareAreaPowerConsumption> findPage(EmsTimeShareAreaPowerConsumption emsTimeShareAreaPowerConsumption) {
		return super.findPage(emsTimeShareAreaPowerConsumption);
	}
	
	/**
	 * 查询列表数据
	 * @param emsTimeShareAreaPowerConsumption
	 * @return
	 */
	@Override
	public List<EmsTimeShareAreaPowerConsumption> findList(EmsTimeShareAreaPowerConsumption emsTimeShareAreaPowerConsumption) {
		return super.findList(emsTimeShareAreaPowerConsumption);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param emsTimeShareAreaPowerConsumption
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(EmsTimeShareAreaPowerConsumption emsTimeShareAreaPowerConsumption) {
		super.save(emsTimeShareAreaPowerConsumption);
	}
	
	/**
	 * 更新状态
	 * @param emsTimeShareAreaPowerConsumption
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(EmsTimeShareAreaPowerConsumption emsTimeShareAreaPowerConsumption) {
		super.updateStatus(emsTimeShareAreaPowerConsumption);
	}
	
	/**
	 * 删除数据
	 * @param emsTimeShareAreaPowerConsumption
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(EmsTimeShareAreaPowerConsumption emsTimeShareAreaPowerConsumption) {
		super.delete(emsTimeShareAreaPowerConsumption);
	}

	public EmsTimeShareAreaPowerConsumption isStockedRec(EmsTimeShareAreaPowerConsumption entity) {
		return this.dao.isStockedRec(entity);
	}

	public TimeShareStatisticsEntity timeShareStatistics(EmsTimeShareAreaPowerConsumption entity) {
		return this.dao.timeShareStatistics(entity);
	}

	public List<TimeSharePowerConsumptionEntity> findTimeShareList(EmsTimeShareAreaPowerConsumption entity) {
		return this.dao.findTimeShareList(entity);
	}
}