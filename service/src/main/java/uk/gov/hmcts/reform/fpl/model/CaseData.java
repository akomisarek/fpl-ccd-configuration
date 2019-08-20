package uk.gov.hmcts.reform.fpl.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.reform.fpl.enums.OrderType;
import uk.gov.hmcts.reform.fpl.model.common.Document;
import uk.gov.hmcts.reform.fpl.model.common.DocumentSocialWorkOther;
import uk.gov.hmcts.reform.fpl.model.common.Element;
import uk.gov.hmcts.reform.fpl.validators.interfaces.HasDocumentStatus;
import uk.gov.hmcts.reform.fpl.validators.interfaces.HasEPOGrounds;
import uk.gov.hmcts.reform.fpl.validators.interfaces.HasThresholdDetails;
import uk.gov.hmcts.reform.fpl.validators.interfaces.HasThresholdReason;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@HasEPOGrounds
@HasThresholdReason
@HasThresholdDetails
@HasDocumentStatus
@SuppressWarnings("membername")
public class CaseData {
    @NotBlank(message = "Enter a case name")
    private final String caseName;
    private final String gatekeeperEmail;
    private final String caseLocalAuthority;
    private final Risks risks;
    @NotNull(message = "You need to add details to orders and directions needed")
    @Valid
    private final Orders orders;

    public boolean hasEPOGrounds() {
        return orders != null && orders.getOrderType() != null
            && orders.getOrderType().contains(OrderType.EMERGENCY_PROTECTION_ORDER);
    }

    private final Grounds grounds;
    private final GroundsForEPO groundsForEPO;
    @NotNull(message = "You need to add details to children")
    @Valid
    private final Children children;
    private final OldApplicant applicant;
    @NotNull(message = "You need to add details to applicant")
    private final List<Element<Applicant>> applicants;

    @Valid
    public final Applicant getMainApplicant() {
        if (applicants != null && applicants.get(0) != null && applicants.get(0).getValue() != null) {
            return applicants.get(0).getValue();
        } else {
            return Applicant.builder().build();
        }
    }

    private final String applicantsMigrated;
    private final List<Element<RespondentParty>> respondent;
    private final Proceeding proceeding;
    private final Solicitor solicitor;
    private final FactorsParenting factorsParenting;
    private final AllocationProposal allocationProposal;
    @NotNull(message = "You need to add details to hearing needed")
    @Valid
    private final Hearing hearing;
    private final HearingPreferences hearingPreferences;
    private final InternationalElement internationalElement;

    private final List<Element<DocumentSocialWorkOther>> documents_socialWorkOther;

    @JsonProperty("documents_socialWorkCarePlan_document")
    public Document socialWorkCarePlanDocument;

    @JsonProperty("documents_socialWorkStatement_document")
    public Document socialWorkStatementDocument;

    @JsonProperty("documents_socialWorkAssessment_document")
    public Document socialWorkAssessmentDocument;

    @JsonProperty("documents_socialWorkChronology_document")
    public Document socialWorkChronologyDocument;

    @JsonProperty("documents_checklist_document")
    public Document checklistDocument;

    @JsonProperty("documents_threshold_document")
    public Document thresholdDocument;

    private final Document documents_socialWorkEvidenceTemplate_document;
}
