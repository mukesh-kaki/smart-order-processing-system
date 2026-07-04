package com.mukesh.payment.service;

import com.mukesh.commonoutbox.entity.AggregateType;
import com.mukesh.commonoutbox.service.OutboxPublisherService;
import com.mukesh.events.PaymentCompletedEvent;
import com.mukesh.events.PaymentFailedEvent;
import com.mukesh.events.PaymentRequestedEvent;
import com.mukesh.payment.entity.PaymentEntity;
import com.mukesh.payment.entity.PaymentStatus;
import com.mukesh.payment.gateway.PaymentGateway;
import com.mukesh.payment.gateway.PaymentResult;
import com.mukesh.payment.mapper.PaymentMapper;
import com.mukesh.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentGateway paymentGateway;
    private final PaymentMapper paymentMapper;
    private final OutboxPublisherService outboxPublisherService;

    @Transactional
    @Override
    public void processPayment(PaymentRequestedEvent event) {

        Optional<PaymentEntity> existingPayment =
                paymentRepository.findByOrderId(event.orderId());

        if (existingPayment.isPresent()) {
            log.info(
                    "Payment already exists for Order {}",
                    event.orderId()
            );
            return;
        }

        PaymentEntity payment = paymentMapper.toEntity(event);
        paymentRepository.save(payment);

        PaymentResult result = paymentGateway.process(payment);

        if (result.success()) {
            payment.setStatus(PaymentStatus.SUCCESS);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason(result.failureReason());
        }

        payment.setUpdatedAt(Instant.now());

        paymentRepository.save(payment);

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            PaymentCompletedEvent paymentCompletedEvent =
                    paymentMapper.toPaymentCompletedEvent(payment);

            outboxPublisherService.publish(
                    payment.getPaymentId(),
                    AggregateType.PAYMENT,
                    paymentCompletedEvent
            );
        } else {
            PaymentFailedEvent paymentFailedEvent =
                    paymentMapper.toPaymentFailedEvent(payment);

            outboxPublisherService.publish(
                    payment.getPaymentId(),
                    AggregateType.PAYMENT,
                    paymentFailedEvent
            );
        }
    }
}
