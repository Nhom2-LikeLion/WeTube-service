package com.wetube.wetube_service.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.wetube.wetube_service.service.DataMigrationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "app.data.auto-import", havingValue = "true", matchIfMissing = false)
public class DataInitializationConfig implements CommandLineRunner {
    private final DataMigrationService migrationService;

    @Override
    public void run(String... args) throws Exception {
        log.info("Auto-import enabled. Starting data migration from MySQL to Elasticsearch...");
        
        try {
            long mysqlCount = migrationService.getMySQLVideoCount();
            long esCount = migrationService.getElasticsearchDocumentCount();
            
            if (mysqlCount > 0 && esCount == 0) {
                log.info("Found {} products in MySQL and {} documents in Elasticsearch. Starting import...", 
                    mysqlCount, esCount);
                migrationService.importAllVideosToElasticsearch();
                log.info("Auto-import completed successfully");
            } else if (esCount > 0) {
                log.info("Elasticsearch already contains {} documents. Skipping auto-import.", esCount);
            } else {
                log.info("No products found in MySQL. Skipping auto-import.");
            }
        } catch (Exception e) {
            log.error("Auto-import failed: {}", e.getMessage(), e);
        }
    }
}

