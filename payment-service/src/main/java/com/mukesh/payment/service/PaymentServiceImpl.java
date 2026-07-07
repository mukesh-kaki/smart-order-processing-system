package com.mukesh.payment.service;

import com.mukesh.events.PaymentCompletedEvent;
import com.mukesh.events.PaymentFailedEvent;
import com.mukesh.events.PaymentRequestedEvent;
import com.mukesh.payment.entity.PaymentEntity;
import com.mukesh.payment.entity.PaymentStatus;
import com.mukesh.payment.gateway.PaymentGateway;
import com.mukesh.payment.gateway.PaymentResult;
import com.mukesh.payment.mapper.PaymentMapper;
import com.mukesh.payment.publisher.PaymentEventPublisher;
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
    private final PaymentEventPublisher paymentEventPublisher;

    @Override
    @Transactional
    public void processPayment(PaymentRequestedEvent event) {

        log.info(
                "Processing payment for Order {}",
                event.orderId()
        );

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

        log.info(
                "Payment record created for Order {}",
                event.orderId()
        );

        PaymentResult result = paymentGateway.process(payment);

        if (result.success()) {

            payment.setStatus(PaymentStatus.SUCCESS);

            log.info(
                    "Payment successful for Order {}",
                    event.orderId()
            );

        } else {

            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason(result.failureReason());

            log.warn(
                    "Payment failed for Order {}. Reason={}",
                    event.orderId(),
                    result.failureReason()
            );
        }

        payment.setUpdatedAt(Instant.now());

        paymentRepository.save(payment);

        publishPaymentEvent(payment);
    }

    /**
     * Publishes either PaymentCompletedEvent
     * or PaymentFailedEvent depending
     * on payment status.
     */
    private void publishPaymentEvent(PaymentEntity payment) {

        if (payment.getStatus() == PaymentStatus.SUCCESS) {

            PaymentCompletedEvent completedEvent =
                    paymentMapper.toPaymentCompletedEvent(payment);

            paymentEventPublisher.publishPaymentCompleted(
                    payment.getPaymentId(),
                    completedEvent
            );

            log.info(
                    "Published PaymentCompletedEvent for Payment {}",
                    payment.getPaymentId()
            );

            return;
        }

        PaymentFailedEvent failedEvent =
                paymentMapper.toPaymentFailedEvent(payment);

        paymentEventPublisher.publishPaymentFailed(
                payment.getPaymentId(),
                failedEvent
        );

        log.info(
                "Published PaymentFailedEvent for Payment {}",
                payment.getPaymentId()
        );
    }
}