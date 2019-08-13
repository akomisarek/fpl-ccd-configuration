package uk.gov.hmcts.reform.fpl.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
// Added supression to remove pattern match error on DOB - must match pattern '^[a-z][a-z0-9][a-zA-Z0-9]*$'
@SuppressWarnings("all")
public class Other {

    private final String DOB;
    private final String name;
    private final String gender;
    private final Address address;
    private final String telephone;
    private final String birthplace;
    private final String childInformation;
    private final String litigationIssues;
    private final String genderIdentification;
}
