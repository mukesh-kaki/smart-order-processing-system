package com.mukesh.payment.publisher;

import com.mukesh.commonoutbox.entity.AggregateType;
import com.mukesh.commonoutbox.service.OutboxPublisherService;
import com.mukesh.events.PaymentCompletedEvent;
import com.mukesh.events.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventPublisherImpl implements PaymentEventPublisher {

    private final OutboxPublisherService outboxPublisherService;

    @Override
    public void publishPaymentCompleted(
            UUID paymentId,
            PaymentCompletedEvent event) {

        outboxPublisherService.publish(
                paymentId,
                AggregateType.PAYMENT,
                event
        );

        log.info(
                "Published PaymentCompletedEvent for Payment {}",
                paymentId
        );
    }

    @Override
    public void publishPaymentFailed(
            UUID paymentId,
            PaymentFailedEvent event) {

        outboxPublisherService.publish(
                paymentId,
                AggregateType.PAYMENT,
                event
        );

        log.info(
                "Published PaymentFailedEvent for Payment {}",
                paymentId
        );
    }
}