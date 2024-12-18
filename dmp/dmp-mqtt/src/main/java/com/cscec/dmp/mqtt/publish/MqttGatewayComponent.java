package com.cscec.dmp.mqtt.publish;

import com.cscec.dmp.mqtt.base.MqttConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "mqtt.enabled", havingValue = "true")
@MessagingGateway(defaultRequestChannel = MqttConstant.PUBLISH_CHANNEL)
public interface MqttGatewayComponent {
    /**
     * 发送 默认topic的消息机制
     *
     * @param payload
     */
    void sendToMqtt(String payload);

    /**
     * 发送消息 向mqtt指定topic发送消息
     *
     * @param topic
     * @param payload
     */
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, String payload);

    /**
     * 发送使用Gzip压缩后的字节数组
     * 推荐使用此方法！！发送消息时请先调用GzipUtil内的数据压缩方法发送字节数组
     *
     * @param topic   topic
     * @param payload 消息内容
     */
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, byte[] payload);

    /**
     * 发送消息至指定topic 能指定qos
     *
     * @param payload 消息内容
     * @param topic   topic
     * @param qos     值为 0 1 2
     */
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, String payload);
}
