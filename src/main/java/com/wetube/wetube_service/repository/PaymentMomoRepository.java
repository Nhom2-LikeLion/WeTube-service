package com.wetube.wetube_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.wetube.wetube_service.entity.PaymentMomo;

public interface PaymentMomoRepository extends CrudRepository<PaymentMomo, UUID> {
    Optional<PaymentMomo> findByOrderId(String orderId); 
} 
    

