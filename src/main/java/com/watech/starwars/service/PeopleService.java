package com.watech.starwars.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.watech.starwars.client.SwapiClient;
import com.watech.starwars.model.CharacterResponse;
import com.watech.starwars.model.PeopleResponse;
import com.watech.starwars.model.Person;
import com.watech.starwars.util.FormatUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PeopleService {

  private final SwapiClient swapiClient;

  private final Cache<Integer, Mono<PeopleResponse>> peopleCache;
  private final Cache<Integer, Mono<CharacterResponse>> charactersCache;

  public PeopleService(
      SwapiClient swapiClient,
      @Qualifier("peopleCache") Cache<Integer, Mono<PeopleResponse>> peopleCache,
      @Qualifier("charactersCache") Cache<Integer, Mono<CharacterResponse>> charactersCache) {
    this.swapiClient = swapiClient;
    this.peopleCache = peopleCache;
    this.charactersCache = charactersCache;
  }

  public Mono<PeopleResponse> getPeople(int page) {
    return peopleCache.get(
        page, k -> swapiClient.fetchPeople(k).doOnError(e -> peopleCache.invalidate(k)).cache());
  }

  public Mono<CharacterResponse> getCharacter(int id) {
    return charactersCache.get(
        id,
        k ->
            swapiClient
                .fetchCharacter(k)
                .map(c -> toCharacterResponse(c))
                .doOnError(e -> charactersCache.invalidate(k))
                .cache());
  }

  private CharacterResponse toCharacterResponse(Person p) {
    return new CharacterResponse(
        p.name(),
        FormatUtil.parseDouble(p.height()) / 100,
        FormatUtil.parseDouble(p.mass()),
        p.birthYear(),
        p.films().size(),
        FormatUtil.formatOffsetDateDMY(p.created()));
  }
}
