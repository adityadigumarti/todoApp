package com.mashable.assignment.resource;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.mashable.assignment.domain.TodoItem;
import com.mashable.assignment.util.TodoAppUtil;

/**
 * Resource for all Configuration changes.
 * Phone Number can be updated here.
 * 
 * @author Adi
 * 
 */
@RolesAllowed("todoAppUser")
@Path("todo/configure")
public class TodoConfigurationResource {

    @Autowired
    private TodoAppUtil todoAppUtil;

    @PUT
    @Path("/{phoneNumber}")
    public void updateTodoItem(TodoItem todoItem, @PathParam("phoneNumber") String phoneNumber,
            @Context final HttpServletResponse response) {
        todoAppUtil.parseAndvalidatePhoneNumber(phoneNumber);
        todoAppUtil.updateToNumber(phoneNumber);
        response.setStatus(Response.Status.NO_CONTENT.getStatusCode());
    }

}
