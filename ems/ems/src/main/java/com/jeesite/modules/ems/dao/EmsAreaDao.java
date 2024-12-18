package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.TreeDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsArea;

import java.util.List;

/**
 * 区域表DAO接口
 * @author 李鹏
 * @version 2023-06-12
 */
@MyBatisDao
public interface EmsAreaDao extends TreeDao<EmsArea> {

    List<EmsArea> findAllMarked(EmsArea emsArea);

    EmsArea getById(EmsArea emsArea);
}