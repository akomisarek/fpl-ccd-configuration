package uk.gov.hmcts.reform.fpl.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("integration-test")
@WebFluxTest(RootController.class)
@OverrideAutoConfiguration(enabled = true)
class RootControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    void shouldWelcomeUponRootRequestWith200ResponseCode() {
        webClient
            .get().uri("/")
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(String.class).isEqualTo("Welcome to fpl-service");
    }
}
