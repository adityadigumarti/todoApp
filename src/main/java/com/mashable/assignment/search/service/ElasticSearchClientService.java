package com.mashable.assignment.search.service;

import com.mashable.assignment.domain.TodoItem;

/**
 * Interface for the Search Client
 * 
 * 
 * @author Adi
 * 
 */
public interface ElasticSearchClientService {

    public void add(TodoItem todoItem);

    public String search(String searchString);

    public void update(String id, TodoItem todoItem);

    public void delete(String id);

}
