package com.wetube.wetube_service.service.video.impl;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import com.wetube.wetube_service.entity.AppUser;
import com.wetube.wetube_service.entity.playlist.Playlist;
import com.wetube.wetube_service.entity.playlist.PlaylistVideo;
import com.wetube.wetube_service.enumeration.ActiveStatus;
import com.wetube.wetube_service.enumeration.PlaylistType;
import com.wetube.wetube_service.repository.PlaylistRepository;
import com.wetube.wetube_service.repository.PlaylistVideoRepository;
import com.wetube.wetube_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.wetube.wetube_service.repository.video.TagRepository;
import com.wetube.wetube_service.repository.video.VideoRepository;
import com.wetube.wetube_service.repository.video.VideoTagRepository;
import com.wetube.wetube_service.dto.video.RecommendResponseDto;
import com.wetube.wetube_service.dto.video.RecommendVideoDto;
import com.wetube.wetube_service.dto.video.TagDto;
import com.wetube.wetube_service.dto.video.VideoDetailDto;
import com.wetube.wetube_service.dto.video.VideoDetailResponseDto;
import com.wetube.wetube_service.dto.video.VideoDto;
import com.wetube.wetube_service.entity.video.Video;
import com.wetube.wetube_service.entity.video.VideoTag;
import com.wetube.wetube_service.mapper.video.VideoMapper;
import com.wetube.wetube_service.entity.video.Tag;
import com.wetube.wetube_service.service.CloudinaryService;
import com.wetube.wetube_service.service.video.VideoService;

import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final TagRepository tagRepository;
    private final VideoTagRepository videoTagRepository;
    private final VideoMapper videoMapper;
    private final CloudinaryService cloudinaryService;

    private final PlaylistRepository playlistRepository;
    private final PlaylistVideoRepository playlistVideoRepository;
    private final UserRepository userRepository;

    private static final String ID_NOT_FOUND = "Video not found with id: ";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VideoDto createVideo(MultipartFile videoFile,
            MultipartFile thumbnailFile,
            VideoDto videoDto) throws Exception {

        if (videoFile == null || videoFile.isEmpty()) {
            throw new IllegalArgumentException("Video file is required");
        }

        String videoUrl = cloudinaryService.uploadVideo(videoFile);
        String thumbnailUrl = (thumbnailFile != null && !thumbnailFile.isEmpty())
                ? cloudinaryService.uploadThumbnail(thumbnailFile)
                : null;

        Video entity = videoMapper.toEntity(videoDto);
        entity.setVideoUrl(videoUrl);
        entity.setThumbnailUrl(thumbnailUrl);
        entity.setVideosStatus(ActiveStatus.ACTIVE);
        LocalDateTime now = LocalDateTime.now();
        if (entity.getCreatedAt() == null)
            entity.setCreatedAt(now);

        entity.setUpdatedAt(now);

        Video savedVideo = videoRepository.save(entity);
        videoRepository.flush();

        addVideoToUserUploadedPlaylist(savedVideo);

        String tagsAsString = videoDto.getTagsAsString();
        if (tagsAsString != null && !tagsAsString.isBlank()) {
            log.info("Adding tags to new video {}: {}", savedVideo.getId(), tagsAsString);
            return this.addTags(savedVideo.getId(), tagsAsString);
        }

        return videoMapper.toDto(savedVideo);
    }

    private void addVideoToUserUploadedPlaylist(Video video) {
        try {
            UUID userId = video.getUser().getId();

            Playlist uploadedPlaylist = playlistRepository
                    .findByUserIdAndPlaylistType(userId, PlaylistType.USER_UPLOADED)
                    .orElseGet(() -> {
                        log.info("USER_UPLOADED playlist not found for user {}. Creating new one.", userId);
                        AppUser user = userRepository.findById(userId)
                                .orElseThrow(
                                        () -> new IllegalArgumentException("User not found for creating playlist"));

                        Playlist newPlaylist = Playlist.builder()
                                .title("Uploaded videos")
                                .playlistType(PlaylistType.USER_UPLOADED)
                                .user(user)
                                .build();
                        return playlistRepository.save(newPlaylist);
                    });

            PlaylistVideo playlistVideoLink = PlaylistVideo.builder()
                    .playlist(uploadedPlaylist)
                    .video(video)
                    .build();

            playlistVideoRepository.save(playlistVideoLink);
            log.info("Successfully added video {} to USER_UPLOADED playlist for user {}", video.getId(), userId);

        } catch (Exception e) {
            log.error("Failed to add video {} to USER_UPLOADED playlist for user {}. Error: {}",
                    video.getId(), video.getUser().getId(), e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VideoDto addTags(UUID videoId, String hashtagText) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND + videoId));

        Set<String> names = parseHashtagText(hashtagText);

        for (String name : names) {
            Tag tag = tagRepository.findByNameIgnoreCase(name).orElse(null);
            if (tag == null) {
                tag = new Tag();
                tag.setName(name);
                tag.setCount(0);
                tag.setCreatedAt(LocalDateTime.now());
                tag = tagRepository.save(tag);
            }

            if (!videoTagRepository.existsByVideo_IdAndTag_Id(video.getId(), tag.getId())) {
                VideoTag vt = new VideoTag();
                vt.setVideo(video);
                vt.setTag(tag);
                videoTagRepository.save(vt);

                video.getVideoTags().add(vt);

                tag.setCount((tag.getCount() == null ? 0 : tag.getCount()) + 1);
                tagRepository.save(tag);
            }
        }

        Video dtoSource = videoRepository.findByIdWithTags(videoId)
                .orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND + videoId));
        return videoMapper.toDto(dtoSource);
    }

    @Override
    @Transactional(readOnly = true)
    public VideoDto getById(String id) {
        UUID uuid = UUID.fromString(id);
        Video video = videoRepository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND + id));
        return videoMapper.toDto(video);
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<VideoDto> getAllVideo() {
        java.util.List<Video> videos = videoRepository.findAll();
        return videoMapper.toDtoList(videos);
    }

    private Set<String> parseHashtagText(String hashtagText) {
        if (hashtagText == null || hashtagText.isBlank())
            return java.util.Collections.emptySet();
        String[] parts = hashtagText.split("\\s+");
        Set<String> out = new LinkedHashSet<>();
        for (String raw : parts) {
            if (raw == null)
                continue;
            String s = raw.trim();
            if (s.startsWith("#"))
                s = s.substring(1);
            s = s.trim().toLowerCase(Locale.ROOT);
            if (!s.isBlank())
                out.add(s);
        }
        return out;
    }

    @Override
    public List<VideoDto> searchByTitle(String title) {
        return videoRepository.findByTitleContainingIgnoreCase(title)
                .stream().map(videoMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VideoDto> searchByTitlePaging(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return videoRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(videoMapper::toDto);
    }

    @Override
    public Page<VideoDto> getAllVideosPaging(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return videoRepository.findAll(pageable).map(videoMapper::toDto);
    }

    @Override
    public VideoDetailResponseDto getDetailWithRecommend(UUID videoId) {
        var video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException(ID_NOT_FOUND + videoId));

        List<TagDto> tags = tagRepository.findByVideoTags_Video_Id(videoId)
                .stream().map(tag -> new TagDto(tag.getId(), tag.getName(), tag.getCreatedAt(), 0)).toList();
        
        var detail = new VideoDetailDto(
            video.getId(),
            video.getTitle(),
            video.getDescription(),
            video.getVideoUrl(),
            video.getCreatedAt().toLocalDate(),
            video.getTotalView(),
            video.getUser().getName(),
            video.getUser().getPicture()
        );

        var relatedVideos = videoRepository.findDistinctByVideoTags_Tag_NameInAndIdNot(
                        tags.stream().map(TagDto::getName).toList(),
                        videoId
                )
                .stream()
                .map(v -> new RecommendVideoDto(
                        v.getId(),
                        v.getTitle(),
                        v.getThumbnailUrl(),
                        Integer.valueOf(v.getTotalView()),
                        v.getCreatedAt().toLocalDate(),
                        v.getUser().getName(),
                        (long) v.getDuration(),
                        v.getUser().getPicture()
                )) 
                .toList();

                 var recommend = RecommendResponseDto.builder()
                .video(relatedVideos)
                .tags(tags)
                .build();

                return new VideoDetailResponseDto(detail, recommend);
    }

    @Override
    public List<VideoDto> getVideosByTag(String tagName) {
        return videoRepository.findByVideoTags_Tag_NameOrderByTotalViewDesc(tagName)
                .stream()
                .map(videoMapper::toDto)
                .toList();
    }
}
