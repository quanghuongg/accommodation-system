package com.accommodation.system.dao.impl;

import com.accommodation.system.dao.PostDao;
import com.accommodation.system.define.Constant;
import com.accommodation.system.entity.Post;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * User: huongnq4
 * Date:  11/06/2020
 * Time: 13 :41
 * To change this template use File | Settings | File and Code Templates.
 */
@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class PostDaoImpl implements PostDao {
    private final Client esClient;
    private final ObjectMapper objectMapper;
    public final List<String> indexNames = Arrays.asList(new String[]{"post"});

    @Autowired
    public PostDaoImpl(ObjectMapper objectMapper, Client esClient) {
        this.objectMapper = objectMapper;
        this.esClient = esClient;
    }

    @Override
    public Post find(String id) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indicesOptions(IndicesOptions.fromOptions(Constant.IGNORE_UNAVAILABLE, Constant.ALLOW_NO_INDICES, Constant.EXPAND_TO_OPEN_INDICES, Constant.EXPAND_TO_CLOSED_INDICES));
        searchRequest.indices(String.valueOf(indexNames));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.trackTotalHits(true);
        BoolQueryBuilder mainQueryBuilder = QueryBuilders.boolQuery();
        mainQueryBuilder.filter(QueryBuilders.matchPhraseQuery("id", id));
        searchSourceBuilder.query(mainQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        org.elasticsearch.action.search.SearchResponse response = this.esClient.search(searchRequest).actionGet();
        SearchHits searchHits = response.getHits();
        long count = searchHits.getTotalHits().value;
        if (count == 0) {
            return null;
        } else {
            String message = searchHits.getAt(0).getSourceAsString();
            Post post = this.objectMapper.readValue(message, Post.class);
            return post;
        }
    }

    @Override
    public String createPost(Post document) {
        UUID uuid = UUID.randomUUID();
        document.setId(uuid.toString());

        Map<String, Object> documentMapper = objectMapper.convertValue(document, Map.class);

        IndexRequest indexRequest = new IndexRequest("post", "_doc", uuid.toString())
                .source(documentMapper);
        ActionFuture<IndexResponse> indexResponse = esClient.index(indexRequest);
        return uuid.toString();

    }
}
