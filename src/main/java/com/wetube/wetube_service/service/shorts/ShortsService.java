package com.wetube.wetube_service.service.shorts;

import com.wetube.wetube_service.dto.ShortVideoDto;
import com.wetube.wetube_service.entity.channel.Channel;
import com.wetube.wetube_service.entity.shorts.ShortsVideo;
import com.wetube.wetube_service.mapper.ShortVideoMapper;
import com.wetube.wetube_service.repository.channel.ChannelRepository;
import com.wetube.wetube_service.repository.ShortVideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShortsService {
    private final ShortVideoRepository shortVideoRepository;
    private final ChannelRepository channelRepository;
    private final ShortVideoMapper shortVideoMapper;

    public ShortVideoDto create(ShortVideoDto dto) {
        ShortsVideo entity = shortVideoMapper.toEntity(dto);

        Channel channel = channelRepository.findById(dto.getChannelId())
                .orElseThrow(() -> new RuntimeException("Channel not found"));
        entity.setChannel(channel);

        ShortsVideo saved = shortVideoRepository.save(entity);
        return shortVideoMapper.toShortDto(saved);
    }

    public List<ShortVideoDto> getByChannel(UUID channelId) {
        return shortVideoRepository.findByChannelId(channelId).stream()
                .map(shortVideoMapper::toShortDto)
                .toList();
    }

    public ShortVideoDto getById(UUID id) {
        return shortVideoRepository.findById(id)
                .map(shortVideoMapper::toShortDto)
                .orElseThrow(() -> new RuntimeException("Short video not found"));
    }

    public ShortVideoDto update(UUID id, ShortVideoDto dto) {
        ShortsVideo video = shortVideoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Short video not found"));

        video.setTitle(dto.getTitle());
        video.setDescription(dto.getDescription());
        video.setVideoUrl(dto.getVideoUrl());
        video.setThumbnailUrl(dto.getThumbnailUrl());

        ShortsVideo updated = shortVideoRepository.save(video);
        return shortVideoMapper.toShortDto(updated);
    }

    public void delete(UUID id) {
        if (!shortVideoRepository.existsById(id)) {
            throw new RuntimeException("Short video not found");
        }
        shortVideoRepository.deleteById(id);
    }

    public List<ShortVideoDto> getRandomShorts(int limit) {
        return shortVideoRepository.findRandomShorts(limit).stream()
                .map(shortVideoMapper::toShortDto)
                .toList();
    }
}
