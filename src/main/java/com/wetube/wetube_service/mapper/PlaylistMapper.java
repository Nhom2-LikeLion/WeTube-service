package com.wetube.wetube_service.mapper;

import com.wetube.wetube_service.dto.PlaylistDto;
import com.wetube.wetube_service.dto.VideoDto;
import com.wetube.wetube_service.entity.playlist.PlaylistVideo;

import java.util.List;
import java.util.stream.Collectors;

import com.wetube.wetube_service.entity.Video;

public class PlaylistMapper {

    // Chuyển PlaylistVideo -> PlaylistDto
    public static PlaylistDto toPlaylistDto(PlaylistVideo pv) {
        if (pv == null) return null;

        Video video = pv.getVideo();

        return PlaylistDto.builder()
                .playlistVideoId(pv.getId())
                .videoId(video.getId())
                .videoTitle(video.getTitle())
                .videoUrl(video.getVideoUrl())
                .thumbnailUrl(video.getThumbnailUrl())
                .historyDuration(pv.getHistoryDuration())
                .build();
    }

    // Chuyển List<PlaylistVideo> -> List<PlaylistDto>
    public static List<PlaylistDto> toPlaylistDtoList(List<PlaylistVideo> playlistVideos) {
        return playlistVideos.stream()
                .map(PlaylistMapper::toPlaylistDto)
                .collect(Collectors.toList());
    }

    // Chuyển Video -> VideoDto
    public static VideoDto toVideoDto(Video video) {
        if (video == null) return null;

        return VideoDto.builder()
                .id(video.getId())
                .title(video.getTitle())
                .videoUrl(video.getVideoUrl())
                .thumbnailUrl(video.getThumbnailUrl())
                .duration(video.getDuration())
                .build();
    }
}

