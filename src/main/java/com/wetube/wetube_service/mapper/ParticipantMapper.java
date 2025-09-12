package com.wetube.wetube_service.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.util.JsonFormat;
import com.wetube.wetube_service.dto.response.livekit.ParticipantDto;
import com.wetube.wetube_service.dto.response.livekit.TrackDto;
import com.wetube.wetube_service.utils.ParticipantMapperHelper;
import livekit.LivekitModels;
import org.mapstruct.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = ParticipantMapperHelper.class)
public interface ParticipantMapper {

    @Mapping(target = "sid", source = "sid")
    @Mapping(target = "name", source = "identity")
    @Mapping(target = "metadata", source = "metadata", qualifiedByName = "stringToJson")
    @Mapping(target = "joinedAt", source = "joinedAt")
    @Mapping(target = "joinedAtMs", source = "joinedAtMs")
    @Mapping(target = "region", source = "region")
    @Mapping(target = "publisher", source = "isPublisher")
    @Mapping(target = "version", source = "version")
    @Mapping(target = "state", expression = "java(p.getState().name())")
    @Mapping(target = "kind", expression = "java(p.getKind().name())")
    @Mapping(target = "kindDetails", source = "kindDetailsList", qualifiedByName = "kindDetailsToList")
    @Mapping(target = "permission", source = "permission", qualifiedByName = "permissionToJson")
    @Mapping(target = "attributes", expression = "java(p.getAttributesMap())")
    @Mapping(target = "disconnectReason", expression = "java(p.getDisconnectReason().name())")
    @Mapping(target = "tracks", source = "tracksList", qualifiedByName = "mapTracks")
    @Mapping(target = "muted", expression = "java(p.getTracksList().stream().allMatch(livekit.LivekitModels.TrackInfo::getMuted))")
    ParticipantDto toDto(LivekitModels.ParticipantInfo p);

    List<ParticipantDto> toDtoList(List<LivekitModels.ParticipantInfo> list);
}
