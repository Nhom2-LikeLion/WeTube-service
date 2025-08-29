package com.wetube.wetube_service.service.impl;

import com.wetube.wetube_service.Repository.SubscriptionRepository;
import com.wetube.wetube_service.dto.request.SubscriptionRequest;
import com.wetube.wetube_service.dto.response.ChannelResponseDto;
import com.wetube.wetube_service.dto.response.SubscribedChannelDto;
import com.wetube.wetube_service.entity.Channel.Channel;
import com.wetube.wetube_service.entity.Channel.Subscription;
import com.wetube.wetube_service.mapper.ChannelMapper;
import com.wetube.wetube_service.mapper.SubscriptionMapper;
import com.wetube.wetube_service.service.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubScriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final ChannelMapper channelMapper;

    @Override
    public List<SubscribedChannelDto> getSubscribedChannels(UUID userId) {
        List<SubscribedChannelDto> result = new ArrayList<>();
        List<Channel> channels = new ArrayList<>();
        List<Subscription> sub = (List<Subscription>) subscriptionRepository.findByIdSubscriberId(userId);
                for (Subscription subb : sub) {
            //result.add(subscriptionMapper.toSubscribedChannelDto(subb));
                    channels.add(subb.getChannel());
        }

            for(Channel chan : channels){
                result.add(channelMapper.toSubChannelDto(chan));
            }
//        List<Subscription> subs = subscriptionRepository.findBySubscriberId(userId);
//        List<Subscription> subs2 = subscriptionRepository.findBySubscriber_Id(userId);
//        List<Subscription> subs3 = subscriptionRepository.findBySubscriberIdFixed(userId);
//
//        List<Channel> channels = subscriptionRepository.findFullChannelsByUserId(userId);
//        for (Channel channel : channels) {
//            result.add(channelMapper.toSubChannelDto(channel));
//        }

        return result;
    }

    @Override
    public void subscribe(SubscriptionRequest req) {

    }

    @Override
    public void unsubscribe(UUID subscriptionId) {

    }
}
