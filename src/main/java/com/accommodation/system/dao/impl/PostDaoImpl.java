package com.accommodation.system.dao.impl;

import com.accommodation.system.dao.PostDao;
import com.accommodation.system.define.Constant;
import com.accommodation.system.entity.Post;
import com.accommodation.system.entity.model.SearchResult;
import com.accommodation.system.entity.request.PostRequest;
import com.accommodation.system.entity.request.SearchInput;
import com.accommodation.system.uitls.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;


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
    public final String indexName = "post";

    @Autowired
    public PostDaoImpl(ObjectMapper objectMapper, Client esClient) {
        this.objectMapper = objectMapper;
        this.esClient = esClient;
    }


    private List getSearchResultList(SearchHit[] searchHitArr) throws IOException {
        List<Post> posts = new LinkedList<>();
        for (SearchHit searchHit : searchHitArr) {
            String source = searchHit.getSourceAsString();
            Post post = this.objectMapper.readValue(source, Post.class);
            posts.add(post);
        }
        return posts;
    }

    public SearchResult getSearchResponse(SearchInput input, SearchHits searchHits) throws
            IOException {
        long total = searchHits.getTotalHits().value;
        int count = searchHits.getHits().length;
        List post = getSearchResultList(searchHits.getHits());
        SearchResult searchResult = SearchResult.builder()
                .total(total)
                .count(count)
                .hits(post)
                .build();
        return searchResult;
    }

    private void paginateResponse(SearchInput searchInput, SearchSourceBuilder searchSourceBuilder) {
        if (searchInput.getSize() > 0 && searchInput.getPage() >= 0) {
            searchSourceBuilder.size(searchInput.getSize());
            searchSourceBuilder.from(searchInput.getPage() * searchInput.getSize());
        } else {
            searchSourceBuilder.from(0);
            searchSourceBuilder.size(20);
        }
    }

    private SearchRequest buildSearchRequest(SearchInput searchInput) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indicesOptions(IndicesOptions.fromOptions(Constant.IGNORE_UNAVAILABLE, Constant.ALLOW_NO_INDICES, Constant.EXPAND_TO_OPEN_INDICES, Constant.EXPAND_TO_CLOSED_INDICES));
        searchRequest.indices(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.trackTotalHits(true);
        BoolQueryBuilder mainQueryBuilder = new BoolQueryBuilder();


        mainQueryBuilder.filter(QueryBuilders.termQuery(Constant.Post.JsonField.STATUS,
                1));

        if (searchInput.getUserId() > 0) {
            buildUserQuery(searchInput.getUserId(), mainQueryBuilder);
        }

        if (searchInput.getDistrictId() > 0) {
            buildDistrictQuery(searchInput.getDistrictId(), mainQueryBuilder);
        }
        if (searchInput.getWardId() > 0) {
            buildWardQuery(searchInput.getWardId(), mainQueryBuilder);
        }
        if (searchInput.getMaxArea() > 0) {
            buildAreaQuery(searchInput.getMinArea(), searchInput.getMaxArea(), mainQueryBuilder);

        }
        if (Utils.isNotEmpty(searchInput.getLocation())) {
            buildLocationQuery(searchInput.getLocation(), mainQueryBuilder);
        }
        if (searchInput.getRoomTypeId() > 0) {
            buildRoomTypeQuery(searchInput.getRoomTypeId(), mainQueryBuilder);
        }
        if (searchInput.getMaxPrice() > 0) {
            buildPriceQuery(searchInput.getMinPrice(), searchInput.getMaxPrice(), mainQueryBuilder);
        }
        paginateResponse(searchInput, searchSourceBuilder);

        int order = searchInput.getOrder();
        sortResponse(searchSourceBuilder, order);
        searchSourceBuilder.query(mainQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }

    private void buildPriceQuery(long minPrice, long maxPrice, BoolQueryBuilder mainQueryBuilder) {
        BoolQueryBuilder priceBoolQuery = new BoolQueryBuilder();
        priceBoolQuery.must(new RangeQueryBuilder(Constant.Post.JsonField.PRICE).gte(minPrice).lte(maxPrice));
        mainQueryBuilder.must(priceBoolQuery);
    }

    private void buildRoomTypeQuery(int roomType, BoolQueryBuilder mainQueryBuilder) {
        mainQueryBuilder.filter(QueryBuilders.termQuery(Constant.Post.JsonField.ROOM_TYPE_ID,
                roomType));
    }

    private void buildLocationQuery(String location, BoolQueryBuilder mainQueryBuilder) {
        mainQueryBuilder.filter(QueryBuilders.matchPhraseQuery(Constant.Post.JsonField.LOCATION, location));
    }

    private void buildAreaQuery(int minArea, int maxArea, BoolQueryBuilder mainQueryBuilder) {
        BoolQueryBuilder priceBoolQuery = new BoolQueryBuilder();
        priceBoolQuery.must(new RangeQueryBuilder(Constant.Post.JsonField.AREA).gte(minArea).lte(maxArea));
        mainQueryBuilder.must(priceBoolQuery);
    }

    private void buildWardQuery(int wardId, BoolQueryBuilder mainQueryBuilder) {
        mainQueryBuilder.filter(QueryBuilders.termQuery(Constant.Post.JsonField.WARD_ID,
                wardId));
    }

    private void buildDistrictQuery(int districtId, BoolQueryBuilder mainQueryBuilder) {
        mainQueryBuilder.filter(QueryBuilders.termQuery(Constant.Post.JsonField.DISTRICT_ID,
                districtId));
    }

    private void buildUserQuery(int userId, BoolQueryBuilder mainQueryBuilder) {
        mainQueryBuilder.filter(QueryBuilders.termQuery(Constant.Post.JsonField.USER_ID,
                userId));
    }


    @Override
    public Post find(String id) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indicesOptions(IndicesOptions.fromOptions(Constant.IGNORE_UNAVAILABLE, Constant.ALLOW_NO_INDICES, Constant.EXPAND_TO_OPEN_INDICES, Constant.EXPAND_TO_CLOSED_INDICES));
        searchRequest.indices(indexName);
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
    public Post findByUser(String id, int userId) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indicesOptions(IndicesOptions.fromOptions(Constant.IGNORE_UNAVAILABLE, Constant.ALLOW_NO_INDICES, Constant.EXPAND_TO_OPEN_INDICES, Constant.EXPAND_TO_CLOSED_INDICES));
        searchRequest.indices(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.trackTotalHits(true);
        BoolQueryBuilder mainQueryBuilder = QueryBuilders.boolQuery();
        mainQueryBuilder.filter(QueryBuilders.matchPhraseQuery("id", id));
        mainQueryBuilder.filter(QueryBuilders.matchPhraseQuery("user_id", userId));

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
        esClient.index(indexRequest);
        return uuid.toString();

    }


    private void sortResponse(SearchSourceBuilder searchSourceBuilder, int order) {
        switch (order) {
            case Constant.Order.Type.CREATE_TIME_ASC:
                searchSourceBuilder.sort(new FieldSortBuilder(Constant.Post.JsonField.CREATED_AT)
                        .order(SortOrder.ASC));
                break;
            case Constant.Order.Type.CREATE_TIME_DESC:
                searchSourceBuilder.sort(new FieldSortBuilder(Constant.Post.JsonField.CREATED_AT)
                        .order(SortOrder.DESC));
                break;
            case Constant.Order.Type.PRICE_ASC:
                searchSourceBuilder.sort(new FieldSortBuilder(Constant.Post.JsonField.PRICE)
                        .order(SortOrder.ASC));
                break;
            case Constant.Order.Type.PRICE_DESC:
                searchSourceBuilder.sort(new FieldSortBuilder(Constant.Post.JsonField.PRICE)
                        .order(SortOrder.DESC));
                break;
            case Constant.Order.Type.AREA_ASC:
                searchSourceBuilder.sort(new FieldSortBuilder(Constant.Post.JsonField.AREA)
                        .order(SortOrder.ASC));
                break;
            case Constant.Order.Type.AREA_DESC:
                searchSourceBuilder.sort(new FieldSortBuilder(Constant.Post.JsonField.AREA)
                        .order(SortOrder.DESC));
                break;
            default:
                break;
        }
    }

    @Override
    public SearchResult loadByIds(SearchInput requestInput) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indicesOptions(IndicesOptions.fromOptions(Constant.IGNORE_UNAVAILABLE, Constant.ALLOW_NO_INDICES, Constant.EXPAND_TO_OPEN_INDICES, Constant.EXPAND_TO_CLOSED_INDICES));
        searchRequest.indices(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.trackTotalHits(true);

        BoolQueryBuilder mainQueryBuilder = new BoolQueryBuilder();
        BoolQueryBuilder idQueryBuilder = new BoolQueryBuilder();
        for (String id : requestInput.getIds()) {
            idQueryBuilder.should(QueryBuilders.matchPhraseQuery("_id", id));
        }
        mainQueryBuilder.filter(idQueryBuilder);
        int order = requestInput.getOrder();
        sortResponse(searchSourceBuilder, order);
        searchSourceBuilder.query(mainQueryBuilder);
        searchRequest.source(searchSourceBuilder);

        org.elasticsearch.action.search.SearchResponse response = this.esClient.search(searchRequest).actionGet();
        log.info("took: {}", response.getTook().getMillis());
        SearchResult searchResult = this.getSearchResponse(requestInput, response.getHits());
        return searchResult;
    }

    @Override
    public SearchResult loadBySearchRequest(SearchInput searchInput) throws IOException {
        SearchRequest searchRequest = this.buildSearchRequest(searchInput);
        org.elasticsearch.action.search.SearchResponse response = this.esClient.search(searchRequest).actionGet();
        log.info("took: {}", response.getTook().getMillis());
        SearchResult ormSearchResponse = this.getSearchResponse(searchInput, response.getHits());
        return ormSearchResponse;
    }

    @Override
    public void deletePost(String postId) {
        this.esClient.prepareDelete(indexName, "_doc", postId).get();
    }

    @Override
    public void updatePost(PostRequest postRequest) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(indexName);
        updateRequest.type("_doc");
        updateRequest.id(postRequest.getPostId());
        updateRequest.doc(jsonBuilder()
                .startObject()
                .field("price", postRequest.getPrice())
                .field("title", postRequest.getTitle())
                .field("description", postRequest.getDescription())
                .endObject());
        this.esClient.update(updateRequest).actionGet();
    }

    @Override
    public void updateStatus(PostRequest postRequest) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(indexName);
        updateRequest.type("_doc");
        updateRequest.id(postRequest.getPostId());
        updateRequest.doc(jsonBuilder()
                .startObject()
                .field("status", postRequest.getStatus())
                .endObject());
        this.esClient.update(updateRequest).actionGet();
    }

    @Override
    public void updateImage(String postId, List<String> images) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(indexName);
        updateRequest.type("_doc");
        updateRequest.id(postId);
        String[] itemsArray = new String[images.size()];
        itemsArray = images.toArray(itemsArray);
        updateRequest.doc(jsonBuilder()
                .startObject()
                .field("image", images.get(0))
                .field("images", itemsArray)
                .endObject());
        this.esClient.update(updateRequest).actionGet();
    }
}
