package com.mashable.assignment.search.service;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;
import io.searchbox.core.Search;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mashable.assignment.domain.TodoItem;
import com.mashable.assignment.exception.SearchClientException;
import com.mashable.assignment.exception.TodoApiInternalError;

@Service("searchlyClientService")
public class SearchlyClientServiceImpl implements ElasticSearchClientService {

    private static final String SEARCHLY_TYPE = "todoitem";
    private static final String SEARCHLY_INDEX = "todoitems";

    private static final Logger LOG = LoggerFactory.getLogger(SearchlyClientServiceImpl.class);

    @Value("${searchly.connection.url}")
    private String connectionUrl;
    private JestClient client = null;

    @Override
    public void add(TodoItem todoItem) {
        Index index = new Index.Builder(todoItem).index(SEARCHLY_INDEX).type(SEARCHLY_TYPE).build();

        try {
            getClient().execute(index);
        } catch (Exception e) {
            throw new SearchClientException("Exception indexing Data for Search");
        }

    }

    @Override
    public String search(String searchString) {
        String resultString = null;

        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(searchString, "title^3", "body");
            searchSourceBuilder.query(queryBuilder);

            Search search =
                    (Search) new Search.Builder(searchSourceBuilder.toString()).addIndex("todoItems")
                            .addType(SEARCHLY_TYPE).build();

            JestResult result = client.execute(search);
            resultString = result.getJsonString();
        } catch (Exception e) {
            LOG.error("Exception executing search Query.");
            throw new TodoApiInternalError();
        }

        return resultString;

    }

    /**
     * As per Elastic search document, Update is Deleting and Re Indexing.
     * So We will delete the existing document and Re Index it.
     * 
     * TODO - Issue here if delete works and add fails. Is there an Update API?
     */
    @Override
    public void update(String id, TodoItem todoItem) {
        delete(id);
        add(todoItem);
    }

    @Override
    public void delete(String id) {
        try {
            client.execute(new Delete.Builder(id).index(SEARCHLY_INDEX).type(SEARCHLY_TYPE).build());
        } catch (Exception e) {
            e.printStackTrace();
            throw new SearchClientException("Exception Deleting Data for Search");
        }
    }

    private JestClient getClient() {
        if (client == null) {
            JestClientFactory factory = new JestClientFactory();
            factory.setHttpClientConfig(new HttpClientConfig.Builder(connectionUrl).multiThreaded(true).build());
            client = factory.getObject();
        }

        return client;
    }

}
