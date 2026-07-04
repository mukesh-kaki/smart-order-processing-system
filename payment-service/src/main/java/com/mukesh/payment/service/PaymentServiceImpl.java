package com.mukesh.payment.service;

import com.mukesh.commonoutbox.entity.AggregateType;
import com.mukesh.commonoutbox.entity.OutboxEntity;
import com.mukesh.commonoutbox.factory.OutboxFactory;
import com.mukesh.commonoutbox.service.OutboxService;
import com.mukesh.events.PaymentCompletedEvent;
import com.mukesh.events.PaymentFailedEvent;
import com.mukesh.events.PaymentRequestedEvent;
import com.mukesh.payment.entity.PaymentEntity;
import com.mukesh.payment.entity.PaymentStatus;
import com.mukesh.payment.gateway.FakePaymentGateway;
import com.mukesh.payment.gateway.PaymentGateway;
import com.mukesh.payment.gateway.PaymentResult;
import com.mukesh.payment.mapper.PaymentMapper;
import com.mukesh.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{


    private final PaymentRepository paymentRepository;
    private final PaymentGateway paymentGateway;
    private final PaymentMapper paymentMapper;
    private final OutboxFactory outboxFactory;
    private  final OutboxService outboxService;

    @Transactional
    @Override
    public void processPayment(PaymentRequestedEvent event){

        Optional<PaymentEntity> existingPayment =paymentRepository.findByOrderId(event.orderId());

        if(existingPayment.isPresent()){
            log.info(
                    "Payment already exists for Order {}",
                    event.orderId()
            );
            return;
        }

        PaymentEntity payment=paymentMapper.toEntity(event);
        paymentRepository.save(payment);

        PaymentResult result=PaymentGateway.process(payment);

        if(result. success()){
            payment.setStatus(PaymentStatus.SUCCESS);
        }else{
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason(result.reason());
        }

        payment.setUpdatedAt(Instant.now());

        paymentRepository.save(payment);

        if(payment.getStatus()==PaymentStatus.SUCCESS) {
            PaymentCompletedEvent paymentCompletedEvent = paymentMapper.toPaymentCompletedEvent(payment);

            OutboxEntity outboxEntity = outboxFactory.create(payment.getPaymentId(),
                    AggregateType.PAYMENT,
                    paymentCompletedEvent);

            OutboxService.save(outboxEntity);
        }else{
            PaymentFailedEvent paymentFailedEvent=paymentMapper.toPaymentFailedEvent(payment);
            OutboxEntity outboxEntity=outboxFactory.create(payment.getPaymentId(), AggregateType.PAYMENT, paymentFailedEvent);
            outboxService.save(outboxEntity);
        }



    }
}
