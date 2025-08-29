package com.wetube.wetube_service.Repository;

import com.wetube.wetube_service.entity.AppUser;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface MemberTierRepository extends CrudRepository<AppUser, UUID> {
}
