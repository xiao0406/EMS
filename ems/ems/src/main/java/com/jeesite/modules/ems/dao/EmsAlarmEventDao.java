package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsAlarmEvent;

import java.util.Date;
import java.util.List;

/**
 * 报警事件记录表DAO接口
 * @author 李鹏
 * @version 2023-07-22
 */
@MyBatisDao
public interface EmsAlarmEventDao extends CrudDao<EmsAlarmEvent> {

    List<EmsAlarmEvent> findListByMin(String deviceId,Date date);

    List<String> getloseDataBy15Min(Date date);
}