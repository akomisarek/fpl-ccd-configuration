package uk.gov.hmcts.reform.fpl.model.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TelephoneNumber {
    private final String telephoneNumber;
    private final String telephoneUsageType;
    private final String contactDirection;

    @JsonCreator
    public TelephoneNumber(@JsonProperty("telephoneNumber") final String telephoneNumber,
                           @JsonProperty("telephoneUsageType") final String telephoneUsageType,
                           @JsonProperty("contactDirection") final String contactDirection) {
        this.telephoneNumber = telephoneNumber;
        this.telephoneUsageType = telephoneUsageType;
        this.contactDirection = contactDirection;
    }
}