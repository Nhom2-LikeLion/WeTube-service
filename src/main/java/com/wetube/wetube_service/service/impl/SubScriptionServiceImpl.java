package com.wetube.wetube_service.service.impl;

import com.wetube.wetube_service.Repository.SubscriptionRepository;
import com.wetube.wetube_service.Repository.UserRepository;
import com.wetube.wetube_service.dto.request.SubscriptionRequest;
import com.wetube.wetube_service.dto.response.ChannelResponseDto;
import com.wetube.wetube_service.dto.response.SubscribedChannelDto;
import com.wetube.wetube_service.entity.AppUser;
import com.wetube.wetube_service.entity.Channel.Channel;
import com.wetube.wetube_service.entity.Channel.Subscription;
import com.wetube.wetube_service.mapper.ChannelMapper;
import com.wetube.wetube_service.mapper.SubscriptionMapper;
import com.wetube.wetube_service.service.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubScriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final ChannelMapper channelMapper;

    @Override
    public List<SubscribedChannelDto> getSubscribedChannels(UUID userId) {
        Optional.ofNullable(userId)
                .orElseThrow(() -> new IllegalArgumentException("UserID is empty"));

        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Subscription> subscriptions = Optional.ofNullable(subscriptionRepository.findByIdSubscriberId(userId))
                .orElseGet(Collections::emptyList);

        return subscriptions.stream()
                .sorted((s1, s2) -> s2.getCreatedAt().compareTo(s1.getCreatedAt())) // gần đây nhất lên đầu
                .map(Subscription::getChannel)
                .distinct() // loại trùng channel
                .map(channelMapper::toSubChannelDto)
                .collect(Collectors.toList());
    }

    @Override
    public void subscribe(SubscriptionRequest req) {

    }

    @Override
    public void unsubscribe(UUID subscriptionId) {

    }
}
