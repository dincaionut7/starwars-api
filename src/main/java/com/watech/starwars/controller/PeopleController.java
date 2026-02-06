package com.watech.starwars.controller;

import com.watech.starwars.exception.BadRequestException;
import com.watech.starwars.model.CharacterResponse;
import com.watech.starwars.model.PeopleResponse;
import com.watech.starwars.service.PeopleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/people")
public class PeopleController {

  private final PeopleService peopleService;

  public PeopleController(PeopleService peopleService) {
    this.peopleService = peopleService;
  }

  @GetMapping
  public Mono<PeopleResponse> getPeople(@RequestParam(defaultValue = "1") int page) {
    if (page < 1) throw new BadRequestException("Page number must be 1 or greater");

    return peopleService.getPeople(page);
  }

  @GetMapping("/{id}")
  public Mono<CharacterResponse> getCharacter(@PathVariable int id) {
    if (id < 1) throw new BadRequestException("Character id must be 1 or greater");

    return peopleService.getCharacter(id);
  }
}
