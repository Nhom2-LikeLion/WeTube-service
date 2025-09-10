package com.wetube.wetube_service.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wetube.wetube_service.dto.request.SubscriptionRequest;
import com.wetube.wetube_service.dto.request.UnsubscribeRequest;
import com.wetube.wetube_service.dto.response.SubscribedChannelResponseDto;
import com.wetube.wetube_service.entity.channel.Subscription;
import com.wetube.wetube_service.enumeration.SubscriptionType;
import com.wetube.wetube_service.exception.ResourceNotFoundException;
import com.wetube.wetube_service.mapper.ChannelMapper;
import com.wetube.wetube_service.mapper.SubscriptionMapper;
import com.wetube.wetube_service.repository.ChannelRepository;
import com.wetube.wetube_service.repository.MemberTierRepository;
import com.wetube.wetube_service.repository.SubscriptionRepository;
import com.wetube.wetube_service.repository.UserRepository;
import com.wetube.wetube_service.service.SubscriptionService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class SubScriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MemberTierRepository memberTierRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final ChannelMapper channelMapper;

    @Override
    public List<SubscribedChannelResponseDto> getSubscribedChannels(UUID userId) {
        Optional.ofNullable(userId)
                .orElseThrow(() -> new IllegalArgumentException("UserID is empty"));

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User","Id", userId.toString());
        }

        List<Subscription> subscriptions = Optional.ofNullable(subscriptionRepository.findByIdSubscriberId(userId))
                .orElseGet(Collections::emptyList);

//        return subscriptions.stream()
//                .sorted((s1, s2) -> s2.getCreatedAt().compareTo(s1.getCreatedAt()))
//                .map(Subscription::getChannel)
//                .distinct() // loại trùng channel
//                .map(channelMapper::toSubChannelDto)
//                .toList();

                return subscriptions.stream()
                .sorted((s1, s2) -> s2.getCreatedAt().compareTo(s1.getCreatedAt()))
                .map(channelMapper::toSubChannelDto)
                .toList();
    }

    @Override
    public void updateSubscriptionMode(UUID subscriberId, UUID channelId, UUID tierId, SubscriptionType newMode) {

    }

    @Override
    public void subscribe(SubscriptionRequest req) {

    }

    @Override
    public void unsubscribe(UnsubscribeRequest req) {

    }

}
