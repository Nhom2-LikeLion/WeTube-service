package com.wetube.wetube_service.migration;

import com.wetube.wetube_service.entity.channel.Channel;
import com.wetube.wetube_service.repository.channel.ChannelRepository;
import com.wetube.wetube_service.service.channel.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component // Đánh dấu đây là một Spring Bean để được quản lý
@RequiredArgsConstructor
@Slf4j
public class DataMigrationRunner {

    private final ChannelRepository channelRepository;
    private final ChannelService channelService;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void runCategoryMigration() {
        log.info("Starting category migration for legacy channels...");

        List<Channel> channelsWithoutCategories = channelRepository.findChannelsWithoutCategories();

        if (channelsWithoutCategories.isEmpty()) {
            log.info("No legacy channels found needing category migration.");
            return;
        }

        log.info("Found {} channels to migrate.", channelsWithoutCategories.size());
        for (Channel channel : channelsWithoutCategories) {
            log.info("Initiating categories for channel: {}", channel.getId());
            // Tận dụng lại logic bạn đã viết trong ChannelService
            channelService.initiateCategories(channel);
        }
        log.info("Category migration completed successfully.");
    }
}