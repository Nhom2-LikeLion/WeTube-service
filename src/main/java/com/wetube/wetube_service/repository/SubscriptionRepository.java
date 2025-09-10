package com.wetube.wetube_service.repository;

import com.wetube.wetube_service.entity.channel.Subscription;
import com.wetube.wetube_service.entity.CompositeKey.SubscriptionId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.wetube.wetube_service.entity.channel.Subscription;
import com.wetube.wetube_service.entity.compositeKey.SubscriptionId;

public interface SubscriptionRepository extends CrudRepository<Subscription, SubscriptionId> {
    List<Subscription> findByIdSubscriberId(UUID subscriberId);
    List<Subscription> findByIdTierId(UUID subscriberId);

}
