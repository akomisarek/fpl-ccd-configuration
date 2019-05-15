package uk.gov.hmcts.reform.fpl.controllers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.ccd.client.model.AboutToStartOrSubmitCallbackResponse;
import uk.gov.hmcts.reform.ccd.client.model.CallbackRequest;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;

import java.util.Map;

@Api
@RestController
@RequestMapping("/callback/children")
public class ChildrenInitiationController {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public ChildrenInitiationController(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostMapping("/about-to-start")
    @SuppressWarnings("unchecked")
    public AboutToStartOrSubmitCallbackResponse handleAboutToStartEvent(
        @RequestHeader(value = "authorization") String authorization,
        @RequestBody CallbackRequest callbackrequest) {
        CaseDetails caseDetails = callbackrequest.getCaseDetails();

        Map<String, Object> data = caseDetails.getData();

        data.put("children", ImmutableMap.builder()
            .put("additionalChildren", ImmutableList.builder()
                .add(ImmutableMap.builder()
                    .put("id", "1")
                    .put("value", ImmutableMap.builder()
                        .put("childName", "")
                        .build())
                    .build())
                .build())
            .build());


        return AboutToStartOrSubmitCallbackResponse.builder()
            .data(data)
            .build();
    }
}
