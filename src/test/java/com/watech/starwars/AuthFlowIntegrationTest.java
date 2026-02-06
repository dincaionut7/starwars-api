package com.watech.starwars;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.watech.starwars.model.auth.LoginRequest;
import com.watech.starwars.model.auth.LoginResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class AuthFlowIntegrationTest {

  private final String REFRESH_HEADER = "Refresh-Token";
  private final String NEW_ACCESS_HEADER = "New-Access-Token";

  @Autowired WebTestClient webTestClient;

  @Test
  void fullAuthFlowLoginAccessRefreshLogoutAccess() {

    // LOGIN
    LoginResponse login =
        webTestClient
            .post()
            .uri("/auth/login")
            .bodyValue(new LoginRequest("wa", "tech"))
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(LoginResponse.class)
            .returnResult()
            .getResponseBody();

    assertNotNull(login);
    assertNotNull(login.accessToken());
    assertNotNull(login.refreshToken());

    String accessToken = login.accessToken();
    String refreshToken = login.refreshToken();

    // ACCESS PROTECTED ENDPOINT WITH VALID ACCESS TOKEN
    webTestClient
        .get()
        .uri("/favourites")
        .header("Authorization", "Bearer " + accessToken)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(List.class);

    // ACCESS AGAIN WITH INVALID TOKEN ( OLD TOKEN ) AND NO REFRESH HEADER
    webTestClient
        .get()
        .uri("/favourites")
        .header("Authorization", "Bearer old")
        .exchange()
        .expectStatus()
        .isUnauthorized();

    // ACCESS AGAIN WITH OLD ACCESS TOKEN + VALID REFRESH TOKEN
    // EXPECT 200 + new access token header
    String newAccessToken =
        webTestClient
            .get()
            .uri("/favourites")
            .header("Authorization", "Bearer old") // old/invalid
            .header(REFRESH_HEADER, refreshToken) // valid refresh
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .exists(NEW_ACCESS_HEADER)
            .expectBody(List.class)
            .returnResult()
            .getResponseHeaders()
            .getFirst(NEW_ACCESS_HEADER);

    assertNotNull(newAccessToken);
    assertFalse(newAccessToken.isBlank());

    // ACCESS WITH NEW ACCESS TOKEN
    webTestClient
        .get()
        .uri("/favourites")
        .header("Authorization", "Bearer " + newAccessToken)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(List.class);

    // 6) LOGOUT WITH NEW TOKEN (CLEAR SESSION)
    webTestClient
        .post()
        .uri("/auth/logout")
        .header("Authorization", "Bearer " + newAccessToken)
        .exchange()
        .expectStatus()
        .isNoContent();

    // 7) NOW PROTECTED ENDPOINT SHOULD BE BLOCKED
    webTestClient
        .get()
        .uri("/favourites")
        .header("Authorization", "Bearer " + newAccessToken)
        .exchange()
        .expectStatus()
        .isUnauthorized();
  }
}
