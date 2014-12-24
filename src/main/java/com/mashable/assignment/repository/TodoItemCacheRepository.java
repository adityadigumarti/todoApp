package com.mashable.assignment.repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.mashable.assignment.domain.TodoItem;

/**
 * In Memory cache to store the Todo Items.
 * 
 * 
 * @author Adi
 * 
 */
@Component("todoItemRepository")
public class TodoItemCacheRepository implements TodoItemRepository {

    private static Map<String, TodoItem> itemsCache = new ConcurrentHashMap<String, TodoItem>(100);

    private static final TimeBasedGenerator gen = Generators.timeBasedGenerator(EthernetAddress.fromInterface());

    @Override
    public void insert(TodoItem todoItem) {
        todoItem.setId(generateUniqueId());
        itemsCache.put(todoItem.getId(), todoItem);
    }

    @Override
    public void delete(String id) {
        itemsCache.remove(id);

    }

    @Override
    public TodoItem findById(String id) {
        return itemsCache.get(id);
    }

    @Override
    public void updateStatus(String id, boolean done) {
        findById(id).setDone(done);
    }

    @Override
    public Collection<TodoItem> findAll() {
        return itemsCache.values();
    }

    private String generateUniqueId() {
        return gen.generate().toString();
    }

    @Override
    public void update(String id, TodoItem todoItem) {
        TodoItem currentItem = itemsCache.get(id);

        if (todoItem.getBody() != null) {
            currentItem.setBody(todoItem.getBody());
        }

        if (todoItem.getTitle() != null) {
            currentItem.setTitle(todoItem.getTitle());
        }
    }

}
