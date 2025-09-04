package com.wetube.wetube_service.repository;

import com.wetube.wetube_service.dto.response.SubscribedChannelDto;
import com.wetube.wetube_service.entity.channel.Channel;
import com.wetube.wetube_service.entity.channel.Subscription;
import com.wetube.wetube_service.entity.CompositeKey.SubscriptionId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SubscriptionRepository extends CrudRepository<Subscription, SubscriptionId> {
    List<Subscription> findByIdSubscriberId(UUID subscriberId);
    List<Subscription> findByIdTierId(UUID subscriberId);

}
