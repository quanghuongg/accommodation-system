package com.accommodation.system.utils2.mongo;

import com.accommodation.system.define.Constant;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class MongoDB {

    private static Logger log = LogManager.getLogger(MongoDB.class);

    private static MongoClient mongoClient = null;

    private static class MongoDBHolder {
        private static final MongoDB INSTANCE = new MongoDB();
    }

    public static MongoDB getInstance() {
        return MongoDBHolder.INSTANCE;
    }

    public enum Action {
        GET,
        INSERT,
        UPDATE
    }

    public void init() {
        if (null == mongoClient) {
            try {
                MongoClientURI clientURI = new MongoClientURI("Constant.MongoConfiguration.MONGO_URI");
                log.info("[MONGO - COMMON] initialize with connection to {}", clientURI);
                mongoClient = new MongoClient(clientURI);
                MongoDatabase database = mongoClient.getDatabase("Constant.MongoConfiguration.MONGO_DATABASE");
                checkDatabaseAndCollectionsExisted(mongoClient, database);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public CollectionDB getCollection(String dbName, String name) {
        MongoCollection collection = mongoClient.getDatabase(dbName).getCollection(name);
        log.info("[MONGO - COMMON] collection {} is ready to use", name);
        return new CollectionDB(collection);
    }

    public CollectionDB getCollection(String name) {
        if (null == mongoClient) init();
        log.info("[MONGO - COMMON] collection {} is ready to use", name);
        return getCollection("Constant.MongoConfiguration.MONGO_DATABASE", name);
    }

    private void checkDatabaseAndCollectionsExisted(MongoClient mongoClient, MongoDatabase database) {
        boolean dbExisted = mongoClient.listDatabaseNames().into(new ArrayList<>()).contains(database.getName());
        if (!dbExisted) {
            log.error("[MONGO - COMMON] database not existed.");
            mongoClient.close();
        }

        String[] collectionList = "Constant.MongoConfiguration.MONGO_COLLECTIONS_LIST"
                .split("Constant.MongoConfiguration.COLLECTION_DELIMITER");
        for (String collection : collectionList) {
            checkCollection(database, collection);
        }
    }

    private void checkCollection(MongoDatabase database, String collectionName) {
        boolean isCollectionExisted = database.listCollectionNames().into(new ArrayList<>()).contains(collectionName);
        if (!isCollectionExisted) {
            log.info("[MONGO - COMMON] collection {} is not existed. Initializing one... ", collectionName);
            database.createCollection(collectionName);
            log.info("[MONGO - COMMON] collection {} initialized success !", collectionName);
        }
    }
}
