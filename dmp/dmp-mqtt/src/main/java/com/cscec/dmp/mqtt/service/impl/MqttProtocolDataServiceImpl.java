package com.cscec.dmp.mqtt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cecec.api.redis.RedisKeyUtil;
import com.cscec.dmp.mqtt.entity.MqttErrorData;
import com.cscec.dmp.mqtt.entity.MqttProtocolData;
import com.cscec.dmp.mqtt.mapper.MqttProtocolDataMapper;
import com.cscec.dmp.mqtt.service.MqttErrorDataService;
import com.cscec.dmp.mqtt.service.MqttProtocolDataService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
* @author duans
* @description 针对表【dmp_mqtt_protocol_data】的数据库操作Service实现
* @createDate 2023-07-04 11:20:59
*/
@Service
@Transactional
public class MqttProtocolDataServiceImpl extends ServiceImpl<MqttProtocolDataMapper, MqttProtocolData>
    implements MqttProtocolDataService {

    private static final String TIMESTAMP = "ts";
    private static final String METER_CODE = "i";
    private static final String METER_PARAM = "v";

    private String topic;
    private JSONObject payload;
    private String terminalCode;
    private LocalDateTime ts;

    private final StringRedisTemplate stringRedisTemplate;

    private final MqttErrorDataService mqttErrorDataService;

    public MqttProtocolDataServiceImpl(StringRedisTemplate stringRedisTemplate, MqttErrorDataService mqttErrorDataService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.mqttErrorDataService = mqttErrorDataService;
    }

    @Override
    public void handleMessage(String topic, String payload){
        this.topic = topic;
        this.payload = JSON.parseObject(payload);
        handleTopic();
    }

    @Override
    public void handleMessage(String topic, String terminalCode, String payload){
        this.topic = topic;
        this.payload = JSON.parseObject(payload);
        this.terminalCode = terminalCode;
        handlePayload();
    }

    public void handleTopic(){
        String[] topicArr = topic.split("/");
        terminalCode = topicArr[2];
        handlePayload();
    }

    public void handlePayload(){
        handleTimestamp();
         saveData();
    }

    public void handleTimestamp(){
        if(!this.payload.containsKey(TIMESTAMP)){
            return;
        }
        ts = LocalDateTime.ofEpochSecond(this.payload.getLongValue(TIMESTAMP),0, ZoneOffset.ofHours(8));
    }

    public void saveData(){
        for (String key : payload.keySet()) {
            if(!key.equals(TIMESTAMP)){
                JSONObject js = payload.getJSONObject(key);
                JSONObject params = js.getJSONObject(METER_PARAM);
                MqttProtocolData protocolData = new MqttProtocolData();
                protocolData.setTopic(topic);
                protocolData.setMeterCode(js.getString(METER_CODE));
                protocolData.setPayload(params.toString());
                protocolData.setTerminalCode(terminalCode);
                protocolData.setTs(ts);
                protocolData.setCreateDate(LocalDateTime.now());
                if(params.containsKey("ep")){
                     save(protocolData);
                     redisSave(terminalCode, "online");
                }else {
                    MqttErrorData errorData = BeanUtil.copyProperties(protocolData, MqttErrorData.class);
                    errorData.setPayload(this.payload.toString());
                    mqttErrorDataService.save(errorData);
                    redisSave(terminalCode, "error");
                }
            }
        }
    }

    public void redisSave(String terminalCode, String value){
        stringRedisTemplate.opsForValue().set(RedisKeyUtil.terminalStatus(terminalCode), value, Duration.ofMinutes(10));
    }
}




