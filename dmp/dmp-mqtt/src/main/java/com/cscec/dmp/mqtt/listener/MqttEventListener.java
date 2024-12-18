package com.cscec.dmp.mqtt.listener;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttConnect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.integration.mqtt.event.MqttConnectionFailedEvent;
import org.springframework.integration.mqtt.event.MqttMessageDeliveredEvent;
import org.springframework.integration.mqtt.event.MqttMessageSentEvent;
import org.springframework.integration.mqtt.event.MqttSubscribedEvent;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@ConditionalOnProperty(value = "mqtt.enabled", havingValue = "true")
public class MqttEventListener {


    @EventListener(MqttConnectionFailedEvent.class)
    public void mqttConnectionFailedEvent(MqttConnectionFailedEvent event) {
        log.error("MQTT服务器连接失败: date={}, error={}", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"), event.getCause().getMessage());
    }

    @EventListener(MqttConnect.class)
    public void reSubscribed() {
    }

    @EventListener(MqttMessageSentEvent.class)
    public void mqttMessageSentEvent(MqttMessageSentEvent event) {
        log.info("发送信息: date={}, info={}", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"), event.toString());
    }

    @EventListener(MqttMessageDeliveredEvent.class)
    public void mqttMessageDeliveredEvent(MqttMessageDeliveredEvent event) {
        log.info("发送成功信息: date={}, info={}", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"), event.toString());
    }

    @EventListener(MqttSubscribedEvent.class)
    public void mqttSubscribedEvent(MqttSubscribedEvent event) {
        log.info("订阅成功信息: date={}, info={}", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"), event.toString());
    }
}
