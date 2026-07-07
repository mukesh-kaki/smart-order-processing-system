package com.mukesh.order.config;

import com.fasterxml.jackson.databind.JsonSerializer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
//@EnableConfigurationProperties(KafkaTopicProperties.class)
public class KafkaProducerConfig {

    private final KafkaTopicProperties kafkaTopicProperties;

    @Bean
    public ProducerFactory<String, Object> producerFactory(){

        Map<String, Object> properties=new HashMap<>();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaTopicProperties.getBootstrapServers());

        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        properties.put(ProducerConfig.ACKS_CONFIG, "all"); //Kafka confirms the message only after all replicas receive it.
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true); //
        properties.put(ProducerConfig.RETRIES_CONFIG, 3); // If Kafka is temporarily unavailable producer retries 3 times
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 5); //Wait 5MS before sending , Kafka batches multiple events together.
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16_384); // Maximum batch size before kafka sends
        return  new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory){
        return new KafkaTemplate<>(producerFactory);
    }
}
