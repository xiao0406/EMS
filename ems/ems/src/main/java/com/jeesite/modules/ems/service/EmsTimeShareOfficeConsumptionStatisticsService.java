package com.jeesite.modules.ems.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.entity.EmsTimeShareOfficeConsumptionStatistics;
import com.jeesite.modules.ems.dao.EmsTimeShareOfficeConsumptionStatisticsDao;

/**
 * 峰平谷部门电耗表数据统计表Service
 * @author 李鹏
 * @version 2023-06-28
 */
@Service
@Transactional(readOnly=true)
public class EmsTimeShareOfficeConsumptionStatisticsService extends CrudService<EmsTimeShareOfficeConsumptionStatisticsDao, EmsTimeShareOfficeConsumptionStatistics> {
	
	/**
	 * 获取单条数据
	 * @param emsTimeShareOfficeConsumptionStatistics
	 * @return
	 */
	@Override
	public EmsTimeShareOfficeConsumptionStatistics get(EmsTimeShareOfficeConsumptionStatistics emsTimeShareOfficeConsumptionStatistics) {
		return super.get(emsTimeShareOfficeConsumptionStatistics);
	}
	
	/**
	 * 查询分页数据
	 * @param emsTimeShareOfficeConsumptionStatistics 查询条件
	 * @return
	 */
	@Override
	public Page<EmsTimeShareOfficeConsumptionStatistics> findPage(EmsTimeShareOfficeConsumptionStatistics emsTimeShareOfficeConsumptionStatistics) {
		return super.findPage(emsTimeShareOfficeConsumptionStatistics);
	}
	
	/**
	 * 查询列表数据
	 * @param emsTimeShareOfficeConsumptionStatistics
	 * @return
	 */
	@Override
	public List<EmsTimeShareOfficeConsumptionStatistics> findList(EmsTimeShareOfficeConsumptionStatistics emsTimeShareOfficeConsumptionStatistics) {
		return super.findList(emsTimeShareOfficeConsumptionStatistics);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param emsTimeShareOfficeConsumptionStatistics
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(EmsTimeShareOfficeConsumptionStatistics emsTimeShareOfficeConsumptionStatistics) {
		super.save(emsTimeShareOfficeConsumptionStatistics);
	}
	
	/**
	 * 更新状态
	 * @param emsTimeShareOfficeConsumptionStatistics
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(EmsTimeShareOfficeConsumptionStatistics emsTimeShareOfficeConsumptionStatistics) {
		super.updateStatus(emsTimeShareOfficeConsumptionStatistics);
	}
	
	/**
	 * 删除数据
	 * @param emsTimeShareOfficeConsumptionStatistics
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(EmsTimeShareOfficeConsumptionStatistics emsTimeShareOfficeConsumptionStatistics) {
		super.delete(emsTimeShareOfficeConsumptionStatistics);
	}

	public EmsTimeShareOfficeConsumptionStatistics isStockedRec(EmsTimeShareOfficeConsumptionStatistics build) {
		return this.dao.isStockedRec(build);
	}

	public List<EmsTimeShareOfficeConsumptionStatistics> findOfficeList(EmsTimeShareOfficeConsumptionStatistics emsTimeShareOfficeConsumptionStatistics) {
		return this.dao.findOfficeList(emsTimeShareOfficeConsumptionStatistics);
	}
}