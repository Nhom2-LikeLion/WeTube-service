package com.wetube.wetube_service.service;

import com.wetube.wetube_service.dto.request.SubscriptionRequest;
import com.wetube.wetube_service.dto.response.ChannelResponseDto;
import com.wetube.wetube_service.dto.response.SubscribedChannelDto;
import com.wetube.wetube_service.entity.Channel.Channel;

import java.util.List;
import java.util.UUID;

public interface SubscriptionService {
    List<SubscribedChannelDto> getSubscribedChannels(UUID userId);

    void subscribe(SubscriptionRequest req);
    void unsubscribe(UUID subscriptionId);
}
