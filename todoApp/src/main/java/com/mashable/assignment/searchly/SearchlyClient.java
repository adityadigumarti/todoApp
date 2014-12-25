package com.mashable.assignment.searchly;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Index;
import io.searchbox.core.Search;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryBuilders.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mashable.assignment.domain.TodoItem;
import com.mashable.assignment.exception.TodoApiInternalError;

public class SearchlyClient {

    private final static Logger LOG = LoggerFactory.getLogger(SearchlyClient.class);

    private static final String connectionUrl =
            "https://site:66f09d7e97a3e251018b1cebcdbd9461@bofur-us-east-1.searchly.com";
    private static JestClient client = null;

    static {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig.Builder(connectionUrl).multiThreaded(true).build());
        client = factory.getObject();
    }

    private static JestClient getClient() {
        return client;
    }

    public static void add(TodoItem todoItem) {
        Index index = new Index.Builder(todoItem).index("todoitems").type("todoitem").build();
        try {
            getClient().execute(index);
        } catch (Exception e) {
            LOG.error("Exception creating search Index.");
            throw new TodoApiInternalError();
        }
    }

    public static String search(String searchString) {
        String resultString = null;

        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(searchString, "title^3", "body");
            searchSourceBuilder.query(queryBuilder);

            Search search =
                    (Search) new Search.Builder(searchSourceBuilder.toString()).addIndex("todoItems")
                            .addType("todoitem").build();

            JestResult result = client.execute(search);
            resultString = result.getJsonString();
        } catch (Exception e) {
            LOG.error("Exception executing search Query.");
            throw new TodoApiInternalError();
        }

        return resultString;

    }
}
