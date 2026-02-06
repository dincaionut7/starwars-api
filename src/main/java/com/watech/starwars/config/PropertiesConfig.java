package com.watech.starwars.config;

import com.watech.starwars.config.properties.AppCacheProperties;
import com.watech.starwars.config.properties.SwapiProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({SwapiProperties.class, AppCacheProperties.class})
public class PropertiesConfig {}
