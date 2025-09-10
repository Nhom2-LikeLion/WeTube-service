package com.wetube.wetube_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wetube.wetube_service.dto.request.livekit.*;
import com.wetube.wetube_service.dto.response.livekit.*;
import io.livekit.server.*;
import livekit.LivekitIngress;
import livekit.LivekitModels;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/livekit")
@AllArgsConstructor
public class LivekitController {
    private final RoomServiceClient roomClient;
    private final IngressServiceClient ingressClient;

    private final ObjectMapper objectMapper;

    private final LivekitProperties livekitProperties;


    @PostMapping("/create-stream")
    public CreateStreamResponse createStream(@RequestBody CreateStreamParams params) throws IOException {
        // === 1. Lấy identity từ metadata ===
        RoomMetadata metadata = params.getMetadata();

        // === 2. Nếu không có roomName thì generate ===
        String roomName = (params.getRoomName() == null || params.getRoomName().isEmpty())
                ? generateRoomId()
                : params.getRoomName();

        // === 3. Tạo AccessToken ===
        AccessToken at = new AccessToken(livekitProperties.getApiKey(), livekitProperties.getApiSecret());
        at.setIdentity(metadata.getCreatorIdentity());

        // add grants tương ứng
        at.addGrants(
                new RoomJoin(true),
                new RoomName(roomName),
                new CanPublish(true),
                new CanSubscribe(true)
        );

        // === 4. Tạo Room trong LiveKit ===
        roomClient.createRoom(
                roomName,
                null,
                null,
                null,
                objectMapper.writeValueAsString(metadata),
                null,
                null,
                null,
                null
        ).execute().body();

        // === 5. Connection details ===
        ConnectionDetails connectionDetails = new ConnectionDetails();
        connectionDetails.setWsUrl(livekitProperties.getUrl());
        connectionDetails.setToken(at.toJwt());

        // === 6. Auth token backend ===
        String authToken = createAuthToken(roomName, metadata.getCreatorIdentity());

        // === 7. Return DTO ===
        CreateStreamResponse response = new CreateStreamResponse();
        response.setAuthToken(authToken);
        response.setConnectionDetails(connectionDetails);

        return response;
    }

    @PostMapping("/join-stream")
    public JoinStreamResponse joinStream(@RequestBody JoinStreamParams params) throws IOException {
        String roomName = params.getRoomName();
        String identity = params.getIdentity();

        // Check required
        if (roomName == null || roomName.isBlank()) {
            throw new RuntimeException("room_name is required");
        }
        if (identity == null || identity.isBlank()) {
            throw new RuntimeException("identity is required");
        }

        // Check for existing participant
        boolean exists = false;
        try {
            Response<LivekitModels.ParticipantInfo> resp =
                    roomClient.getParticipant(roomName, identity).execute();
            if (resp.isSuccessful() && resp.body() != null) {
                exists = true;
            }
        } catch (Exception ignored) {
            // ignore like TS code
        }

        if (exists) {
            throw new RuntimeException("Participant already exists");
        }

        // Create LiveKit access token
        AccessToken at = new AccessToken(livekitProperties.getApiKey(), livekitProperties.getApiSecret());
        at.setIdentity(identity);
        at.addGrants(
                new RoomJoin(true),
                new RoomName(roomName),
                new CanPublish(false),
                new CanSubscribe(true),
                new CanPublishData(true)
        );

        // Create backend auth token
        String authToken = createAuthToken(roomName, identity);

        // Build response
        ConnectionDetails conn = new ConnectionDetails();
        conn.setWsUrl(livekitProperties.getUrl());
        conn.setToken(at.toJwt());

        JoinStreamResponse res = new JoinStreamResponse();
        res.setAuth_token(authToken);
        res.setConnection_details(conn);
        return res;
    }

    @GetMapping("/rooms/{roomName}/participants")
    public List<ParticipantDto> listParticipants(@PathVariable String roomName) throws IOException {
        retrofit2.Response<List<livekit.LivekitModels.ParticipantInfo>> resp =
                roomClient.listParticipants(roomName).execute();

        if (!resp.isSuccessful() || resp.body() == null) {
            throw new RuntimeException("Failed to fetch participants for room: " + roomName);
        }

        return resp.body().stream().map(p -> {
            ParticipantDto dto = new ParticipantDto();
            dto.setIdentity(p.getIdentity());
            dto.setName(p.getName());
            dto.setMetadata(p.getMetadata());
            dto.setPublisher(p.getTracksList().stream()
                    .anyMatch(t -> t.getType() == livekit.LivekitModels.TrackType.AUDIO
                            || t.getType() == livekit.LivekitModels.TrackType.VIDEO));
            dto.setMuted(p.getTracksList().stream().allMatch(livekit.LivekitModels.TrackInfo::getMuted));
            return dto;
        }).toList();
    }

    @PostMapping("/stop-stream")
    public void stopStream(@RequestBody StopStreamParams params) throws IOException {
        String roomName = params.getRoomName();
        String identity = params.getIdentity();

        // 1. Lấy danh sách room theo tên
        retrofit2.Response<List<LivekitModels.Room>> resp = roomClient.listRooms(List.of(roomName)).execute();
        if (!resp.isSuccessful() || resp.body() == null || resp.body().isEmpty()) {
            throw new RuntimeException("Room does not exist");
        }
        LivekitModels.Room room = resp.body().get(0);

        // 2. Parse metadata để xác thực creator
        RoomMetadata metadata;
        try {
            metadata = objectMapper.readValue(room.getMetadata(), RoomMetadata.class);
        } catch (Exception e) {
            throw new RuntimeException("Invalid room metadata");
        }

        if (!metadata.getCreatorIdentity().equals(identity)) {
            throw new RuntimeException("Only the creator can stop the stream");
        }

        // 3. Xóa room
        retrofit2.Response<Void> deleteResp = roomClient.deleteRoom(roomName).execute();
        if (!deleteResp.isSuccessful()) {
            throw new RuntimeException("Failed to delete room: " + deleteResp.message());
        }
    }

    @PostMapping("/create-ingress")
    public CreateIngressResponse createIngress(@RequestBody CreateIngressParams params) throws IOException {
        RoomMetadata metadata = params.getMetadata();
        String roomName = params.getRoomName();
        String ingressType = params.getIngressType() != null ? params.getIngressType() : "rtmp";

        if (roomName == null || roomName.isBlank()) {
            roomName = generateRoomId();
        }

        // Tạo Room
        roomClient.createRoom(
                roomName,
                null,
                null,
                null,
                objectMapper.writeValueAsString(metadata),
                null,
                null,
                null,
                null
        ).execute();

        // Video & Audio options
        LivekitIngress.IngressVideoOptions videoOptions = null;
        LivekitIngress.IngressAudioOptions audioOptions = null;
        Boolean bypassTranscoding = null;

        if ("whip".equalsIgnoreCase(ingressType)) {
            bypassTranscoding = true;
        } else {
            videoOptions = LivekitIngress.IngressVideoOptions.newBuilder()
                    .setSource(LivekitModels.TrackSource.CAMERA)
                    .setPreset(LivekitIngress.IngressVideoEncodingPreset.H264_1080P_30FPS_3_LAYERS)
                    .build();
            audioOptions = LivekitIngress.IngressAudioOptions.newBuilder()
                    .setSource(LivekitModels.TrackSource.MICROPHONE)
                    .setPreset(LivekitIngress.IngressAudioEncodingPreset.OPUS_STEREO_96KBPS)
                    .build();
        }

        // Gọi IngressServiceClient
        LivekitIngress.IngressInfo ingress = ingressClient.createIngress(
                roomName,
                roomName,
                metadata.getCreatorIdentity() + " (via OBS)",
                metadata.getCreatorIdentity() + " (via OBS)",
                "whip".equalsIgnoreCase(ingressType) ? LivekitIngress.IngressInput.WHIP_INPUT : LivekitIngress.IngressInput.RTMP_INPUT,
                audioOptions,
                videoOptions,
                bypassTranscoding,
                null,
                null
        ).execute().body();

        // Tạo LiveKit AccessToken
        AccessToken at = new AccessToken(livekitProperties.getApiKey(), livekitProperties.getApiSecret());
        at.setIdentity(metadata.getCreatorIdentity());
        at.addGrants(
                new RoomJoin(true),
                new RoomName(roomName),
                new CanPublish(false),
                new CanSubscribe(true),
                new CanPublishData(true)
        );

        String authToken = createAuthToken(roomName, metadata.getCreatorIdentity());

        ConnectionDetails conn = new ConnectionDetails();
        conn.setWsUrl(livekitProperties.getUrl());
        conn.setToken(at.toJwt());

        CreateIngressResponse response = new CreateIngressResponse();
        response.setIngress(ingress);
        response.setAuthToken(authToken);
        response.setConnectionDetails(conn);

        return response;
    }




    private String generateRoomId() {
        return "room-" + UUID.randomUUID();
    }

    private String createAuthToken(String roomName, String creatorIdentity) {
        // TODO: implement JWT signing if needed
        return UUID.randomUUID().toString();
    }

    @GetMapping("/token")
    public String getToken(
            @RequestParam String identity,
            @RequestParam String room) {

        AccessToken token = new AccessToken(livekitProperties.getApiKey(), livekitProperties.getApiSecret());
        token.setName(identity);      // Optional
        token.setIdentity(identity);  // Important
        token.setMetadata("custom-data");

        token.addGrants(
                new RoomJoin(true),
                new RoomName(room)
        );

        return token.toJwt();
    }
}

