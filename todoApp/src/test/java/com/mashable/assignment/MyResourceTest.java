package com.mashable.assignment;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Application;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import com.mashable.assignment.resource.TodoResource;


public class MyResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(TodoResource.class);
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void testGetIt() {
        final String responseMsg = target().path("myresource").request().get(String.class);

        System.out.println(responseMsg);
        assertEquals("Hello, Heroku!", responseMsg);
    }
}
