package com.watech.starwars.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "swapi")
public record SwapiProperties(String baseUrl) {}
