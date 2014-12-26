package com.mashable.assignment.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mashable.assignment.domain.TodoItem;
import com.mashable.assignment.exception.BadRequestException;
import com.mashable.assignment.exception.ItemNotFoundException;
import com.mashable.assignment.repository.TodoItemRepository;

/**
 * Util class for all Util Methods
 * 
 * @author Adi
 * 
 */
@Component
public class TodoAppUtil {

    private static final Logger LOG = LoggerFactory.getLogger(TodoAppUtil.class);

    @Value("${default.phone.number}")
    private String toNumber;

    @Autowired
    private TodoItemRepository mongoTodoItemRepository;

    public void validateCreateTodo(TodoItem todoItem) {
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

    public void validateUpdateTodo(String id, TodoItem todoItem) {
        // If the Id's URL and Body don't match throw an exception. id is not required in Body, but if present should be
        // consistent with the URL.
        if (mongoTodoItemRepository.findById(id) == null) {
            throw new ItemNotFoundException("Item not Found for id " + id);
        }

        if (todoItem.getId() != null && !id.equals(todoItem.getId())) {
            throw new BadRequestException(String.format("Id fields %s and %s are not the same.", id, todoItem.getId()));
        }

        if (todoItem.getBody() == null && todoItem.getTitle() == null) {
            throw new BadRequestException("Atleast one of Body and Title is required.");
        }

    }

    public void isValidId(String id) {
        if (mongoTodoItemRepository.findById(id) == null) {
            throw new ItemNotFoundException("Item not Found for id " + id);
        }
    }

    public String getTaskCompletionMessage(String taskName) {
        return String.format("\"%s\" task has been marked as done.", taskName);
    }

    public String getPhoneNumber() {
        return toNumber;
    }

    public synchronized void updateToNumber(String number) {
        LOG.info(String.format("Updating the Phone Number for sending SMS Messages from %s to %s ", toNumber, number));

        toNumber = "+1" + number;
    }

    public String parseAndvalidatePhoneNumber(String phoneNumber) {
        String formattedPhoneNumber = phoneNumber.replaceAll("[\\D]", "");
        if (formattedPhoneNumber.charAt(0) == '1') {
            formattedPhoneNumber = formattedPhoneNumber.substring(1, formattedPhoneNumber.length());
        }

        if (formattedPhoneNumber.length() != 10) {
            throw new BadRequestException("Invalid Phone Number - " + phoneNumber);
        }

        return formattedPhoneNumber;
    }

}
