package com.mashable.assignment.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mashable.assignment.searchly.SearchlyClient;

@Path("todo/search")
public class TodoSearchResource {

    private final static Logger LOG = LoggerFactory.getLogger(TodoSearchResource.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{searchString}")
    public String search(@PathParam("searchString") String searchString) {
        LOG.info("Searching for Search String " + searchString);

        return SearchlyClient.search(searchString);
    }

}
