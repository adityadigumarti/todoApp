package com.mashable.assignment.repository;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.mashable.assignment.domain.TodoItem;
import com.mashable.assignment.exception.TodoApiInternalError;
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

    public static final String TITLE = "title";
    public static final String BODY = "body";
    public static final String ID = "_id";
    public static final String TODO_COLLECTION = "todoCollection";
    public static final String TASK_STATUS = "done";

    private static final Logger LOG = LoggerFactory.getLogger(MongoTodoItemRepository.class);

    private MongoClient mongoClient;
    private DB db;

    @Value("${mongo.db.user}")
    private String dbUserName;

    @Value("${mongo.db.password}")
    private String dbPassword;

    @Value("${mongo.db.name}")
    private String databaseName;

    @Value("${mongo.db.serverAddress}")
    private String serverAddress;

    @Value("${mongo.db.server.port}")
    private int port;

    @Override
    public void insert(TodoItem todoItem) {
        BasicDBObject doc =
                new BasicDBObject(TITLE, todoItem.getTitle()).append(BODY, todoItem.getBody()).append(TASK_STATUS,
                        todoItem.isDone());

        DBCollection coll = getDB().getCollection(TODO_COLLECTION);

        coll.insert(doc);

        ObjectId id = (ObjectId) doc.get(ID);
        todoItem.setId(id.toString());
    }

    @Override
    public void delete(String id) {
        BasicDBObject query = new BasicDBObject(ID, new ObjectId(id));
        DBCollection coll = getDB().getCollection(TODO_COLLECTION);
        DBObject result = findDocument(query, coll);

        coll.remove(result);

    }

    @Override
    public TodoItem findById(String id) {
        BasicDBObject query = new BasicDBObject(ID, new ObjectId(id));
        DBCollection coll = getDB().getCollection(TODO_COLLECTION);
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
        DBCollection collection = getDB().getCollection(TODO_COLLECTION);

        BasicDBObject newDocument = new BasicDBObject();
        newDocument.append("$set", new BasicDBObject().append(TASK_STATUS, status));

        BasicDBObject searchQuery = new BasicDBObject().append(ID, new ObjectId(id));

        collection.update(searchQuery, newDocument);
    }

    @Override
    public void update(String id, TodoItem todoItem) {
        DBCollection collection = getDB().getCollection(TODO_COLLECTION);

        BasicDBObject carrier = new BasicDBObject();
        BasicDBObject set = new BasicDBObject("$set", carrier);

        if (todoItem.getTitle() != null) {
            carrier.put(TITLE, todoItem.getTitle());
        }

        if (todoItem.getBody() != null) {
            carrier.put(BODY, todoItem.getBody());
        }

        BasicDBObject searchQuery = new BasicDBObject().append(ID, new ObjectId(id));

        collection.update(searchQuery, set);

    }

    @Override
    public Collection<TodoItem> findAll(boolean ignoreCompletedTasks) {

        Collection<TodoItem> list = new ArrayList<TodoItem>();
        DBCollection coll = getDB().getCollection(TODO_COLLECTION);

        DBCursor cursor = null;

        if (ignoreCompletedTasks) {
            BasicDBObject query = new BasicDBObject(TASK_STATUS, false);
            cursor = coll.find(query);
        } else {
            cursor = coll.find();
        }

        while (cursor.hasNext()) {
            list.add(getTodo(cursor.next()));
        }

        return list;
    }

    private TodoItem getTodo(DBObject result) {
        TodoItem todoItem = null;

        if (result != null) {
            todoItem = new TodoItem();

            if (result.get(TITLE) != null) {
                todoItem.setTitle(result.get(TITLE).toString());
            }

            if (result.get(BODY) != null) {
                todoItem.setBody(result.get(BODY).toString());
            }

            if (result.get(TASK_STATUS) != null) {
                todoItem.setDone(Boolean.parseBoolean(result.get(TASK_STATUS).toString()));
            }

            todoItem.setId(result.get(ID).toString());

        }

        return todoItem;
    }

    public DB getDB() {
        if (db == null) {
            try {
                MongoCredential credential =
                        MongoCredential.createMongoCRCredential(dbUserName, databaseName, dbPassword.toCharArray());
                mongoClient = new MongoClient(new ServerAddress(serverAddress, port), Arrays.asList(credential));
                db = mongoClient.getDB(databaseName);
            } catch (UnknownHostException e) {
                LOG.error("Exception instantiating Mongo DB", e);
                throw new TodoApiInternalError("Exception instantiating Mongo DB", e);
            }
        }

        return db;

    }
}
