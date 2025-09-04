package com.wetube.wetube_service.configuration;

import com.maxmind.geoip2.DatabaseReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@Configuration
public class GeoIPConfig {

    @Bean
    public DatabaseReader databaseReader() throws IOException {
        File database = new File("src/main/resources/GeoLite/GeoLite2-Country.mmdb");
        return new DatabaseReader.Builder(database).build();
    }
}
