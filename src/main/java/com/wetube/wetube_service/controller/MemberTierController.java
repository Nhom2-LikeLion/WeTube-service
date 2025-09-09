package com.wetube.wetube_service.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wetube.wetube_service.dto.request.MemberTierRequest;
import com.wetube.wetube_service.mapper.MemberTierMapper;
import com.wetube.wetube_service.service.MemberTierService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/tiers")
@RequiredArgsConstructor
public class MemberTierController {
    private final MemberTierService memberTierService;
    private final MemberTierMapper mapper;

    @Operation(summary = "Create a new member tier", description = "Creates a new membership tier for the given channelId and returns the created resource location")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tier created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Channel not found")
    })
    @PostMapping("/{channelId}")
    public ResponseEntity<Void> createTier(
            @PathVariable UUID channelId,
            @RequestBody MemberTierRequest request) {

        UUID createdId = memberTierService.addTier(
                channelId,
                mapper.requestToDto(request)
        );

        URI location = URI.create("/api/tiers/" + createdId);
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Update a member tier", description = "Updates an existing membership tier by its tierId")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tier updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Tier not found")
    })
    @PutMapping("/{tierId}")
    public ResponseEntity<Void> updateTier(
            @PathVariable UUID tierId,
            @RequestBody MemberTierRequest request) {

        memberTierService.updateTier(tierId, mapper.requestToDto(request));
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary = "Delete a member tier", description = "Deletes a membership tier by its tierId")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tier deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Tier not found")
    })
    @DeleteMapping("/{tierId}")
    public ResponseEntity<Void> deleteTier(@PathVariable UUID tierId) {
        memberTierService.deleteTier(tierId);
        return ResponseEntity.noContent().build();
    }
}