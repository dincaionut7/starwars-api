package com.watech.starwars.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cache")
public record AppCacheProperties(CacheProps peoplePages, CacheProps characters) {
  public record CacheProps(int ttl, int maxSize) {}
}
