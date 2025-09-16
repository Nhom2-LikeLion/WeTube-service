//package com.wetube.wetube_service.controller;
//
//import com.wetube.wetube_service.dto.room.RoomMember;
//import com.wetube.wetube_service.dto.room.UpcommingListUpdate;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.stream.Collectors;
//
//@Controller
//public class RoomController {
//
//    private final Map<String, Set<String>> roomMembers = new ConcurrentHashMap<>();
//
//    @MessageMapping("/rooms.members.{roomId}")
//    @SendTo("/topic/rooms.{roomId}.members")
//    public RoomMember updateMembers(@DestinationVariable String roomId, RoomMember member) {
//        roomMembers.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(member.getUsername());
//
//        List<RoomMember> membersList = roomMembers.get(roomId).stream()
//                .map(username -> {
//                    RoomMember m = new RoomMember();
//                    m.setUsername(username);
//                    return m;
//                })
//                .collect(Collectors.toList());
//
//        return new RoomMember(membersList.size(), membersList);
//    }
//
//    @MessageMapping("/rooms.members.leave.{roomId}")
//    @SendTo("/topic/rooms.{roomId}.members")
//    public RoomMember leaveRoom(@DestinationVariable String roomId, RoomMember member) {
//        Set<String> members = roomMembers.getOrDefault(roomId, Collections.emptySet());
//        members.remove(member.getUsername());
//
//        List<RoomMember> membersList = members.stream()
//                .map(username -> {
//                    RoomMember m = new RoomMember();
//                    m.setUsername(username);
//                    return m;
//                })
//                .collect(Collectors.toList());
//
//        return new RoomMember(membersList.size(), membersList);
//    }
//
//    @MessageMapping("/rooms.playlist.{roomId}")
//    @SendTo("/topic/rooms.{roomId}.playlist")
//    public UpcommingListUpdate updatePlaylist(@DestinationVariable String roomId, UpcommingListUpdate update) {
//        return update;
//    }
//}
//
