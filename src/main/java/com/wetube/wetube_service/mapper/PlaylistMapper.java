package com.wetube.wetube_service.mapper;

import java.util.List;

import com.wetube.wetube_service.dto.response.playlist.PlaylistDetailDto;
import com.wetube.wetube_service.dto.response.playlist.PlaylistVideoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.wetube.wetube_service.dto.request.CreatePlaylistRequest;
import com.wetube.wetube_service.dto.request.PlaylistaddRequest;
import com.wetube.wetube_service.dto.response.playlist.UserPlaylistDto;
import com.wetube.wetube_service.entity.AppUser;
import com.wetube.wetube_service.entity.video.Video;
import com.wetube.wetube_service.entity.playlist.Playlist;
import com.wetube.wetube_service.entity.playlist.PlaylistVideo;

@Mapper(componentModel = "spring")
public interface PlaylistMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "playlistVideos", ignore = true)
    @Mapping(source = "dto.title", target = "title")
    @Mapping(source = "dto.type", target = "playlistType")
    @Mapping(source = "user", target = "user")
    Playlist toEntity(CreatePlaylistRequest dto, AppUser user);

    @Mapping(target = "playlistId", source = "id")
    @Mapping(target = "playlistTitle", source = "title")
    @Mapping(target = "playlistType", source = "playlistType")
    @Mapping(target = "totalVideos", expression = "java(playlist.getPlaylistVideos() != null ? playlist.getPlaylistVideos().size() : 0)")
    @Mapping(target = "createdAt", source = "createdAt")
    UserPlaylistDto toDto(Playlist playlist);

    List<UserPlaylistDto> toDtoList(List<Playlist> playlists);

    @Mapping(target = "videoId", source = "video.id")
    @Mapping(target = "videoTitle", source = "video.title")
    @Mapping(target = "videoUrl", source = "video.videoUrl")
    @Mapping(target = "thumbnailUrl", source = "video.thumbnailUrl")
    @Mapping(target = "historyDuration", source = "historyDuration")
    PlaylistVideoDto toPlaylistDto(PlaylistVideo pv);

    List<PlaylistVideoDto> toPlaylistDtoList(List<PlaylistVideo> playlistVideos);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "playlist", source = "playlist")
    @Mapping(target = "video", source = "video")
    @Mapping(target = "historyDuration", source = "dto.historyDuration")
    PlaylistVideo toPlaylistVideo(PlaylistaddRequest dto, Playlist playlist, Video video);

    @Mapping(target = "playlistId", source = "id")
    @Mapping(target = "playlistTitle", source = "title")
    @Mapping(target = "playlistType", source = "playlistType")
    @Mapping(target = "totalVideos", expression = "java(playlist.getPlaylistVideos() != null ? playlist.getPlaylistVideos().size() : 0)")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "videos", expression = "java(toPlaylistDtoList(playlist.getPlaylistVideos()))")
    PlaylistDetailDto toPlaylistDetailDto(Playlist playlist);

}