package com.wetube.wetube_service.repository;

import com.wetube.wetube_service.entity.channel.Subscription;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

import com.wetube.wetube_service.entity.compositekey.SubscriptionId;


public interface SubscriptionRepository extends CrudRepository<Subscription, SubscriptionId> {
    List<Subscription> findByIdSubscriberId(UUID subscriberId);
    List<Subscription> findByIdTierId(UUID subscriberId);

}
