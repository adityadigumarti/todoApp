package com.mashable.assignment.resource;

import java.util.Collection;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mashable.assignment.domain.TodoItem;
import com.mashable.assignment.exception.ItemNotFoundException;
import com.mashable.assignment.repository.TodoItemRepository;
import com.mashable.assignment.repository.TodoItemRepositoryFactory;
import com.mashable.assignment.searchly.SearchlyClient;
import com.mashable.assignment.util.TodoAppUtil;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("todo")
public class TodoResource {

    private TodoItemRepository todoItemRepository;

    public TodoResource() {
        todoItemRepository = TodoItemRepositoryFactory.getInstance();
    }

    /**
     * Mapped to the Http Get Method
     * Returns a TodoItem resource in Json format for the Id sent in the URL
     * 
     * @param id
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public TodoItem getTodoItem(@PathParam("id") String id) {
        TodoAppUtil.isValidId(id);

        return todoItemRepository.findById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<TodoItem> listTodoItems() {
        return todoItemRepository.findAll();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public TodoItem createTodoItem(TodoItem todoItem, @Context final HttpServletResponse response) {
        TodoAppUtil.validateCreateTodo(todoItem);

        SearchlyClient.add(todoItem);
        todoItemRepository.insert(todoItem);

        response.setStatus(Response.Status.CREATED.getStatusCode());
        return todoItem;
    }

    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/{id}")
    public TodoItem updateTodoItem(TodoItem todoItem, @PathParam("id") String id,
            @Context final HttpServletResponse response) {
        TodoAppUtil.validateUpdateTodo(id, todoItem);

        todoItemRepository.insert(todoItem);
        response.setStatus(Response.Status.NO_CONTENT.getStatusCode());
        return todoItem;
    }

    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/status/{id}/{value}")
    public TodoItem updateTodoStatus(@PathParam("id") String id, @PathParam("value") boolean value,
            @Context final HttpServletResponse response) {
        TodoItem todoItem = todoItemRepository.findById(id);

        if (todoItem == null) {
            throw new ItemNotFoundException("Item not Found for id " + id);
        }

        todoItem.setDone(value);
        response.setStatus(Response.Status.NO_CONTENT.getStatusCode());

        return todoItem;
    }

    public void delete(@PathParam("id") String id, @Context final HttpServletResponse response) {
        if (todoItemRepository.findById(id) == null) {
            throw new ItemNotFoundException("Item not Found for id " + id);
        }

        todoItemRepository.delete(id);
    }
}
