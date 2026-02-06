package com.watech.starwars.client;

import com.watech.starwars.exception.ExternalApiClientException;
import com.watech.starwars.exception.NotFoundException;
import com.watech.starwars.exception.SwapiUnavailableException;
import com.watech.starwars.model.PeopleResponse;
import com.watech.starwars.model.Person;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
public class SwapiClient {

  private final WebClient swapiClient;

  public SwapiClient(@Qualifier("swapiWebClient") WebClient swapiClient) {
    this.swapiClient = swapiClient;
  }

  public Mono<PeopleResponse> fetchPeople(int page) {
    return swapiClient
        .get()
        .uri("/people?page=" + page)
        .retrieve()
        .bodyToMono(PeopleResponse.class)
        .transform(m -> applySwapiErrorMapping(m, "Page number " + page));
  }

  public Mono<Person> fetchCharacter(int id) {
    return swapiClient
        .get()
        .uri("/people/" + id)
        .retrieve()
        .bodyToMono(Person.class)
        .transform(m -> applySwapiErrorMapping(m, "Character with id " + id));
  }

  private <T> Mono<T> applySwapiErrorMapping(Mono<T> mono, String resourceLabel) {
    return mono.onErrorMap(
            WebClientResponseException.NotFound.class,
            e -> new NotFoundException(resourceLabel + " not found"))
        .onErrorMap(
            WebClientResponseException.class,
            e -> {
              if (e.getStatusCode().is5xxServerError())
                return new SwapiUnavailableException(
                    "SWAPI unavailable with code " + e.getStatusCode(), e);
              else
                return new ExternalApiClientException(
                    "SWAPI non-500 error code: " + e.getStatusCode(), e);
            })
        .onErrorMap(
            WebClientRequestException.class,
            e -> new SwapiUnavailableException("SWAPI was unreachable", e));
  }
}
