package uk.gov.hmcts.reform.fpl.handlers;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.fpl.config.CafcassEmailLookupConfiguration;
import uk.gov.hmcts.reform.fpl.events.SubmittedCaseEvent;
import uk.gov.hmcts.reform.fpl.service.HmctsCourtLookUpService;
import uk.gov.hmcts.reform.fpl.service.LocalAuthorityService;
import uk.gov.service.notify.NotificationClient;
import uk.gov.service.notify.NotificationClientException;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static uk.gov.hmcts.reform.fpl.CaseDefinitionConstants.CASE_TYPE;
import static uk.gov.hmcts.reform.fpl.CaseDefinitionConstants.JURISDICTION;
import static uk.gov.hmcts.reform.fpl.NotifyTemplates.CAFCASS_SUBMISSION_TEMPLATE;
import static uk.gov.hmcts.reform.fpl.NotifyTemplates.HMCTS_COURT_SUBMISSION_TEMPLATE;

@Component
public class NotificationHandler {

    private final HmctsCourtLookUpService hmctsCourtLookUpService;
    private final CafcassEmailLookupConfiguration cafcassEmailLookupConfiguration;
    private final LocalAuthorityService localAuthorityService;
    private final NotificationClient notificationClient;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String uiBaseUrl;

    @Autowired
    public NotificationHandler(HmctsCourtLookUpService hmctsCourtLookUpService,
                               NotificationClient notificationClient,
                               CafcassEmailLookupConfiguration cafcassEmailLookupConfiguration,
                               LocalAuthorityService localAuthorityService,
                               @Value("${ccd.ui.base.url}") String uiBaseUrl) {
        this.hmctsCourtLookUpService = hmctsCourtLookUpService;
        this.cafcassEmailLookupConfiguration = cafcassEmailLookupConfiguration;
        this.notificationClient = notificationClient;
        this.localAuthorityService = localAuthorityService;
        this.uiBaseUrl = uiBaseUrl;
    }

    @EventListener
    public void sendNotificationToHmctsAdmin(SubmittedCaseEvent event) {
        CaseDetails caseDetails = event.getCallbackRequest().getCaseDetails();
        String localAuthorityCode = caseDetails.getData().get("caseLocalAuthority").toString();
        Map<String, String> parameters = buildHmctsEmailData(caseDetails, localAuthorityCode);
        String reference = caseDetails.getId().toString();

        String email = hmctsCourtLookUpService.getCourt(localAuthorityCode).getEmail();
        logger.debug(
            "Sending submission notification (with template id: {}) to {}", HMCTS_COURT_SUBMISSION_TEMPLATE, email);

        try {
            notificationClient.sendEmail(HMCTS_COURT_SUBMISSION_TEMPLATE, email, parameters, reference);
        } catch (NotificationClientException e) {
            logger.warn("Failed to send submission notification (with template id: {}) to {}",
                HMCTS_COURT_SUBMISSION_TEMPLATE, email, e);
        }
    }

    @EventListener
    public void sendNotificationToCafcass(SubmittedCaseEvent event) {
        CaseDetails caseDetails = event.getCallbackRequest().getCaseDetails();
        String localAuthorityCode = (String) caseDetails.getData().get("caseLocalAuthority");
        Map<String, String> parameters = buildCafcassEmailData(caseDetails, localAuthorityCode);
        String reference = (String.valueOf(caseDetails.getId()));

        String email = cafcassEmailLookupConfiguration.getLookupTable().get(localAuthorityCode);
        logger.debug("Sending email to {}", email);

        try {
            notificationClient.sendEmail(CAFCASS_SUBMISSION_TEMPLATE, email, parameters, reference);
        } catch (NotificationClientException e) {
            logger.warn("Failed to send email to {}", email, e);
        }
    }

    private Map<String, String> buildHmctsEmailData(CaseDetails caseDetails, String localAuthorityCode) {
        Map orders =
            Optional.ofNullable((Map) caseDetails.getData().get("orders")).orElse(ImmutableMap.builder().build());

        String orderType = Optional.ofNullable(orders.get("orderType")).orElse("").toString();

        Map hearing =
            Optional.ofNullable((Map) caseDetails.getData().get("hearing")).orElse(ImmutableMap.builder().build());

        return ImmutableMap.<String, String>builder()
            .put("court", hmctsCourtLookUpService.getCourt(localAuthorityCode).getName())
            .put("localAuthority", localAuthorityService.getLocalAuthorityName(localAuthorityCode))
            .put("orders", orderType.replace("[", "").replace("]", ""))
            .put("directionsAndInterim", Optional.ofNullable((String) orders.get("directionsAndInterim"))
                .orElse(""))
            .put("timeFramePresent", (hearing.containsKey("timeFrame")) ? ("Yes") : ("No"))
            .put("timeFrame", Optional.ofNullable((String) hearing.get("timeFrame")).orElse(""))
            .put("reference", String.valueOf(caseDetails.getId()))
            .put("caseUrl", uiBaseUrl + "/case/" + JURISDICTION + "/" + CASE_TYPE + "/"
                + String.valueOf(caseDetails.getId()))
            .build();
    }

    private Map<String, String> buildCafcassEmailData(CaseDetails caseDetails, String localAuthorityCode) {
        Map orders =
            Optional.ofNullable((Map) caseDetails.getData().get("orders")).orElse(ImmutableMap.builder().build());

        ArrayList orderType = (ArrayList) Optional.ofNullable(orders.get("orderType")).orElse(new ArrayList<>());

        ImmutableMap.Builder<String, String> orderTypeArray = ImmutableMap.builder();
        for (int i = 0; i < 5; i++) {
            if (i < orderType.size()) {
                orderTypeArray.put("orders" + i, "^" + orderType.get(i));
            } else {
                orderTypeArray.put("orders" + i, "");
            }
        }

        return ImmutableMap.<String, String>builder()
            .put("localAuthority", localAuthorityService.getLocalAuthorityName(localAuthorityCode))
            .putAll(orderTypeArray.build())
            .put("directionsAndInterim", (orders.containsKey("directionsAndInterim"))
                ? "^" + (orders.get("directionsAndInterim")) : "")
            .put("reference", String.valueOf(caseDetails.getId()))
            .put("caseUrl", uiBaseUrl + "/case/" + JURISDICTION + "/" + CASE_TYPE + "/" + caseDetails.getId())
            .build();
    }
}
