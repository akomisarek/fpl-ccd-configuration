package uk.gov.hmcts.reform.fpl.controllers;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.ccd.client.model.CallbackRequest;
import uk.gov.hmcts.reform.fpl.events.SubmittedCaseEvent;

import javax.validation.constraints.NotNull;

@Api
@RestController
@RequestMapping("/callback/case-submission")
public class CaseSubmissionController {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public CaseSubmissionController(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostMapping
    public void handleCaseSubmission(
        @RequestHeader(value = "authorization") String authorization,
        @RequestHeader(value = "user-id") String userId,
        @RequestBody @NotNull CallbackRequest callbackRequest) {

        applicationEventPublisher.publishEvent(new SubmittedCaseEvent(callbackRequest, authorization, userId));
    }
}
