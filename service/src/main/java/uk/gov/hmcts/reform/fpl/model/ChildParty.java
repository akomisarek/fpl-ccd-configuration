package uk.gov.hmcts.reform.fpl.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.hmcts.reform.fpl.enums.PartyType;
import uk.gov.hmcts.reform.fpl.model.common.EmailAddress;
import uk.gov.hmcts.reform.fpl.model.common.Party;
import uk.gov.hmcts.reform.fpl.model.common.Telephone;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ChildParty extends Party {
    private final String gender;
    private final String genderIdentification;
    private final String livingSituation;
    private final String situationDetails;
    private final Date situationDate;
    private final String keyDates;
    private final String careAndContact;
    private final String adoption;
    private final String placementOrderApplication;
    private final String placementCourt;
    private final String mothersName;
    private final String fathersName;
    private final String fathersResponsibility;
    private final String socialWorkerName;
    private final Telephone socialWorkerTel;
    private final String additionalNeeds;
    private final String additionalNeedsDetails;
    private final String detailsHidden;
    private final String detailsHiddenReason;
    private final String litigationIssues;
    private final String litigationIssuesDetails;

    @Builder(toBuilder = true)
    public ChildParty(String partyID,
                       PartyType partyType,
                       String firstName,
                       String lastName,
                       Date dateOfBirth,
                       Address address,
                       EmailAddress email,
                       Telephone telephoneNumber,
                       String gender,
                       String genderIdentification,
                       String livingSituation,
                       String situationDetails,
                       Date situationDate,
                       String keyDates,
                       String careAndContact,
                       String adoption,
                       String placementOrderApplication,
                       String placementCourt,
                       String mothersName,
                       String fathersName,
                       String fathersResponsibility,
                       String socialWorkerName,
                       Telephone socialWorkerTel,
                       String additionalNeeds,
                       String additionalNeedsDetails,
                       String detailsHidden,
                       String detailsHiddenReason,
                       String litigationIssues,
                       String litigationIssuesDetails) {
        super(partyID, partyType, firstName, lastName, dateOfBirth, address, email, telephoneNumber);

        this.gender = gender;
        this.genderIdentification = genderIdentification;
        this.livingSituation = livingSituation;
        this.situationDetails = situationDetails;
        this.situationDate = situationDate;
        this.keyDates = keyDates;
        this.careAndContact = careAndContact;
        this.adoption = adoption;
        this.placementOrderApplication = placementOrderApplication;
        this.placementCourt = placementCourt;
        this.mothersName = mothersName;
        this.fathersName = fathersName;
        this.fathersResponsibility = fathersResponsibility;
        this.socialWorkerName = socialWorkerName;
        this.socialWorkerTel = socialWorkerTel;
        this.additionalNeeds = additionalNeeds;
        this.additionalNeedsDetails = additionalNeedsDetails;
        this.detailsHidden = detailsHidden;
        this.detailsHiddenReason = detailsHiddenReason;
        this.litigationIssues = litigationIssues;
        this.litigationIssuesDetails = litigationIssuesDetails;
    }
}
