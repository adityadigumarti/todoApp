package com.mashable.assignment.repository;

import java.util.Collection;

import com.mashable.assignment.domain.TodoItem;

public interface TodoItemRepository {

    public void insert(TodoItem todoItem);

    public void delete(String id);

    public TodoItem findById(String id);

    public void updateStatus(String id, boolean status);

    public Collection<TodoItem> findAll();

}
