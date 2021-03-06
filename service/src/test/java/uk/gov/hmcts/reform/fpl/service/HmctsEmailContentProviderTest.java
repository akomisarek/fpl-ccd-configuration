package uk.gov.hmcts.reform.fpl.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.fpl.config.HmctsCourtLookupConfiguration;
import uk.gov.hmcts.reform.fpl.config.HmctsCourtLookupConfiguration.Court;
import uk.gov.hmcts.reform.fpl.config.LocalAuthorityNameLookupConfiguration;
import uk.gov.hmcts.reform.fpl.service.email.content.HmctsEmailContentProvider;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static uk.gov.hmcts.reform.fpl.CaseDefinitionConstants.CASE_TYPE;
import static uk.gov.hmcts.reform.fpl.CaseDefinitionConstants.JURISDICTION;
import static uk.gov.hmcts.reform.fpl.utils.CoreCaseDataStoreLoader.emptyCaseDetails;
import static uk.gov.hmcts.reform.fpl.utils.CoreCaseDataStoreLoader.populatedCaseDetails;

@ExtendWith(SpringExtension.class)
class HmctsEmailContentProviderTest {

    private static final String LOCAL_AUTHORITY_CODE = "example";
    private static final String COURT_NAME = "Test court";
    private static final String COURT_EMAIL_ADDRESS = "FamilyPublicLaw+test@gmail.com";

    @Mock
    private HmctsCourtLookupConfiguration hmctsCourtLookupConfiguration;

    @Mock
    private LocalAuthorityNameLookupConfiguration localAuthorityNameLookupConfiguration;

    @InjectMocks
    private HmctsEmailContentProvider hmctsEmailContentProvider;

    @Test
    void shouldReturnExpectedMapWithValidCaseDetails() throws IOException {
        List<String> ordersAndDirections = ImmutableList.of("Emergency protection order",
            "Contact with any named person");
        Map<String, Object> expectedMap = ImmutableMap.<String, Object>builder()
            .put("court", COURT_NAME)
            .put("localAuthority", "Example Local Authority")
            .put("dataPresent", "Yes")
            .put("fullStop", "No")
            .put("ordersAndDirections", ordersAndDirections)
            .put("timeFramePresent", "Yes")
            .put("timeFrameValue", "Same day")
            .put("reference", "12345")
            .put("caseUrl", "null/case/" + JURISDICTION + "/" + CASE_TYPE + "/12345")
            .build();

        given(hmctsCourtLookupConfiguration.getCourt(LOCAL_AUTHORITY_CODE))
            .willReturn(new Court(COURT_NAME, COURT_EMAIL_ADDRESS));

        given(localAuthorityNameLookupConfiguration.getLocalAuthorityName(LOCAL_AUTHORITY_CODE))
            .willReturn("Example Local Authority");

        assertThat(hmctsEmailContentProvider.buildHmctsSubmissionNotification(populatedCaseDetails(),
            LOCAL_AUTHORITY_CODE)).isEqualTo(expectedMap);
    }

    @Test
    void shouldReturnSuccessfullyWithEmptyCaseDetails() throws IOException {
        Map<String, Object> expectedMap = ImmutableMap.<String, Object>builder()
            .put("court", COURT_NAME)
            .put("localAuthority", "Example Local Authority")
            .put("dataPresent", "No")
            .put("fullStop", "Yes")
            .put("ordersAndDirections", "")
            .put("timeFramePresent", "No")
            .put("timeFrameValue", "")
            .put("reference", "123")
            .put("caseUrl", "null/case/" + JURISDICTION + "/" + CASE_TYPE + "/123")
            .build();

        given(hmctsCourtLookupConfiguration.getCourt(LOCAL_AUTHORITY_CODE))
            .willReturn(new Court(COURT_NAME, COURT_EMAIL_ADDRESS));

        given(localAuthorityNameLookupConfiguration.getLocalAuthorityName(LOCAL_AUTHORITY_CODE))
            .willReturn("Example Local Authority");

        assertThat(hmctsEmailContentProvider.buildHmctsSubmissionNotification(emptyCaseDetails(),
            LOCAL_AUTHORITY_CODE)).isEqualTo(expectedMap);
    }
}
