package com.mashable.assignment.resource;

import static org.junit.Assert.*;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.util.UUID;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.mashable.assignment.domain.TodoItem;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TodoResourceTest extends JerseyTest {

    private static String id = null;

    private static String body;
    private static String title;

    private static String updatedBody;
    private static String updatedTitle;

    @BeforeClass
    public static void setUpClass() {
        body = UUID.randomUUID().toString();
        title = UUID.randomUUID().toString();
        updatedBody = UUID.randomUUID().toString();
        updatedTitle = UUID.randomUUID().toString();
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(TodoResource.class);
    }

    @Test
    public void step1_createNewTodoItem() {
        enable(TestProperties.LOG_TRAFFIC);

        boolean thrown = false;
        TodoItem todoItem = new TodoItem();

        todoItem.setBody(body);
        todoItem.setTitle(title);

        try {
            Response response = target().path("todo").request().post(Entity.entity(todoItem, "application/json"));

            TodoItem todoItemResponse = response.readEntity(TodoItem.class);

            assertNotNull(todoItemResponse.getId());

            id = todoItemResponse.getId();
        } catch (Exception e) {
            e.printStackTrace();
            thrown = true;
        }

        assertFalse(thrown);
    }

    @Test
    public void step2_searchTodoItem() {
        enable(TestProperties.LOG_TRAFFIC);

        boolean thrown = false;

        try {
            String response = target().path("todo/search/" + title).request().get(String.class);

            System.out.println(response);

            assertNotEquals(-1, response.indexOf(title));

        } catch (Exception e) {
            e.printStackTrace();
            thrown = true;
        }

        assertFalse(thrown);
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void step3_testGetIt() {
        final TodoItem todoItem = target().path("todo/" + id).request().get(TodoItem.class);

        assertEquals(title, todoItem.getTitle());
        assertEquals(body, todoItem.getBody());
    }

    @Test
    public void step4_updateTodoItem_body() {
        enable(TestProperties.LOG_TRAFFIC);

        boolean thrown = false;
        TodoItem todoItem = new TodoItem();

        todoItem.setBody(updatedBody);

        try {
            Response response = target().path("todo/" + id).request().put(Entity.entity(todoItem, "application/json"));

            final TodoItem todoItemUpdated = target().path("todo/" + id).request().get(TodoItem.class);

            assertEquals(title, todoItemUpdated.getTitle());
            assertEquals(updatedBody, todoItemUpdated.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            thrown = true;
        }

        assertFalse(thrown);
    }

    @Test
    public void step5_updateTodoItem_title() {
        enable(TestProperties.LOG_TRAFFIC);

        boolean thrown = false;
        TodoItem todoItem = new TodoItem();

        todoItem.setBody(body);
        todoItem.setTitle(updatedTitle);

        try {
            Response response = target().path("todo/" + id).request().put(Entity.entity(todoItem, "application/json"));

            final TodoItem todoItemUpdated = target().path("todo/" + id).request().get(TodoItem.class);

            assertEquals(updatedTitle, todoItemUpdated.getTitle());
            assertEquals(body, todoItemUpdated.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            thrown = true;
        }

        assertFalse(thrown);
    }

    @Test
    public void step6_updateTodoItem_status() {
        enable(TestProperties.LOG_TRAFFIC);

        boolean thrown = false;

        try {
            Response response =
                    target().path("todo/taskCompleted/" + id).request().put(Entity.entity("", "application/json"));

            final TodoItem todoItemUpdated = target().path("todo/" + id).request().get(TodoItem.class);

            assertEquals(true, todoItemUpdated.isDone());

        } catch (Exception e) {
            e.printStackTrace();
            thrown = true;
        }

        assertFalse(thrown);
    }

    @Test
    public void step7_deleteTodoItem() {
        enable(TestProperties.LOG_TRAFFIC);

        boolean thrown = false;

        try {
            Response response = target().path("todo/" + id).request().delete();

            assertEquals(204, response.getStatus());

            response = target().path("todo/" + id).request().get();
            assertEquals(404, response.getStatus());

        } catch (Exception e) {
            e.printStackTrace();
            thrown = true;
        }

        assertFalse(thrown);
    }

}
