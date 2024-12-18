package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsTimeShareOfficeConsumption;
import com.jeesite.modules.ems.entity.EmsTimeSharePowerConsumption;

import java.util.List;

/**
 * 峰平谷部门电耗表数据基础表DAO接口
 * @author 李鹏
 * @version 2023-06-28
 */
@MyBatisDao
public interface EmsTimeShareOfficeConsumptionDao extends CrudDao<EmsTimeShareOfficeConsumption> {

    EmsTimeShareOfficeConsumption isStockedRec(EmsTimeShareOfficeConsumption build);

    List<EmsTimeShareOfficeConsumption> findOfficeList(EmsTimeShareOfficeConsumption emsTimeShareOfficeConsumption);
}