package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsMeterOffice;

/**
 * 机构电表用电占比配置表DAO接口
 * @author 李鹏
 * @version 2023-06-14
 */
@MyBatisDao
public interface EmsMeterOfficeDao extends CrudDao<EmsMeterOffice> {

    void deleteOrm(String meterCode);
}