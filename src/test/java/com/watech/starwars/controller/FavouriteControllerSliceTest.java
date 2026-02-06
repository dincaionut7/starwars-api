package com.watech.starwars.controller;

import static org.mockito.Mockito.when;

import com.watech.starwars.filter.AuthFilter;
import com.watech.starwars.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(FavouritesController.class)
@Import(AuthFilter.class)
public class FavouriteControllerSliceTest {

  @Autowired WebTestClient webClient;

  @MockitoBean AuthService authService;

  @Test
  void getFavouritesNoTokenReturns401() {

    webClient.get().uri("/favourites").exchange().expectStatus().isUnauthorized();
  }

  @Test
  void getFavouritesValidTokenReturns200() {
    when(authService.isAccessTokenValid("valid")).thenReturn(true);

    webClient
        .get()
        .uri("/favourites")
        .header("Authorization", "Bearer valid")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(String.class);
  }
}
