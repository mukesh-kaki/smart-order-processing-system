package com.mukesh.payment.mapper;

import com.mukesh.events.PaymentCompletedEvent;
import com.mukesh.events.PaymentFailedEvent;
import com.mukesh.events.PaymentRequestedEvent;
import com.mukesh.payment.entity.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring", imports = Instant.class)
public interface PaymentMapper {

    @Mapping(target = "paymentId", expression = "java(java.util.UUID.randomUUID())")

    @Mapping(source = "totalAmount", target="amount")

    @Mapping(target = "status", constant = "PENDING")

    @Mapping(target = "createdAt", expression = "java(Instant.now())")

    @Mapping(target = "updatedAt", expression = "java(Instant.now())")
    PaymentEntity toEntity(PaymentRequestedEvent event);

    @Mapping(
            target = "eventId",
            expression = "java(java.util.UUID.randomUUID())"
    )
    @Mapping(
            target = "completedAt",
            expression = "java(java.time.Instant.now())"
    )
    PaymentCompletedEvent toPaymentCompletedEvent(PaymentEntity payment);

    @Mapping(target="reason", source = "failureReason")
    @Mapping(
            target = "failedAt",
            expression = "java(java.time.Instant.now())"
    )
    PaymentFailedEvent toPaymentFailedEvent(PaymentEntity payment);
}
