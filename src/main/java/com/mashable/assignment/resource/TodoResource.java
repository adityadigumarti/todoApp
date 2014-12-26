package com.mashable.assignment.resource;

import java.util.Collection;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mashable.assignment.domain.TodoItem;
import com.mashable.assignment.exception.ItemNotFoundException;
import com.mashable.assignment.exception.TodoApiInternalError;
import com.mashable.assignment.repository.TodoItemRepository;
import com.mashable.assignment.search.service.ElasticSearchClientService;
import com.mashable.assignment.sms.service.SmsClientService;
import com.mashable.assignment.util.TodoAppUtil;

/**
 * Todo Resource.
 * 
 * All REST calls are handled here.
 * 
 * @author Adi
 * 
 */
@RolesAllowed("todoAppUser")
@Path("todo")
public class TodoResource {

    private static final Logger LOG = LoggerFactory.getLogger(TodoResource.class);

    // @Autowired
    // private TodoItemRepository todoItemRepository;

    @Autowired
    private TodoItemRepository mongoTodoItemRepository;

    @Autowired
    private TodoAppUtil todoAppUtil;

    @Autowired
    private ElasticSearchClientService searchlyClientService;

    @Autowired
    private SmsClientService twiloClientService;

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
        TodoItem todoItem = mongoTodoItemRepository.findById(id);

        if (todoItem == null) {
            throw new ItemNotFoundException("Item not Found for id " + id);
        }

        return todoItem;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<TodoItem> listTodoItems() {
        LOG.info("Logging - Inside Get ALL");

        return mongoTodoItemRepository.findAll();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public TodoItem createTodoItem(TodoItem todoItem, @Context final HttpServletResponse response) {
        LOG.info("Inside Create Todo Item - " + todoItem);

        todoAppUtil.validateCreateTodo(todoItem);

        mongoTodoItemRepository.insert(todoItem);

        // Since there are no transactions, we need to make sure we cleanup on exceptions.
        try {
            searchlyClientService.add(todoItem);
        } catch (Exception e) {
            LOG.error("Exception while creating serach index.", e);
            // Revert previous step
            mongoTodoItemRepository.delete(todoItem.getId());
            throw new TodoApiInternalError(e);
        }

        if (response != null) {
            response.setStatus(Response.Status.CREATED.getStatusCode());
        }

        return todoItem;
    }

    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/{id}")
    public void updateTodoItem(TodoItem todoItem, @PathParam("id") String id,
            @Context final HttpServletResponse response) {
        TodoItem originalTodo = mongoTodoItemRepository.findById(id);

        if (originalTodo == null) {
            throw new ItemNotFoundException("Item not Found for id " + id);
        }

        todoAppUtil.validateUpdateTodo(id, todoItem);

        mongoTodoItemRepository.update(id, todoItem);

        try {
            searchlyClientService.update(id, mongoTodoItemRepository.findById(id));
        } catch (Exception e) {
            LOG.error("Exception Updating in Searchly", e);
            // Revert DB Update
            mongoTodoItemRepository.update(id, originalTodo);
            throw new TodoApiInternalError(e);
        }

        response.setStatus(Response.Status.NO_CONTENT.getStatusCode());

    }

    @PUT
    @Path("/taskCompleted/{id}")
    public void updateTodoStatus(@PathParam("id") String id, @Context final HttpServletResponse response) {
        TodoItem todoItem = mongoTodoItemRepository.findById(id);

        if (todoItem == null) {
            throw new ItemNotFoundException("Item not Found for id " + id);
        }

        // Update only if task is currently still pending. Else just ignore.
        if (!todoItem.isDone()) {
            try {
                mongoTodoItemRepository.updateStatus(id, true);
                twiloClientService.sendText(todoAppUtil.getTaskCompletionMessage(todoItem.getTitle()),
                        todoAppUtil.getPhoneNumber());
            } catch (Exception e) {
                throw new TodoApiInternalError(e);
            }
        }

        response.setStatus(Response.Status.NO_CONTENT.getStatusCode());
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") String id, @Context final HttpServletResponse response) {
        TodoItem originalTodoItem = mongoTodoItemRepository.findById(id);

        if (originalTodoItem == null) {
            throw new ItemNotFoundException("Item not Found for id " + id);
        }

        LOG.info("Deleting TODO Item with id" + id);

        searchlyClientService.delete(id);

        try {
            mongoTodoItemRepository.delete(id);
        } catch (Exception e) {
            LOG.error("Exception deleting from DB", e);
            // Insert back into Search Index to keep it consistent.
            searchlyClientService.add(originalTodoItem);
            throw new TodoApiInternalError(e);
        }

        if (response != null) {
            response.setStatus(Response.Status.NO_CONTENT.getStatusCode());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/search/{searchString}")
    public String search(@PathParam("searchString") String searchString) {
        LOG.info("Searching for Search String " + searchString);

        return searchlyClientService.search(searchString);
    }

}
