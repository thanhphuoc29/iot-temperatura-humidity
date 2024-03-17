package com.IOTProject.config;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.IOTProject.Service.DataService;
import com.IOTProject.model.Data;
import com.IOTProject.model.Sensor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;


@Configuration
public class MqttConfig {
	private static final String BROKER_URL = "tcp://localhost:1883";
	private static final String CLIENT_ID = "serverIn";
	private String username = "thanhphuoc";
	private String password = "123";
	
	@Autowired
    private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private DataService service;
	
	@Bean
	public DefaultMqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		MqttConnectOptions options = new MqttConnectOptions();
		options.setServerURIs(new String[] { BROKER_URL });
		options.setUserName(username);
		options.setPassword(password.toCharArray());
		options.setCleanSession(true);
//	        options.setKeepAliveInterval(10);
		factory.setConnectionOptions(options);
		return factory;
	}

	@Bean
	public MessageChannel mqttInputChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer mqttInbound() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(CLIENT_ID,
				mqttClientFactory(), "#");
		adapter.setOutputChannel(mqttInputChannel());
		return adapter;
	}
	
	public String creatJson(String data) {
		ObjectMapper objectMapper = new ObjectMapper();
		String newJson = "";
        try {
        	
            Data newData =  objectMapper.readValue(data, Data.class);
            Timestamp time = new Timestamp(System.currentTimeMillis()); 
            newData.setTimeStamp(time);
            if(newData.isValid()) {
            	service.updateData(newData);
            }
         // Chuyển đối tượng mới thành JSON
            newJson = objectMapper.writeValueAsString(newData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newJson;
	}
	
	@Bean
	@ServiceActivator(inputChannel = "mqttInputChannel")
	public MessageHandler handler() {
		return new MessageHandler() {
			@Override
			public void handleMessage(Message<?> message) throws MessagingException {
				 // Lấy ra topic từ header của message
	            String topicName = (String)message.getHeaders().get("mqtt_receivedTopic", String.class).toString();
				String mess = message.getPayload().toString() ;
				if("topic/sensor".equals(topicName)) {
					String payload = creatJson(mess) ;
					messagingTemplate.convertAndSend("/topic/mqtt-data",payload);
				} else if("topic/status-light".equals(topicName)) {
					ObjectMapper objectMapper = new ObjectMapper();
					try {
						JsonNode data = objectMapper.readTree(mess);
						if(data.get("active") != null && data.get("active").asBoolean()) {
							messagingTemplate.convertAndSend("/topic/light-control",mess);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println("Received message from "+topicName +": " + mess);
			}
		};
	}
	
	

    // Kênh đầu ra để gửi dữ liệu MQTT
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    // Cấu hình MqttPahoMessageHandler để gửi dữ liệu đến MQTT Broker
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(CLIENT_ID+"light", mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("topic/status-light"); // Thay bằng chủ đề bạn muốn gửi đến
        return messageHandler;
    }
}
