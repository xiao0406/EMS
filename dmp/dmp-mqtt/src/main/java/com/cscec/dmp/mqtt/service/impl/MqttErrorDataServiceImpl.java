package com.cscec.dmp.mqtt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cscec.dmp.mqtt.entity.MqttErrorData;
import com.cscec.dmp.mqtt.entity.MqttProtocolData;
import com.cscec.dmp.mqtt.mapper.MqttErrorDataMapper;
import com.cscec.dmp.mqtt.mapper.MqttProtocolDataMapper;
import com.cscec.dmp.mqtt.service.MqttErrorDataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* @author duans
* @description 针对表【dmp_mqtt_protocol_data】的数据库操作Service实现
* @createDate 2023-07-04 11:20:59
*/
@Service
@Transactional
public class MqttErrorDataServiceImpl extends ServiceImpl<MqttErrorDataMapper, MqttErrorData>
    implements MqttErrorDataService {

}




