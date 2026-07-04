package com.mukesh.payment.gateway;

import com.mukesh.payment.entity.PaymentEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class FakePaymentGateway implements PaymentGateway {

    @Override
    public PaymentResult process(PaymentEntity entity) {

        log.info(
                "Processing payment {} for amount {}",
                entity.getPaymentId(),
                entity.getAmount()
        );

        return PaymentResult.success(UUID.randomUUID().toString());
    }
}
