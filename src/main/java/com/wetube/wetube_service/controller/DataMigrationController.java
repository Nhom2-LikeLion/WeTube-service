package com.wetube.wetube_service.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wetube.wetube_service.service.DataMigrationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/migration")
@RequiredArgsConstructor
public class DataMigrationController {
    private final DataMigrationService migrationService;

    /**
     * Import all videos from MySQL to Elasticsearch
     */
    @PostMapping("/import-all")
    public ResponseEntity<Map<String, Object>> importAllVideos() {
        try {
            long mysqlCount = migrationService.getMySQLVideoCount();
            long esCountBefore = migrationService.getElasticsearchDocumentCount();
            
            migrationService.importAllVideosToElasticsearch();
            
            long esCountAfter = migrationService.getElasticsearchDocumentCount();
            
            return ResponseEntity.ok(Map.of(
                "message", "Import completed successfully",
                "mysqlProducts", mysqlCount,
                "elasticsearchDocumentsBefore", esCountBefore,
                "elasticsearchDocumentsAfter", esCountAfter,
                "imported", esCountAfter - esCountBefore
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Import failed: " + e.getMessage()));
        }
    }

    /**
     * Import specific videos by IDs
     */
    @PostMapping("/import-by-ids")
    public ResponseEntity<Map<String, Object>> importVideosByIds(@RequestBody List<UUID> videoIds) {
        try {
            migrationService.importVideosByIds(videoIds);
            return ResponseEntity.ok(Map.of(
                "message", "Import completed successfully",
                "importedCount", videoIds.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Import failed: " + e.getMessage()));
        }
    }

    /**
     * Import videos by title
     */
    @PostMapping("/import-by-category")
    public ResponseEntity<Map<String, Object>> importVideoByTitle(@RequestParam String title) {
        try {
            migrationService.importVideoByTitle(title);
            return ResponseEntity.ok(Map.of(
                "message", "Import completed successfully",
                "category", title
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Import failed: " + e.getMessage()));
        }
    }

    /**
     * Clear all data from Elasticsearch
     */
    @DeleteMapping("/clear-elasticsearch")
    public ResponseEntity<Map<String, Object>> clearElasticsearchData() {
        try {
            migrationService.clearElasticsearchData();
            return ResponseEntity.ok(Map.of(
                "message", "Elasticsearch data cleared successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Clear failed: " + e.getMessage()));
        }
    }

    /**
     * Get statistics about data in both systems
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        try {
            long mysqlCount = migrationService.getMySQLVideoCount();
            long esCount = migrationService.getElasticsearchDocumentCount();
            
            return ResponseEntity.ok(Map.of(
                "mysqlProducts", mysqlCount,
                "elasticsearchDocuments", esCount,
                "syncStatus", mysqlCount == esCount ? "SYNCED" : "OUT_OF_SYNC"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to get stats: " + e.getMessage()));
        }
    }
}

