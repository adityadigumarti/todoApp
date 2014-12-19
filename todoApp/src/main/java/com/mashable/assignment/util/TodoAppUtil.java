package com.mashable.assignment.util;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.mashable.assignment.domain.TodoItem;
import com.mashable.assignment.exception.BadRequestException;
import com.mashable.assignment.exception.ItemNotFoundException;
import com.mashable.assignment.repository.TodoItemRepository;
import com.mashable.assignment.repository.TodoItemRepositoryFactory;

public class TodoAppUtil {

    private static final TimeBasedGenerator gen = Generators.timeBasedGenerator(EthernetAddress.fromInterface());
    private static TodoItemRepository todoItemRepository = TodoItemRepositoryFactory.getInstance();

    public static String generateUniqueId() {
        return gen.generate().toString();
    }

    public static void validateTodo(TodoItem todoItem) {

    }

    public static void validateCreateTodo(TodoItem todoItem) {
        List<String> errors = null;

        if (todoItem.getBody() == null || todoItem.getTitle() == null) {
            errors = new ArrayList<String>();

            if (todoItem.getBody() == null) {
                errors.add("Todo Body is required.");
            }

            if (todoItem.getTitle() == null) {
                errors.add("Todo Title is required.");
            }

            throw new BadRequestException(errors);
        }

    }

    public static void validateUpdateTodo(String id, TodoItem todoItem) {
        // If the Id's URL and Body don't match throw an exception. id is not required in Body, but if present should be
        // consistent with the URL.
        if (todoItemRepository.findById(id) == null) {
            throw new ItemNotFoundException("Item not Found for id " + id);
        }

        if (todoItem.getId() != null && !id.equals(todoItem.getId())) {
            throw new BadRequestException(String.format("Id fields %s and %s are not the same.", id, todoItem.getId()));
        }

        if (todoItem.getBody() == null && todoItem.getTitle() == null) {
            throw new BadRequestException("Atleast one of Body and Title is required.");
        }

    }

    public static void isValidId(String id) {
        if (todoItemRepository.findById(id) == null) {
            throw new ItemNotFoundException("Item not Found for id " + id);
        }
    }
}
