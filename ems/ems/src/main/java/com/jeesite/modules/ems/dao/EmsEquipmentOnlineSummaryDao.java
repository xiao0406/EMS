package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsEquipmentOnlineSummary;

/**
 * 在线设备统计DAO接口
 * @author 范富华
 * @version 2024-05-29
 */
@MyBatisDao
public interface EmsEquipmentOnlineSummaryDao extends CrudDao<EmsEquipmentOnlineSummary> {
	
}