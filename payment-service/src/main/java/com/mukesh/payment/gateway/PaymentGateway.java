package com.mukesh.payment.gateway;

import com.mukesh.payment.entity.PaymentEntity;

public interface PaymentGateway {

    PaymentResult process(PaymentEntity entity);
}
