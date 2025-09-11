package com.wetube.wetube_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wetube.wetube_service.entity.Tag;

import jakarta.transaction.Transactional;

public interface TagRepository extends JpaRepository<Tag, UUID> {
    Optional<Tag> findByNameIgnoreCase(String name);

    @Modifying
    @Transactional
    @Query("UPDATE Tag t SET t.count = t.count + 1 WHERE t.id = :id")
    int incrementCount(@Param("id") UUID id);
    
}
