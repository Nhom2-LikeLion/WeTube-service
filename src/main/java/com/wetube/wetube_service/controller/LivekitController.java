package com.wetube.wetube_service.controller;

import io.livekit.server.AccessToken;
import io.livekit.server.RoomJoin;
import io.livekit.server.RoomName;
import io.livekit.server.VideoGrant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/livekit")
public class LivekitController {

    private final String API_KEY = "APIBXxGvEUrkfkB";
    private final String API_SECRET = "vzplSC1XqWjK4QlkJE8mv0eIWcA4MvLeCe245rcWDHzB";

    @GetMapping("/token")
    public String getToken(
            @RequestParam String identity,
            @RequestParam String room) {

        AccessToken token = new AccessToken(API_KEY, API_SECRET);
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