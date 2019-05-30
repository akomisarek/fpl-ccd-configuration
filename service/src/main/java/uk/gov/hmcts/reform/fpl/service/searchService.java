//package uk.gov.hmcts.reform.fpl.service;
//
//import org.codelibs.elasticsearch.index.query.BoolQueryBuilder;
//import org.codelibs.elasticsearch.index.query.QueryBuilder;
//import org.codelibs.elasticsearch.index.query.QueryBuilders;
//import org.codelibs.elasticsearch.search.builder.SearchSourceBuilder;
//
//public class searchService {
//
//    BoolQueryBuilder query = QueryBuilders.boolQuery();
//
//    QueryBuilder qb = QueryBuilders.matchQuery("caseData.caseName", "app");
//
//    String testString = new SearchSourceBuilder().query(query).toString();
//
//    //call elastic search endpoint
//    //map response to a builder for the cases so the results are returned as ccd cases so end up with
//}
