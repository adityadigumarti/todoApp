package com.mashable.assignment.repository;

import java.util.Collection;

import com.mashable.assignment.domain.TodoItem;

/**
 * Interface for the Repository Class.
 * All Repository Implementations implement this.
 * 
 * Makes it easy to switch to a different impl.
 * 
 * @author Adi
 * 
 */
public interface TodoItemRepository {

    /**
     * Insert TODO Item
     * 
     * @param todoItem
     */
    public void insert(TodoItem todoItem);

    /**
     * Delete Todo Item, given unique ID
     * 
     * @param id
     */
    public void delete(String id);

    /**
     * Find Todo Instance by Id
     * 
     * @param id
     * @return
     */
    public TodoItem findById(String id);

    /**
     * Update Status , given Id
     * 
     * @param id
     * @param status
     */
    public void updateStatus(String id, boolean status);

    /**
     * Update's Body or title of Todo Item.
     * 
     * @param id
     * @param todoItem
     */
    public void update(String id, TodoItem todoItem);

    /**
     * Returns all Todo Item's
     * 
     * @return
     */
    public Collection<TodoItem> findAll(boolean ignoreCompletedTasks);

}
