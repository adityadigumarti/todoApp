package com.mashable.assignment.exception;

import java.util.List;

import javax.ws.rs.core.Response.Status;

/**
 * Exception thrown if the Request data is Invalid.
 * 
 * @author Adi
 * 
 */
public class BadRequestException extends TodoApiException {

    private static final long serialVersionUID = 1L;

    public BadRequestException(List<String> errors, Throwable e) {
        super(getErrorMessage(errors), Status.BAD_REQUEST, e);
    }

    public BadRequestException(List<String> errors) {
        super(getErrorMessage(errors), Status.BAD_REQUEST);
    }

    public BadRequestException(String error, Throwable e) {
        super(error, Status.BAD_REQUEST, e);
    }

    public BadRequestException(String error) {
        super(error, Status.BAD_REQUEST);
    }

}
