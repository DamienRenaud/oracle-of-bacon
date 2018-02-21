package com.serli.oracle.of.bacon.repository;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ElasticSearchRepository {

    private final RestHighLevelClient client;
    private final RestClient lowClient;
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchRepository.class);

    public ElasticSearchRepository() {
        client = createClient();
        lowClient = createClientLowLevel();
    }

    public static RestHighLevelClient createClient() {
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")
                )
        );
    }


    public static RestClient createClientLowLevel() {
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200, "http"),
                new HttpHost("localhost", 9201, "http")).build();
        return restClient;
    }

    /**
     * Get actors suggestion based on suggest field in our es database.
     *
     * @param searchQuery the searchquery sent by the client.
     * @return a list of 5 potential actors.
     * @throws IOException on client.search function.
     */
    public List<String> getActorsSuggests(String searchQuery) throws IOException {
        final String SUGGESTION_NAME = "suggest_actor";
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SuggestionBuilder completionSuggestionBuilder = SuggestBuilders
                .completionSuggestion("suggest")
                .prefix(searchQuery, Fuzziness.AUTO);

        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion(SUGGESTION_NAME, completionSuggestionBuilder);
        searchSourceBuilder.suggest(suggestBuilder);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);

        Suggest suggest = searchResponse.getSuggest();
        CompletionSuggestion completionSuggestion = suggest.getSuggestion(SUGGESTION_NAME);


        List<String> result = new ArrayList<>();
        for (CompletionSuggestion.Entry entry : completionSuggestion.getEntries()) {
            for (CompletionSuggestion.Entry.Option option : entry) {
                result.add(option.getHit().getSourceAsMap().get("name").toString());
            }
        }
        return result;
    }
}
