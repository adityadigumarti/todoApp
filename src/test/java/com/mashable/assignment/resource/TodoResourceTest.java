package com.mashable.assignment.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;

import com.mashable.assignment.domain.TodoItem;

public class TodoResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(TodoResource.class);
    }

    @Test
    public void createNewTodoItem() {
        boolean thrown = false;
        JSONObject todoItem = new JSONObject();

        try {
            todoItem.put("body", "Testing Todo Item").put("title", "Test").put("done", "false");
            target().path("todo").request().put(Entity.entity(todoItem, "application/json"));
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
    public void testGetIt() {
        final TodoItem responseMsg = target().path("todo/" + "549bc461e4b0a8f23e54d684").request().get(TodoItem.class);

        System.out.println(responseMsg);
    }

}
