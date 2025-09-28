package com.wetube.wetube_service.controller;

import com.wetube.wetube_service.dto.ShortVideoDto;
import com.wetube.wetube_service.entity.shorts.ShortsVideo;
import com.wetube.wetube_service.repository.ShortVideoRepository;
import com.wetube.wetube_service.service.shorts.ShortsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/shorts")
@RequiredArgsConstructor
public class ShortsVideoController {

    private final ShortsService shortsService;


    @PostMapping
    public ShortVideoDto create(@RequestBody ShortVideoDto dto) {
        return shortsService.create(dto);
    }

    @GetMapping("/channel/{channelId}")
    public List<ShortVideoDto> getByChannel(@PathVariable UUID channelId) {
        return shortsService.getByChannel(channelId);
    }

    @GetMapping("/{id}")
    public ShortVideoDto getById(@PathVariable UUID id) {
        return shortsService.getById(id);
    }

    @PutMapping("/{id}")
    public ShortVideoDto update(@PathVariable UUID id, @RequestBody ShortVideoDto dto) {
        return shortsService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        shortsService.delete(id);
    }

    @GetMapping("/random")
    public List<ShortVideoDto> getRandomShorts(@RequestParam(defaultValue = "5") int limit) {
        List<ShortVideoDto> A = shortsService.getRandomShorts(limit);
        return shortsService.getRandomShorts(limit);
    }
}
