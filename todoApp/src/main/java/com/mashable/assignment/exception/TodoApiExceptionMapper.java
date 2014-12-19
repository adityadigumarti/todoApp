package com.mashable.assignment.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.mashable.assignment.domain.ErrorMessage;

@Provider
public class TodoApiExceptionMapper implements ExceptionMapper<TodoApiException> {

    @Override
    public Response toResponse(TodoApiException exception) {
        ErrorMessage error = new ErrorMessage();

        error.setErrorMessage(exception.getMessage());
        error.setStatus(exception.getStatus().getStatusCode());

        return Response.status(exception.getStatus()).type(MediaType.APPLICATION_JSON).entity(error.toString()).build();
    }

}
