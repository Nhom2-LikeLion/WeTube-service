package com.wetube.wetube_service.Repository;

import com.wetube.wetube_service.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByClerkId(String clerkId);
}
