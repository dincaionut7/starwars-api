package com.watech.starwars.config;

import com.watech.starwars.config.properties.SwapiProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  @Bean
  WebClient swapiWebClient(SwapiProperties props) {
    return WebClient.builder().baseUrl(props.baseUrl()).build();
  }
}
