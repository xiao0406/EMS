package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsTerminalMeter;

/**
 * 终端电表绑定表DAO接口
 * @author 李鹏
 * @version 2023-06-14
 */
@MyBatisDao
public interface EmsTerminalMeterDao extends CrudDao<EmsTerminalMeter> {

    void deleteOrm(String terminalCode);
}