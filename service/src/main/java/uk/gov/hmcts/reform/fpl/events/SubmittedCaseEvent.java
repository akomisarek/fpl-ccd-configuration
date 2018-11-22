package uk.gov.hmcts.reform.fpl.events;

import uk.gov.hmcts.reform.ccd.client.model.CallbackRequest;

public class SubmittedCaseEvent {

    private final CallbackRequest callbackRequest;
    private final String authorization;
    private final String userId;

    public SubmittedCaseEvent(CallbackRequest callbackRequest, String authorization, String userId) {
        this.callbackRequest = callbackRequest;
        this.authorization = authorization;
        this.userId = userId;
    }

    public CallbackRequest getCallbackRequest() {
        return callbackRequest;
    }

    public String getAuthorization() {
        return authorization;
    }

    public String getUserId() {
        return userId;
    }
}