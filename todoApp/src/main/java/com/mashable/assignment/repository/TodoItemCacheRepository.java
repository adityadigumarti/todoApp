package com.mashable.assignment.repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

import com.mashable.assignment.domain.TodoItem;
import com.mashable.assignment.util.TodoAppUtil;

/**
 * In Memory cache to store the Todo Items.
 * 
 * 
 * @author diguma01
 * 
 */
@Component
public class TodoItemCacheRepository implements TodoItemRepository {

    private static Map<String, TodoItem> itemsCache = new ConcurrentHashMap<String, TodoItem>(100);

    @Override
    public void insert(TodoItem todoItem) {
        todoItem.setId(TodoAppUtil.generateUniqueId());
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

}
