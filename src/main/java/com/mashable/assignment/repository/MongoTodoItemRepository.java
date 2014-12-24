package com.mashable.assignment.repository;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.mashable.assignment.domain.TodoItem;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

/**
 * Mongo Persistence Implementation.
 * 
 * 
 * @author Adi
 * 
 */
@Component("mongoTodoItemRepository")
public class MongoTodoItemRepository implements TodoItemRepository {

    private static final Logger LOG = LoggerFactory.getLogger(MongoTodoItemRepository.class);

    private MongoClient mongoClient;
    private DB db;

    public MongoTodoItemRepository() {
        try {
            MongoCredential credential =
                    MongoCredential.createMongoCRCredential("todoUser", "todoApp", "todoPassword".toCharArray());
            mongoClient = new MongoClient(new ServerAddress("dogen.mongohq.com", 10040), Arrays.asList(credential));
            db = mongoClient.getDB("todoApp");
        } catch (UnknownHostException e) {
            LOG.error("Exception instantiating Mongo DB", e);
            throw new InstantiationError("Exception instantiating Mongo DB");
        }
    }

    @Override
    public void insert(TodoItem todoItem) {
        BasicDBObject doc =
                new BasicDBObject("title", todoItem.getTitle()).append("body", todoItem.getBody()).append("done",
                        todoItem.isDone());

        DBCollection coll = db.getCollection("todoCollection");

        coll.insert(doc);

        ObjectId id = (ObjectId) doc.get("_id");
        todoItem.setId(id.toString());
    }

    @Override
    public void delete(String id) {
        BasicDBObject query = new BasicDBObject("_id", new ObjectId(id));
        DBCollection coll = db.getCollection("todoCollection");
        DBObject result = findDocument(query, coll);

        coll.remove(result);

    }

    @Override
    public TodoItem findById(String id) {
        BasicDBObject query = new BasicDBObject("_id", new ObjectId(id));
        DBCollection coll = db.getCollection("todoCollection");
        DBObject result = findDocument(query, coll);

        return getTodo(result);
    }

    private DBObject findDocument(BasicDBObject query, DBCollection coll) {
        DBCursor cursor = coll.find(query);
        DBObject result = null;

        try {
            if (cursor.hasNext()) {
                result = cursor.next();
            }
        } finally {
            cursor.close();
        }
        return result;
    }

    @Override
    public void updateStatus(String id, boolean status) {
        DBCollection collection = db.getCollection("todoCollection");

        BasicDBObject newDocument = new BasicDBObject();
        newDocument.append("$set", new BasicDBObject().append("done", status));

        BasicDBObject searchQuery = new BasicDBObject().append("_id", new ObjectId(id));

        collection.update(searchQuery, newDocument);
    }

    @Override
    public void update(String id, TodoItem todoItem) {
        DBCollection collection = db.getCollection("todoCollection");

        BasicDBObject carrier = new BasicDBObject();
        BasicDBObject set = new BasicDBObject("$set", carrier);

        if (todoItem.getTitle() != null) {
            carrier.put("title", todoItem.getTitle());
        }

        if (todoItem.getBody() != null) {
            carrier.put("body", todoItem.getBody());
        }

        BasicDBObject searchQuery = new BasicDBObject().append("_id", new ObjectId(id));

        collection.update(searchQuery, set);

    }

    @Override
    public Collection<TodoItem> findAll() {
        Collection<TodoItem> list = new ArrayList<TodoItem>();
        DBCollection coll = db.getCollection("todoCollection");

        DBCursor cursor = coll.find();

        while (cursor.hasNext()) {
            list.add(getTodo(cursor.next()));
        }

        return list;
    }

    private TodoItem getTodo(DBObject result) {
        TodoItem todoItem = null;

        if (result != null) {
            todoItem = new TodoItem();

            if (result.get("title") != null) {
                todoItem.setTitle(result.get("title").toString());
            }

            if (result.get("body") != null) {
                todoItem.setBody(result.get("body").toString());
            }

            if (result.get("done") != null) {
                todoItem.setDone(Boolean.parseBoolean(result.get("done").toString()));
            }

            todoItem.setId(result.get("_id").toString());

        }

        return todoItem;
    }

}
