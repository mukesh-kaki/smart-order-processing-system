package com.mukesh.payment.repository;

import com.mukesh.payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID>  {

    Optional<PaymentEntity> findByOrderId(UUID orderId);
}
