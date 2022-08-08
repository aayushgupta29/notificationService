package com.meesho.notificationservice.service.kafka;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.meesho.notificationservice.constants.Constants.TOPIC;
@Service
public class Producer {
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    @Autowired
    private KafkaTemplate<String, Integer> kafkaTemplate;

    public void sendMessage(int messageId) {
        logger.info(String.format("#### -> Producing message -> %s", messageId));
        this.kafkaTemplate.send( TOPIC, messageId);
    }
}
