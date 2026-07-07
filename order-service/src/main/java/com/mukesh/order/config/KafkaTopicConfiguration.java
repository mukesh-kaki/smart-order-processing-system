package com.mukesh.order.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfiguration {

    private static final int PARTITIONS = 3;
    private static final short REPLICATION_FACTOR = 1;

    private final KafkaTopicProperties kafkaTopicProperties;

/*    @Bean
    public NewTopic orderCreatedTopic(){
        return TopicBuilder
                .name(KafkaTopicProperties.getTopic("OrderCreatedEvent"))
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic inventoryReservedTopic() {

        return TopicBuilder
                .name(kafkaTopicProperties.getTopic("InventoryReservedEvent"))
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic paymentRequestedTopic() {

        return TopicBuilder
                .name(kafkaTopicProperties.getTopic("PaymentRequestedEvent"))
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic paymentCompletedTopic() {

        return TopicBuilder
                .name(kafkaTopicProperties.getTopic("PaymentCompletedEvent"))
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic paymentFailedTopic() {

        return TopicBuilder
                .name(kafkaTopicProperties.getTopic("PaymentFailedEvent"))
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic inventoryReleaseTopic() {

        return TopicBuilder
                .name(kafkaTopicProperties.getTopic("InventoryReleaseEvent"))
                .partitions(3)
                .replicas(1)
                .build();
    }
*/

    @Bean
    public KafkaAdmin.NewTopics kafkaTopics() {

        List<NewTopic> topics = new ArrayList<>();

        kafkaTopicProperties
                .getMappings()
                .values()
                .forEach(topicName -> {

                    // Main Topic
                    topics.add(createTopic(topicName));

                    // Dead Letter Topic
                    topics.add(createTopic(topicName + "-dlt"));
                });

        return new KafkaAdmin.NewTopics(
                topics.toArray(new NewTopic[0])
        );
    }

    private NewTopic createTopic(String topicName) {

        return TopicBuilder
                .name(topicName)
                .partitions(PARTITIONS)
                .replicas(REPLICATION_FACTOR)
                .build();
    }
}
