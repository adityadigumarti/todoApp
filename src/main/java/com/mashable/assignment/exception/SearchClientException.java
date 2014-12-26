package com.mashable.assignment.exception;

import javax.ws.rs.core.Response.Status;

/**
 * 
 * 
 * @author Adi
 * 
 */
public class SearchClientException extends TodoApiException {

    private static final long serialVersionUID = 1L;

    public SearchClientException(String errorMessage, Throwable e) {
        super(errorMessage, Status.INTERNAL_SERVER_ERROR, e);
    }

}
