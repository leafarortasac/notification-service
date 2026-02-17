package com.br.notification_service.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MqttConfig {

    @Value("${SPRING_MQTT_HOST:localhost}")
    private String mqttHost;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {

        var factory = new DefaultMqttPahoClientFactory();
        var options = new MqttConnectOptions();

        options.setServerURIs(new String[] { "tcp://" + mqttHost + ":1883" });
        options.setCleanSession(true);
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {

        var messageHandler = new MqttPahoMessageHandler("notification-service-client", mqttClientFactory());

        messageHandler.setAsync(true);
        messageHandler.setDefaultRetained(false);
        return messageHandler;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }
}
