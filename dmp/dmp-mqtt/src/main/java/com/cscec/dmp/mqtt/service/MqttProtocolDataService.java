package com.cscec.dmp.mqtt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cscec.dmp.mqtt.entity.MqttProtocolData;

/**
* @author duans
* @description 针对表【dmp_mqtt_protocol_data】的数据库操作Service
* @createDate 2023-07-04 11:20:59
*/
public interface MqttProtocolDataService extends IService<MqttProtocolData> {

    void handleMessage(String topic, String payload);

    void handleMessage(String topic, String terminalCode, String payload);
}
