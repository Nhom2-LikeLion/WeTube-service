package com.wetube.wetube_service.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.wetube.wetube_service.entity.SubPack;

public interface SubPackRepository extends CrudRepository<SubPack, UUID> {
    
}
