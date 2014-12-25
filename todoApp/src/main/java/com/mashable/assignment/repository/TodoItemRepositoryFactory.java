package com.mashable.assignment.repository;

import org.springframework.beans.factory.annotation.Autowired;

public class TodoItemRepositoryFactory {

    @Autowired
    private static TodoItemRepository todoItemCacheRepository;

    private TodoItemRepositoryFactory() {
        // Private Constructor to Prevent Instantiation.
    }

    public static TodoItemRepository getInstance() {
        return todoItemCacheRepository;
    }

}
