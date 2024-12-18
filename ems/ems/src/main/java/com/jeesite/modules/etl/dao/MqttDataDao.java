package com.jeesite.modules.etl.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.etl.entity.MqttData;

import java.util.List;

/**
 * dmp_mqtt_gd_2023DAO接口
 * @author 李鹏
 * @version 2023-06-06
 */
@MyBatisDao(dataSourceName="dmp")
public interface MqttDataDao extends CrudDao<MqttData> {

    MqttData getLatestMqttData(MqttData params);
}