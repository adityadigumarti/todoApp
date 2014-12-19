package com.mashable.assignment.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.mashable.assignment.searchly.SearchlyClient;

@Path("todo/search")
public class TodoSearchResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{searchString}")
    public String search(@PathParam("searchString") String searchString) {
        return SearchlyClient.search(searchString);
    }

}
