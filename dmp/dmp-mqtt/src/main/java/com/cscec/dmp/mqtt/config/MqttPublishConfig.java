package com.cscec.dmp.mqtt.config;

import cn.hutool.core.util.IdUtil;
import com.cscec.dmp.mqtt.base.MqttConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
@ConditionalOnProperty(value = "mqtt.enabled", havingValue = "true")
public class MqttPublishConfig {

    /**
     * 发消息通道
     */
    @Bean(name = MqttConstant.PUBLISH_CHANNEL)
    public MessageChannel publishChannel() {
        return new DirectChannel();
    }

    /**
     * 发布者-MQTT消息处理器（生产者）  将channel绑定到MqttClientFactory上
     * @return {@link org.springframework.messaging.MessageHandler}
     */
    @Bean
    @ServiceActivator(inputChannel = MqttConstant.PUBLISH_CHANNEL)
    public MessageHandler mqttOutbound(MqttPahoClientFactory mqttPahoClientFactory) {
        //clientId每个连接必须唯一,否则,两个相同的clientId相互挤掉线
        String clientId = MqttConstant.PUBLISH_PREFIX + IdUtil.getSnowflake().nextId();
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(clientId, mqttPahoClientFactory);
        //async如果为true，则调用方不会阻塞。而是在发送消息时等待传递确认。默认值为false（发送将阻塞，直到确认发送)
        handler.setAsync(true);
        handler.setAsyncEvents(true);
        handler.setDefaultQos(0);
        handler.setConverter(new DefaultPahoMessageConverter());
        return handler;
    }
}
