package com.wetube.wetube_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wetube.wetube_service.entity.PremiumUser;

public interface PremiumUserRepository extends JpaRepository<PremiumUser, UUID> {

}
