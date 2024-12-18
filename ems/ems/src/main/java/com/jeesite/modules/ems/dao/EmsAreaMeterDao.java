package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsAlarmRecordEntity;
import com.jeesite.modules.ems.entity.EmsAreaMeter;
import com.jeesite.modules.ems.entity.EmsMeterSafetyEntity;

import java.util.List;

/**
 * 区域电表绑定DAO接口
 * @author 李鹏
 * @version 2023-06-13
 */
@MyBatisDao
public interface EmsAreaMeterDao extends CrudDao<EmsAreaMeter> {

    void deleteOrm(EmsAreaMeter emsAreaMeter);

    List<EmsAlarmRecordEntity> meterSafetyRecListData(EmsMeterSafetyEntity emsMeterSafetyEntity);
}