package com.mashable.assignment.exception;

import javax.ws.rs.core.Response.Status;

/**
 * 
 * @author A
 * 
 */
public class ItemNotFoundException extends TodoApiException {

    private static final long serialVersionUID = 1L;

    public ItemNotFoundException(String errorMessage) {
        super(errorMessage, Status.NOT_FOUND);
    }

}
