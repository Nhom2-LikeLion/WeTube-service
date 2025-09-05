// package com.wetube.wetube_service.controller;

// import java.util.List;
// import java.util.UUID;

// import com.wetube.wetube_service.dto.response.ChannelResponseDto;
// import com.wetube.wetube_service.dto.response.SubscribedChannelDto;
// import com.wetube.wetube_service.entity.channel.Channel;
// import com.wetube.wetube_service.service.SubscriptionService;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;


// import lombok.AllArgsConstructor;

// @RestController
// @RequestMapping("/api/subscriptions")
// @AllArgsConstructor
// public class SubscriptionController {
//     private final SubscriptionService subscriptionService;

//     @GetMapping("/{userId}")
//     public ResponseEntity<List<SubscribedChannelDto>> getSubscribedChannels(@PathVariable UUID userId) {
//         return ResponseEntity.ok(subscriptionService.getSubscribedChannels(userId));
//     }
// }
