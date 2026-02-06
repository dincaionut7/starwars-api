package com.watech.starwars.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/favourites")
public class FavouritesController {

  @GetMapping
  public Mono<List<String>> getFavourites() {
    return Mono.just(List.of("BB-8", "R2-D2", "C-3PO"));
  }
}
