package uk.gov.hmcts.reform.fpl.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import uk.gov.hmcts.reform.document.domain.Document;
import uk.gov.hmcts.reform.fpl.service.CaseRepository;
import uk.gov.hmcts.reform.fpl.service.DocumentGeneratorService;
import uk.gov.hmcts.reform.fpl.service.UploadDocumentService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static uk.gov.hmcts.reform.fpl.utils.DocumentManagementStoreLoader.document;
import static uk.gov.hmcts.reform.fpl.utils.ResourceReader.readBytes;

@ActiveProfiles("integration-test")
@WebFluxTest(CaseSubmissionController.class)
@OverrideAutoConfiguration(enabled = true)
class CaseSubmissionControllerTest {

    private static final String AUTH_TOKEN = "Bearer token";
    private static final String USER_ID = "1";

    @MockBean
    private DocumentGeneratorService documentGeneratorService;
    @MockBean
    private UploadDocumentService uploadDocumentService;
    @MockBean
    private CaseRepository caseRepository;

    @Autowired
    private WebTestClient webClient;

    @Test
    void shouldReturnSuccessfulResponseWithValidCaseData() throws Exception {
        byte[] pdf = {1, 2, 3, 4, 5};
        Document document = document();

        given(documentGeneratorService.generateSubmittedFormPDF(any()))
            .willReturn(pdf);
        given(uploadDocumentService.uploadPDF(USER_ID, AUTH_TOKEN, pdf, "2313.pdf"))
            .willReturn(document);

        webClient
            .post().uri("/callback/case-submission")
            .header("authorization", AUTH_TOKEN)
            .header("user-id", USER_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .syncBody(readBytes("fixtures/case.json"))
            .exchange()
            .expectStatus().is2xxSuccessful();

        Thread.sleep(3000);
        verify(caseRepository).setSubmittedFormPDF(AUTH_TOKEN, USER_ID, "2313", document);
    }

    @Test
    void shouldReturnUnsuccessfulResponseWithNoData() throws Exception {
        webClient
            .post().uri("/callback/case-submission")
            .header("authorization", AUTH_TOKEN)
            .header("user-id", USER_ID)
            .exchange()
            .expectStatus().is4xxClientError();
    }

    @Test
    void shouldReturnUnsuccessfulResponseWithMalformedData() throws Exception {
        webClient
            .post().uri("/callback/case-submission")
            .header("authorization", AUTH_TOKEN)
            .header("user-id", USER_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .syncBody("Mock")
            .exchange()
            .expectStatus().is4xxClientError();
    }
}
