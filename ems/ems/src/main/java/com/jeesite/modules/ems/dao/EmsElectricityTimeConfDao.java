package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsElectricityTimeConf;

/**
 * 用电分时配置表DAO接口
 * @author 李鹏
 * @version 2023-06-15
 */
@MyBatisDao
public interface EmsElectricityTimeConfDao extends CrudDao<EmsElectricityTimeConf> {

    EmsElectricityTimeConf getCompanyElectricityTimeConf(EmsElectricityTimeConf where);

    EmsElectricityTimeConf getByCompanyCode(EmsElectricityTimeConf where);
}