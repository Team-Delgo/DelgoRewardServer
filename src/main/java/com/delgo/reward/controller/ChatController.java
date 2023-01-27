package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.kafka.KafkaProperties;
import com.delgo.reward.domain.chat.Message;
import com.delgo.reward.dto.chat.SendMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/kafka")
public class ChatController extends CommController {
    private final KafkaTemplate<String, Message> kafkaTemplate;

    @PostMapping(value = "/publish")
    public ResponseEntity<?> sendMessage(@RequestBody SendMessageDTO sendMessageDTO) {
        System.out.println("sender: " + sendMessageDTO.getSender());
        System.out.println("content: " + sendMessageDTO.getContent());
        Message message = Message.builder().sender(sendMessageDTO.getSender()).content(sendMessageDTO.getContent()).timestamp(LocalDateTime.now()).build();
        try {
            kafkaTemplate.send(KafkaProperties.KAFKA_TOPIC, message).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return SuccessReturn(message);
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/group")
    public Message broadcastGroupMessage(@Payload Message message) {
        return message;
    }

}