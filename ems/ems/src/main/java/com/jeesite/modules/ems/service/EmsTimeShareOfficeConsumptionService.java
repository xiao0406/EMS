package com.jeesite.modules.ems.service;

import java.util.List;

import com.jeesite.modules.ems.entity.EmsTimeSharePowerConsumption;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.entity.EmsTimeShareOfficeConsumption;
import com.jeesite.modules.ems.dao.EmsTimeShareOfficeConsumptionDao;

/**
 * 峰平谷部门电耗表数据基础表Service
 * @author 李鹏
 * @version 2023-06-28
 */
@Service
@Transactional(readOnly=true)
public class EmsTimeShareOfficeConsumptionService extends CrudService<EmsTimeShareOfficeConsumptionDao, EmsTimeShareOfficeConsumption> {
	
	/**
	 * 获取单条数据
	 * @param emsTimeShareOfficeConsumption
	 * @return
	 */
	@Override
	public EmsTimeShareOfficeConsumption get(EmsTimeShareOfficeConsumption emsTimeShareOfficeConsumption) {
		return super.get(emsTimeShareOfficeConsumption);
	}
	
	/**
	 * 查询分页数据
	 * @param emsTimeShareOfficeConsumption 查询条件
	 * @return
	 */
	@Override
	public Page<EmsTimeShareOfficeConsumption> findPage(EmsTimeShareOfficeConsumption emsTimeShareOfficeConsumption) {
		return super.findPage(emsTimeShareOfficeConsumption);
	}
	
	/**
	 * 查询列表数据
	 * @param emsTimeShareOfficeConsumption
	 * @return
	 */
	@Override
	public List<EmsTimeShareOfficeConsumption> findList(EmsTimeShareOfficeConsumption emsTimeShareOfficeConsumption) {
		return super.findList(emsTimeShareOfficeConsumption);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param emsTimeShareOfficeConsumption
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(EmsTimeShareOfficeConsumption emsTimeShareOfficeConsumption) {
		super.save(emsTimeShareOfficeConsumption);
	}
	
	/**
	 * 更新状态
	 * @param emsTimeShareOfficeConsumption
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(EmsTimeShareOfficeConsumption emsTimeShareOfficeConsumption) {
		super.updateStatus(emsTimeShareOfficeConsumption);
	}
	
	/**
	 * 删除数据
	 * @param emsTimeShareOfficeConsumption
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(EmsTimeShareOfficeConsumption emsTimeShareOfficeConsumption) {
		super.delete(emsTimeShareOfficeConsumption);
	}

	public EmsTimeShareOfficeConsumption isStockedRec(EmsTimeShareOfficeConsumption build) {
		return this.dao.isStockedRec(build);
	}

	public List<EmsTimeShareOfficeConsumption> findOfficeList(EmsTimeShareOfficeConsumption emsTimeShareOfficeConsumption) {
		return this.dao.findOfficeList(emsTimeShareOfficeConsumption);
	}
}