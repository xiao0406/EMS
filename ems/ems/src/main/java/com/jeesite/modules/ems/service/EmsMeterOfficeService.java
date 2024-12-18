package com.jeesite.modules.ems.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.entity.EmsMeterOffice;
import com.jeesite.modules.ems.dao.EmsMeterOfficeDao;
import org.springframework.util.CollectionUtils;

/**
 * 机构电表用电占比配置表Service
 * @author 李鹏
 * @version 2023-06-14
 */
@Service
@Transactional(readOnly=true)
public class EmsMeterOfficeService extends CrudService<EmsMeterOfficeDao, EmsMeterOffice> {
	
	/**
	 * 获取单条数据
	 * @param emsMeterOffice
	 * @return
	 */
	@Override
	public EmsMeterOffice get(EmsMeterOffice emsMeterOffice) {
		return super.get(emsMeterOffice);
	}
	
	/**
	 * 查询分页数据
	 * @param emsMeterOffice 查询条件
	 * @return
	 */
	@Override
	public Page<EmsMeterOffice> findPage(EmsMeterOffice emsMeterOffice) {
		return super.findPage(emsMeterOffice);
	}
	
	/**
	 * 查询列表数据
	 * @param emsMeterOffice
	 * @return
	 */
	@Override
	public List<EmsMeterOffice> findList(EmsMeterOffice emsMeterOffice) {
		return super.findList(emsMeterOffice);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param emsMeterOffice
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(EmsMeterOffice emsMeterOffice) {
		super.save(emsMeterOffice);
	}
	
	/**
	 * 更新状态
	 * @param emsMeterOffice
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(EmsMeterOffice emsMeterOffice) {
		super.updateStatus(emsMeterOffice);
	}
	
	/**
	 * 删除数据
	 * @param emsMeterOffice
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(EmsMeterOffice emsMeterOffice) {
		super.delete(emsMeterOffice);
	}

	public void insertBatch(List<EmsMeterOffice> meterOfficeList) {
		if(CollectionUtils.isEmpty(meterOfficeList)){
			return;
		}
		this.dao.insertBatch(meterOfficeList);
	}

	public void deleteOrm(String meterCode) {
		this.dao.deleteOrm(meterCode);
	}
}