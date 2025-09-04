package com.wetube.wetube_service.service;

import com.wetube.wetube_service.dto.request.SubscriptionRequest;
import com.wetube.wetube_service.dto.request.UnsubscribeRequest;
import com.wetube.wetube_service.dto.response.ChannelResponseDto;
import com.wetube.wetube_service.dto.response.SubscribedChannelDto;
import com.wetube.wetube_service.entity.Channel.Channel;
import com.wetube.wetube_service.enumeration.SubscriptionType;

import java.util.List;
import java.util.UUID;

public interface SubscriptionService {
    List<SubscribedChannelDto> getSubscribedChannels(UUID userId);
    void updateSubscriptionMode(UUID subscriberId, UUID channelId, UUID tierId, SubscriptionType newMode);
    void subscribe(SubscriptionRequest req);
    void unsubscribe(UnsubscribeRequest subscriptionId);
}
