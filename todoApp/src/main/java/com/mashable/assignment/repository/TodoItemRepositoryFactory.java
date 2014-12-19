package com.mashable.assignment.repository;

public class TodoItemRepositoryFactory {

    private static TodoItemRepository todoItemRepository = new TodoItemCacheRepository();

    private TodoItemRepositoryFactory() {
        // Private Constructor to Prevent Instantiation.
    }

    public static TodoItemRepository getInstance() {
        return todoItemRepository;
    }

}
