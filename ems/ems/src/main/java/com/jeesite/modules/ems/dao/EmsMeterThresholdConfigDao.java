package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsMeterThresholdConfig;

/**
 * 电表阈值配置表DAO接口
 * @author 李鹏
 * @version 2023-07-22
 */
@MyBatisDao
public interface EmsMeterThresholdConfigDao extends CrudDao<EmsMeterThresholdConfig> {

    EmsMeterThresholdConfig getById(EmsMeterThresholdConfig emsMeterThresholdConfig);
}