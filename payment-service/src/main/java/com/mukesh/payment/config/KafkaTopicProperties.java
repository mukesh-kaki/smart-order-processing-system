package com.mukesh.payment.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix ="app.kafka.topics")
public class KafkaTopicProperties {

    private Map<String, String> mappings=new HashMap<>();

    public String getTopic (String eventType){
        String topic=mappings.get(eventType);
        if(topic==null){
            throw  new IllegalArgumentException("No Kafka Topic configured for events: "+eventType);
        }
        return topic;
    }
}
