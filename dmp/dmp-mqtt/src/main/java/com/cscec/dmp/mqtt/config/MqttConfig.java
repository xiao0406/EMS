package com.cscec.dmp.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

/**
 * 类说明
 * @author 李鹏
 * @date 2023/4/6
 */
@Configuration
@ConditionalOnProperty(value = "mqtt.enabled", havingValue = "true")
public class MqttConfig {
    @Value("${mqtt.host}")
    private String host;

    @Value("${mqtt.username}")
    private String userName;

    @Value("${mqtt.password}")
    private String passWord;

    @Value("${mqtt.connectionTimeout}")
    private int connectionTimeout;

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        // MQTT的连接设置
        MqttConnectOptions options = new MqttConnectOptions();
        // 设置连接的用户名
        options.setUserName(userName);
        // 设置连接的密码
        options.setPassword(passWord.toCharArray());
        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，
        // 把配置里的 cleanSession 设为false，客户端掉线后 服务器端不会清除session，
        // 当重连后可以接收之前订阅主题的消息。当客户端上线后会接受到它离线的这段时间的消息
        options.setCleanSession(false);
        // 设置发布端地址,多个用逗号分隔, 如:tcp://10.0.0.1:1883,tcp://10.0.0.2:1883，类似设置微服务集群
        // 当第一个10.0.0.1连接上后,10.0.0.2不会在连,如果10.0.0.1挂掉后,重试连10.0.0.1几次失败后,会自动去连接10.0.0.2
        options.setServerURIs(new String[]{host});
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        options.setKeepAliveInterval(20);
        //该属性设置后能完成重连但是要另外实现订阅
        //经过对topic的测试，发现只注册到default时也能接收到消息 是因为对入站消息进行了处理（消息先入站再获取topic进行处理）
        options.setAutomaticReconnect(true);
        // 设置“遗嘱”消息的话题，若客户端与服务器之间的连接意外中断，服务器将发布客户端的“遗嘱”消息。
        //mqttConnectOptions.setWill(willTopic, willContent.getBytes(), 2, false);
        options.setMaxInflight(1000000);
        options.setConnectionTimeout(connectionTimeout);
        return options;
    }

    @Bean(value = "mqttPahoClientFactory")
    public MqttPahoClientFactory mqttPahoClientFactory(MqttConnectOptions mqttConnectOptions) {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(mqttConnectOptions);
        return factory;
    }
}
