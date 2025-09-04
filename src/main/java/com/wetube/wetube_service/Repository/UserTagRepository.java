package com.wetube.wetube_service.Repository;


import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wetube.wetube_service.entity.UserTag;
import com.wetube.wetube_service.entity.UserTag.UserTagId;


public interface UserTagRepository extends JpaRepository<UserTag, UserTagId> {
        List<UserTag> findAllByUserIdOrderByPointDesc(UUID userId);
        List<UserTag> findTop5ByUserIdOrderByPointDesc(UUID userId);

}
