package com.jeesite.modules.ems.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.ems.entity.EmsElectricityTimeConf;
import com.jeesite.modules.sys.entity.Company;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.entity.EmsAlarmEvent;
import com.jeesite.modules.ems.dao.EmsAlarmEventDao;

/**
 * 报警事件记录表Service
 * @author 李鹏
 * @version 2023-07-22
 */
@Service
@Transactional(readOnly=true)
public class EmsAlarmEventService extends CrudService<EmsAlarmEventDao, EmsAlarmEvent> {
	
	/**
	 * 获取单条数据
	 * @param emsAlarmEvent
	 * @return
	 */
	@Override
	public EmsAlarmEvent get(EmsAlarmEvent emsAlarmEvent) {
		return super.get(emsAlarmEvent);
	}
	
	/**
	 * 查询分页数据
	 * @param emsAlarmEvent 查询条件
	 * @return
	 */
	@Override
	public Page<EmsAlarmEvent> findPage(EmsAlarmEvent emsAlarmEvent) {
		return super.findPage(emsAlarmEvent);
	}

	private void addCompanyFilter(EmsAlarmEvent entity){
		if (StringUtils.isBlank(entity.getCompanyCode())){
			Company company = EmsUserHelper.userCompany();
			entity.setCompanyCode(company.getCompanyCode());
		}
	}
	
	/**
	 * 查询列表数据
	 * @param emsAlarmEvent
	 * @return
	 */
	@Override
	public List<EmsAlarmEvent> findList(EmsAlarmEvent emsAlarmEvent) {
		addCompanyFilter(emsAlarmEvent);
		return super.findList(emsAlarmEvent);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param emsAlarmEvent
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(EmsAlarmEvent emsAlarmEvent) {
		super.save(emsAlarmEvent);
	}
	
	/**
	 * 更新状态
	 * @param emsAlarmEvent
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(EmsAlarmEvent emsAlarmEvent) {
		super.updateStatus(emsAlarmEvent);
	}
	
	/**
	 * 删除数据
	 * @param emsAlarmEvent
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(EmsAlarmEvent emsAlarmEvent) {
		super.delete(emsAlarmEvent);
	}

	/**
	 * 判断设备15分钟内有无生成报警记录
	 * @param deviceId
	 * @param date
	 * @return
	 */
	public List<EmsAlarmEvent> findListByMin(String deviceId,Date date) {
		List<EmsAlarmEvent> events = this.dao.findListByMin(deviceId,date);
		return events;
	}

	/**
	 * 获取15分钟内未统计的数据丢失的设备ID
	 * @return
	 */
	public List<String> getloseDataBy15Min(Date date) {
		return this.dao.getloseDataBy15Min(date);
	}
}