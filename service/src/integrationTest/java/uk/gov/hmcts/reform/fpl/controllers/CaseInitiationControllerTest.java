package uk.gov.hmcts.reform.fpl.controllers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import uk.gov.hmcts.reform.ccd.client.model.AboutToStartOrSubmitCallbackResponse;
import uk.gov.hmcts.reform.ccd.client.model.CallbackRequest;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.fpl.utils.CoreCaseDataStoreLoader;
import uk.gov.hmcts.reform.idam.client.IdamApi;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;

import static org.mockito.BDDMockito.given;

@ActiveProfiles("integration-test")
@WebFluxTest(CaseInitiationController.class)
@OverrideAutoConfiguration(enabled = true)
class CaseInitiationControllerTest {

    private static final String AUTH_TOKEN = "Bearer token";

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private IdamApi idamApi;

    @Test
    void shouldAddCaseLocalAuthorityToCaseData() {
        AboutToStartOrSubmitCallbackResponse expectedResponse = AboutToStartOrSubmitCallbackResponse.builder()
            .data(ImmutableMap.<String, Object>builder()
                .put("caseName", "title")
                .put("caseLocalAuthority", "EX")
                .build())
            .build();

        given(idamApi.retrieveUserDetails(AUTH_TOKEN)).willReturn(
            new UserDetails(null, "user@example.gov.uk", null, null, null));

        CallbackRequest request = CallbackRequest.builder().caseDetails(CaseDetails.builder()
            .data(ImmutableMap.<String, Object>builder()
                .put("caseName", "title")
                .build()).build())
            .build();

        webClient
            .post().uri("/callback/case-initiation")
            .header("authorization", AUTH_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .syncBody(request)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(AboutToStartOrSubmitCallbackResponse.class).isEqualTo(expectedResponse);
    }

    @Test
    void shouldPopulateErrorsInResponseWhenDomainNameIsNotFound() throws Exception {
        AboutToStartOrSubmitCallbackResponse expectedResponse = AboutToStartOrSubmitCallbackResponse.builder()
            .errors(ImmutableList.<String>builder()
                .add("The email address was not linked to a known Local Authority")
                .build())
            .build();

        given(idamApi.retrieveUserDetails(AUTH_TOKEN))
            .willReturn(new UserDetails(null, "user@email.gov.uk", null, null, null));

        webClient
            .post().uri("/callback/case-initiation")
            .header("authorization", AUTH_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .syncBody(CoreCaseDataStoreLoader.emptyCaseDetails())
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(AboutToStartOrSubmitCallbackResponse.class).isEqualTo(expectedResponse);
    }
}
