package com.mashable.assignment.exception;

import javax.ws.rs.core.Response.Status;

/**
 * 
 * @author Adi
 * 
 */
public class TodoApiInternalError extends TodoApiException {

    private static final long serialVersionUID = 1L;

    public TodoApiInternalError(Throwable e) {
        super("Internal Error. Try Again", Status.INTERNAL_SERVER_ERROR, e);
    }

    public TodoApiInternalError(String errorMessage, Throwable e) {
        super(errorMessage, Status.INTERNAL_SERVER_ERROR, e);
    }

}
