package com.cscec.dmp.mqtt.subscribe;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.cscec.dmp.mqtt.entity.MqttErrorData;
import com.cscec.dmp.mqtt.service.MqttErrorDataService;
import com.cscec.dmp.mqtt.service.MqttProtocolDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 类说明
 * @author 李鹏
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "mqtt.enabled", havingValue = "true")
public class MqttSubscribeHandler implements MessageHandler {

    @Resource
    private MqttProtocolDataService mqttProtocolDataService;
    @Resource
    private MqttErrorDataService mqttErrorDataService;


    private String topic;
    private String payload;

    /**
     *
     *消息会在这进行对topic的处理
     * <p>重连后</p>
     * topic格式 CSCEC-DMP/组织编号/终端号
     * */
    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        try {
            topic = String.valueOf(message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC));
            payload = String.valueOf(message.getPayload());
            //CSCEC-DMP/# 和 JANTOUR/UP/#/pVariable 进行数据转换
            if(topic.startsWith("JANTOUR")){
                payload = jantourToCscec();
                //topic转换  JANTOUR/UP/{deviceID}/pVariable -> CSCEC-DMP/JS/{deviceID}
                String[] topicArr = topic.split("/");
                String terminalCode = topicArr[2];
                topic = "CSCEC-DMP/JS/" + terminalCode;
                log.info("数据转换成功,topic:{},payload:{}", topic, payload);
            }
            mqttProtocolDataService.handleMessage(topic, payload);
        } catch (Exception ex){
            MqttErrorData errorData = new MqttErrorData();
            errorData.setTopic(topic);
            errorData.setPayload(payload);
            errorData.setCreateDate(LocalDateTime.now());
            mqttErrorDataService.save(errorData);
        }
    }

    /**
     * cscec存储类型
     * {
     *     "1":{
     *         "v":{},
     *         "i":"810000715627"
     *     },
     *     "ts":1704187267
     * }
     */
    private String jantourToCscec() throws ParseException {
        JSONObject payloads = new JSONObject();
        //key:1
        JSONObject first = new JSONObject();
        payloads.put("1", first);
        JSONObject old = JSONObject.parse(payload);
        //转时间戳
        String datetime = String.valueOf(old.get("datatime"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(datetime);
        long ts = date.getTime()/1000;
        payloads.put("ts", ts);
        //设备id
        String deviceId = String.valueOf(old.get("deviceID"));
        //i:设备id
        first.put("i", deviceId);
        first.put("v", convert(old));
        return JSON.toJSONString(payloads);
    }

    /**
     * 处理 v:{}
     */
    private JSONObject convert(JSONObject old) {
        JSONObject v = new JSONObject();
        v.put("ep", old.remove("WT"));
        v.put("erp", old.remove("WF"));
        v.put("p", old.remove("PZ"));
        v.put("q", old.remove("QZ"));
        v.put("pf", old.remove("COSZ"));
        v.put("pfa", old.remove("COSA"));
        v.put("pfb", old.remove("COSB"));
        v.put("pfc", old.remove("COSC"));
        old.remove("datatime");
        old.remove("deviceID");
        //v:数据 key小写
        for (String key : old.keySet()) {
            Object value = old.get(key);
            v.put(key.toLowerCase(), value);
        }
        return v;
    }
}
