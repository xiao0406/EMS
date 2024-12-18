package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsMeterYield;

/**
 * 电表产量表DAO接口
 * @author 李鹏
 * @version 2023-06-15
 */
@MyBatisDao
public interface EmsMeterYieldDao extends CrudDao<EmsMeterYield> {
	
}