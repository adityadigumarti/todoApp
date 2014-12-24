package com.mashable.assignment.resource;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mashable.assignment.search.service.ElasticSearchClientService;

/**
 * Search Resource for Todo.
 * 
 * 
 * @author Adi
 * 
 */
@RolesAllowed("todoAppUser")
@Path("todo/search")
public class TodoSearchResource {

    private static final Logger LOG = LoggerFactory.getLogger(TodoSearchResource.class);

    @Autowired
    private ElasticSearchClientService searchlyClientService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{searchString}")
    public String search(@PathParam("searchString") String searchString) {
        LOG.info("Searching for Search String " + searchString);

        return searchlyClientService.search(searchString);
    }

}
