package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsTerminal;

import java.util.List;

/**
 * 终端表DAO接口
 * @author 李鹏
 * @version 2023-06-14
 */
@MyBatisDao
public interface EmsTerminalDao extends CrudDao<EmsTerminal> {

    void deleteBatch(EmsTerminal emsTerminal);

    EmsTerminal findByCode(String terminalCode);

    List<String> findCodes(EmsTerminal emsTerminal);

    EmsTerminal getById(EmsTerminal emsTerminal);
}