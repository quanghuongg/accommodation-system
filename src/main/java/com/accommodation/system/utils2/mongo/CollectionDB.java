package com.accommodation.system.utils2.mongo;

import com.accommodation.system.utils2.GsonUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.accommodation.system.utils2.mongo.MongoDB.Action.GET;
import static com.accommodation.system.utils2.mongo.MongoDB.Action.INSERT;

public class CollectionDB<K> {
    public enum Action {
        GET,
        INSERT,
        UPDATE
    }

    private static Logger logger = LogManager.getLogger(CollectionDB.class);

    private MongoCollection collection;

    public CollectionDB(MongoCollection collection) {
        this.collection = collection;
    }

    private void verifyCollection() throws Exception {
        if (collection == null) {
            logger.error("[MONGO CONFIG] Collection not existed.");
            throw new Exception("DB Collection not existed. Please init DB before using.");
        }
    }

    public Document formatDocument(Object obj, MongoDB.Action action) {
        Document doc;
        String jsonStr = GsonUtils.GSON_UTCDATE_NORNUMBER_UNDERSCORE.toJson(obj);
        doc = Document.parse(jsonStr);

        Object id = doc.remove("id");
        if (id != null && (action == GET || action == INSERT)) {
            if (id instanceof String) {
                try {
                    doc.put("_id", new ObjectId((String) id));
                } catch (Exception ex) {
                    doc.put("_id", id);
                }
            } else {
                doc.put("_id", id);
            }
        }

        Date d = new Date(System.currentTimeMillis());
        logger.info("[MONGO CONFIG] Date format: " + d);
        switch (action) {
            case INSERT: {
                if (!doc.containsKey("created_at") || null == doc.get("created_at")) {
                    doc.put("created_at", d);
                }
                if (!doc.containsKey("last_updated_at") || null == doc.get("last_updated_at")) {
                    doc.put("last_updated_at", d);
                }
                if (null == doc.get("last_updated_by") && null != doc.get("created_by")) {
                    doc.put("last_updated_by", doc.get("created_by"));
                }
                break;
            }
            case UPDATE: {
                if (!doc.containsKey("last_updated_at") || null == doc.get("last_updated_at")) {
                    doc.put("last_updated_at", d);
                }
                break;
            }
        }
        return doc;
    }

    /**
     * @param v
     * @throws Exception INSERT document(s) into collection
     */
    public void insertOne(K v) throws Exception {
        verifyCollection();

        Document document = formatDocument(v, INSERT);
        collection.insertOne(document);
    }

    public void insertMany(List<K> list) throws Exception {
        verifyCollection();

        List<Document> documents = new ArrayList<>();
        for (K element : list) {
            documents.add(formatDocument(element, INSERT));
        }
        collection.insertMany(documents);
    }


    /**
     * @param matcher
     * @return count
     * @throws Exception COUNT documents in collection
     */
    public long count(Bson matcher) throws Exception {
        verifyCollection();
        return collection.count(matcher);
    }

    public long count(Bson matcher, Bson sort) throws Exception {
        verifyCollection();
        return collection.find(matcher).sort(sort).into(new ArrayList()).size();
    }


    /**
     * @param matcher
     * @return
     * @throws Exception RETRIEVE documents in collection
     */
    public Collection find(Bson matcher) throws Exception {
        verifyCollection();
        return collection.find(matcher).into(new ArrayList<>());
    }

    public Object findFirst(Bson matcher) throws Exception {
        verifyCollection();
        return collection.find(matcher).first();
    }

    public Collection find(Bson match, Bson sort, Bson projections, int page, int size) throws Exception {
        verifyCollection();
        return collection.find(match)
                .sort(sort)
                .projection(projections)
                .skip(page * size)
                .limit(size)
                .into(new ArrayList<>());
    }

    public Collection findAll() throws Exception {
        verifyCollection();
        return collection.find()
                .into(new ArrayList<>());
    }


    /**
     * @param dbObjects
     * @return
     * @throws Exception Aggregate list bson to Documents
     */
    public AggregateIterable aggregate(List<BasicDBObject> dbObjects) throws Exception {
        verifyCollection();
        return collection.aggregate(dbObjects);
    }


    /**
     * @param matcher
     * @param updates
     * @return
     * @throws Exception UPDATE document(s) in collection
     */
    public UpdateResult updateMany(Bson matcher, Bson updates) throws Exception {
        verifyCollection();
        return collection.updateMany(matcher, updates);
    }

    public UpdateResult updateMany(Bson matcher, Bson updates, UpdateOptions options) throws Exception {
        verifyCollection();
        return collection.updateMany(matcher, updates, options);
    }


    public UpdateResult updateMany(BasicDBObject query, BasicDBObject documentUpdate) throws Exception {
        verifyCollection();
        return collection.updateMany(query, documentUpdate);
    }


    /**
     * @param matcher
     * @return
     * @throws Exception DELETE document(s) in collection
     */
    public DeleteResult deleteOne(Bson matcher) throws Exception {
        verifyCollection();
        return collection.deleteOne(matcher);
    }

    public DeleteResult deleteMany(Bson matcher) throws Exception {
        verifyCollection();
        return collection.deleteMany(matcher);
    }
}
