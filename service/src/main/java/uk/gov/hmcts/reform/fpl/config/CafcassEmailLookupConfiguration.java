package uk.gov.hmcts.reform.fpl.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.fpl.config.utils.LookupConfigParser;

import java.util.Map;

@Configuration
public class CafcassEmailLookupConfiguration {

    private final Map<String, String> mapping;

    public CafcassEmailLookupConfiguration(@Value("${fpl.local_authority_code_to_cafcass_email.mapping}")
                                               String config) {
        this.mapping = LookupConfigParser.parseStringValue(config);
    }

    public Map<String, String> getLookupTable() {
        return mapping;
    }
}
