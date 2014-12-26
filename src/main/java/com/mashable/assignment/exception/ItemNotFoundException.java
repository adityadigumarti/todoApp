package com.mashable.assignment.exception;

import javax.ws.rs.core.Response.Status;

/**
 * Exception thrown if id specified is not available.
 * 
 * @author Adi
 * 
 */
public class ItemNotFoundException extends TodoApiException {

    private static final long serialVersionUID = 1L;

    public ItemNotFoundException(String errorMessage, Throwable e) {
        super(errorMessage, Status.NOT_FOUND, e);
    }

    public ItemNotFoundException(String errorMessage) {
        super(errorMessage, Status.NOT_FOUND);
    }

}
