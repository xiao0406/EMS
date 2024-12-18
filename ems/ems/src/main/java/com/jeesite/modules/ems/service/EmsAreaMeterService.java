package com.jeesite.modules.ems.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.entity.EmsAreaMeter;
import com.jeesite.modules.ems.dao.EmsAreaMeterDao;

/**
 * 区域电表绑定Service
 * @author 李鹏
 * @version 2023-06-13
 */
@Service
@Transactional(readOnly=true)
public class EmsAreaMeterService extends CrudService<EmsAreaMeterDao, EmsAreaMeter> {
	
	/**
	 * 获取单条数据
	 * @param emsAreaMeter
	 * @return
	 */
	@Override
	public EmsAreaMeter get(EmsAreaMeter emsAreaMeter) {
		return super.get(emsAreaMeter);
	}
	
	/**
	 * 查询分页数据
	 * @param emsAreaMeter 查询条件
	 * @return
	 */
	@Override
	public Page<EmsAreaMeter> findPage(EmsAreaMeter emsAreaMeter) {
		return super.findPage(emsAreaMeter);
	}
	
	/**
	 * 查询列表数据
	 * @param emsAreaMeter
	 * @return
	 */
	@Override
	public List<EmsAreaMeter> findList(EmsAreaMeter emsAreaMeter) {
		return super.findList(emsAreaMeter);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param emsAreaMeter
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(EmsAreaMeter emsAreaMeter) {
		super.save(emsAreaMeter);
	}
	
	/**
	 * 更新状态
	 * @param emsAreaMeter
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(EmsAreaMeter emsAreaMeter) {
		super.updateStatus(emsAreaMeter);
	}
	
	/**
	 * 删除数据
	 * @param emsAreaMeter
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(EmsAreaMeter emsAreaMeter) {
		super.delete(emsAreaMeter);
	}

	public void deleteOrm(EmsAreaMeter emsAreaMeter) {
		this.dao.deleteOrm(emsAreaMeter);
	}
}