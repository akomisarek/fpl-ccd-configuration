package uk.gov.hmcts.reform.fpl.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
// Added supression to remove pattern match error on type_giveReason - must match pattern '^[a-z][a-z0-9][a-zA-Z0-9]*$'
@SuppressWarnings("all")
public class Hearing {

    private final String type;
    private final String reason;
    @NotBlank(message = "Select an option for when you need a hearing")
    private final String timeFrame;
    private final String reducedNotice;
    private final String withoutNotice;
    private final String type_GiveReason;
    private final String respondentsAware;
    private final String reducedNoticeReason;
    private final String withoutNoticeReason;
    private final String respondentsAwareReason;
}
