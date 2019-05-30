package uk.gov.hmcts.reform.fpl.controllers;

import io.swagger.annotations.Api;
import org.codelibs.elasticsearch.index.query.BoolQueryBuilder;
import org.codelibs.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.ccd.client.model.AboutToStartOrSubmitCallbackResponse;
import uk.gov.hmcts.reform.ccd.client.model.CallbackRequest;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;

import java.util.Map;

import static org.codelibs.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.codelibs.elasticsearch.index.query.QueryBuilders.matchQuery;

@Api
@RestController
@RequestMapping("/callback/search-case")
public class SearchACaseController {

    @PostMapping("/about-to-submit")
    public AboutToStartOrSubmitCallbackResponse handleAboutToStartEvent(
        @RequestHeader(value = "authorization") String authorization,
        @RequestBody CallbackRequest callbackrequest) {
        CaseDetails caseDetails = callbackrequest.getCaseDetails();
        Map<String, Object> data = caseDetails.getData();
        BoolQueryBuilder query = boolQuery();

        query.must(matchQuery("caseData.searchACase", "callbackRequest.getData.getCaseName()"));

        String jsonQuery = new SearchSourceBuilder().query(query).toString();

        System.out.println("jsonQuery = " + jsonQuery);
        
        return null;

    }
}
