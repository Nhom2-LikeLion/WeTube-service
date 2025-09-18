package com.wetube.wetube_service.service.notification.impl;

import com.wetube.wetube_service.dto.request.NotificationRequestDto;
import com.wetube.wetube_service.dto.response.NotificationResponseDto;
import com.wetube.wetube_service.entity.channel.Channel;
import com.wetube.wetube_service.entity.channel.MembershipTier;
import com.wetube.wetube_service.entity.channel.Subscription;
import com.wetube.wetube_service.entity.notification.Notification;
import com.wetube.wetube_service.entity.video.Video;
import com.wetube.wetube_service.mapper.NotificationMapper;
import com.wetube.wetube_service.repository.channel.ChannelRepository;
import com.wetube.wetube_service.repository.notification.NotificationRepository;
import com.wetube.wetube_service.repository.video.VideoRepository;
import com.wetube.wetube_service.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final ChannelRepository channelRepository;
    private final VideoRepository videoRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public List<NotificationResponseDto> getUserNotifications(UUID userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void markAsRead(UUID notificationId) {
        Notification noti = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        noti.setRead(true);
        notificationRepository.save(noti);
    }

    @Override
    @Transactional
    public void markAllAsRead(UUID userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        for (Notification n : notifications) {
            n.setRead(true);
        }
        notificationRepository.saveAll(notifications);
    }

    @Override
    @Transactional
    public void deleteNotification(UUID notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    @Override
    @Transactional
    public void createNewVideoNotification(UUID channelId, UUID videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found"));
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found"));

        List<MembershipTier> tiers = channel.getMembershipTiers();

        for (MembershipTier tier : tiers) {
            List<Subscription> subscriptions = tier.getSubscriptions();

            for (Subscription subscription : subscriptions) {
                UUID userId = subscription.getId().getSubscriber().getId();

                Notification notification = Notification.builder()
                        .userId(userId)
                        .channel(channel)
                        .video(video)
                        .type(Notification.NotificationType.NEW_VIDEO)
                        .message("Channel " + channel.getName()
                                + " vừa upload video mới: " + video.getTitle())
                        .isRead(false)
                        .build();

                notificationRepository.save(notification);
            }
        }
    }
}
