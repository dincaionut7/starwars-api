package com.watech.starwars.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.watech.starwars.config.properties.AppCacheProperties;
import com.watech.starwars.config.properties.AppCacheProperties.CacheProps;
import com.watech.starwars.model.CharacterResponse;
import com.watech.starwars.model.PeopleResponse;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class CacheConfig {

  private <K, V> Cache<K, V> buildCache(CacheProps props) {
    return Caffeine.newBuilder()
        .maximumSize(props.maxSize())
        .expireAfterWrite(Duration.ofMinutes(props.ttl()))
        .build();
  }

  @Bean
  public Cache<Integer, Mono<PeopleResponse>> peopleCache(AppCacheProperties props) {
    return buildCache(props.peoplePages());
  }

  @Bean
  public Cache<Integer, Mono<CharacterResponse>> charactersCache(AppCacheProperties props) {
    return buildCache(props.characters());
  }
}
