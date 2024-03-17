package com.IOTProject.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.IOTProject.Service.DataService;
import com.IOTProject.model.Sensor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@CrossOrigin
public class SocketController {
//	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private DataService service;
	
	 @Autowired
		private MessageChannel mqttOutboundChannel;

		public void sendDataToMqtt(String jsonData) {
			Message<String> message = MessageBuilder.withPayload(jsonData).build();
			try {
				mqttOutboundChannel.send(message);
			} catch (MessagingException e) {
				// Xử lý lỗi gửi dữ liệu
			}
		}

	@MessageMapping("/light-state")
    public void handleLightState(@Payload String data, 
                                 SimpMessageHeaderAccessor headerAccessor) {
		ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode sensorDataNode = objectMapper.readTree(data);
    
            int i = 0;
            Sensor sensor = new Sensor();
            sensor.setId(sensorDataNode.get("sensorId").asInt());
            sensor.setOn(sensorDataNode.get("isOn").asBoolean());
            sensor.nameUser = sensorDataNode.get("user").asText();
            service.updateStatusSensor(sensor);
            
            Map<String, Object> dataSendMqtt = new HashMap<>();
            dataSendMqtt.put("light", (Object)sensor.getId());
            dataSendMqtt.put("status", (Object)sensor.isOn);
            String jsonString = objectMapper.writeValueAsString(dataSendMqtt);
            System.out.println(jsonString);
            sendDataToMqtt(jsonString);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
