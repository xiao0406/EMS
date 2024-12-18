package com.cscec.dmp.mqtt.config;

import cn.hutool.core.util.IdUtil;
import com.cscec.dmp.mqtt.base.MqttConstant;
import com.cscec.dmp.mqtt.subscribe.MqttSubscribeHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
@ConditionalOnProperty(value = "mqtt.enabled", havingValue = "true")
public class MqttSubscribeConfig {

    @Value("${mqtt.topic}")
    private String[] topic;

    @Value("${mqtt.completionTimeout}")
    private int completionTimeout;

    private final MqttSubscribeHandler subscribeHandler;

    public MqttSubscribeConfig(MqttSubscribeHandler subscribeHandler) {
        this.subscribeHandler = subscribeHandler;
    }

    /**
     * 接收消息通道
     */
    @Bean(name = MqttConstant.SUBSCRIBE_CHANNEL)
    public MessageChannel subscribeChannel() {
        return new DirectChannel();
    }

    /**
     * MQTT消息订阅绑定（消费者）
     */
    @Bean
    public MessageProducer channelInbound(MessageChannel subscribeChannel, MqttPahoClientFactory mqttClientFactory) {
        //clientId每个连接必须唯一,否则,两个相同的clientId相互挤掉线
        String clientId = MqttConstant.SUBSCRIBE_PREFIX + IdUtil.getSnowflake().nextId();
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId, mqttClientFactory, topic);
        adapter.setCompletionTimeout(completionTimeout);
        adapter.setQos(0);
        adapter.setOutputChannel(subscribeChannel);
        adapter.setConverter(new DefaultPahoMessageConverter());
        return adapter;
    }

    /**
     * 通过通道获取数据 订阅的数据
     */
    @Bean
    @ServiceActivator(inputChannel = MqttConstant.SUBSCRIBE_CHANNEL)
    public MessageHandler handler() {
        return subscribeHandler;
    }
}
