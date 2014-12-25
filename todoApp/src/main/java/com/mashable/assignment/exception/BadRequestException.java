package com.mashable.assignment.exception;

import java.util.List;

import javax.ws.rs.core.Response.Status;

/**
 * 
 * 
 * @author Adi
 * 
 */
public class BadRequestException extends TodoApiException {

    private static final long serialVersionUID = 1L;

    public BadRequestException(List<String> errors) {
        super(getErrorMessage(errors), Status.BAD_REQUEST);
    }

    public BadRequestException(String error) {
        super(error, Status.BAD_REQUEST);
    }

}
