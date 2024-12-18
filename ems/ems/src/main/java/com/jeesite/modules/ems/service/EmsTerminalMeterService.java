package com.jeesite.modules.ems.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.entity.EmsTerminalMeter;
import com.jeesite.modules.ems.dao.EmsTerminalMeterDao;

/**
 * 终端电表绑定表Service
 * @author 李鹏
 * @version 2023-06-14
 */
@Service
@Transactional(readOnly=true)
public class EmsTerminalMeterService extends CrudService<EmsTerminalMeterDao, EmsTerminalMeter> {
	
	/**
	 * 获取单条数据
	 * @param emsTerminalMeter
	 * @return
	 */
	@Override
	public EmsTerminalMeter get(EmsTerminalMeter emsTerminalMeter) {
		return super.get(emsTerminalMeter);
	}
	
	/**
	 * 查询分页数据
	 * @param emsTerminalMeter 查询条件
	 * @return
	 */
	@Override
	public Page<EmsTerminalMeter> findPage(EmsTerminalMeter emsTerminalMeter) {
		return super.findPage(emsTerminalMeter);
	}
	
	/**
	 * 查询列表数据
	 * @param emsTerminalMeter
	 * @return
	 */
	@Override
	public List<EmsTerminalMeter> findList(EmsTerminalMeter emsTerminalMeter) {
		return super.findList(emsTerminalMeter);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param emsTerminalMeter
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(EmsTerminalMeter emsTerminalMeter) {
		super.save(emsTerminalMeter);
	}

	/**
	 * 删除映射关系
	 * @param terminalCode
	 */
	@Transactional(readOnly=false)
	public void deleteOrm(String terminalCode) {
		this.dao.deleteOrm(terminalCode);
	}

	@Transactional(readOnly=false)
	public void insertBatch(List<EmsTerminalMeter> emsTerminalMeters) {
		this.dao.insertBatch(emsTerminalMeters);
	}
	
	/**
	 * 更新状态
	 * @param emsTerminalMeter
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(EmsTerminalMeter emsTerminalMeter) {
		super.updateStatus(emsTerminalMeter);
	}
	
	/**
	 * 删除数据
	 * @param emsTerminalMeter
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(EmsTerminalMeter emsTerminalMeter) {
		super.delete(emsTerminalMeter);
	}
	
}