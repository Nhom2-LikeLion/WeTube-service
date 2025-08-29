package com.wetube.wetube_service.Repository;

import com.wetube.wetube_service.dto.response.SubscribedChannelDto;
import com.wetube.wetube_service.entity.Channel.Channel;
import com.wetube.wetube_service.entity.Channel.Subscription;
import com.wetube.wetube_service.entity.CompositeKey.SubscriptionId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SubscriptionRepository extends CrudRepository<Subscription, SubscriptionId> {
    List<Subscription> findByIdSubscriberId(int subscriberId);

}
