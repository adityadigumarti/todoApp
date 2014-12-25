package com.mashable.assignment.exception;

import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

/**
 * Base class for all TODO API Exceptions.
 * 
 * 
 * @author Adi
 * 
 */
public class TodoApiException extends WebApplicationException {

    static final long serialVersionUID = 1L;
    private Status status;

    public TodoApiException(String errorMessage, Status status) {
        super(errorMessage);
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    protected static String getErrorMessage(List<String> errors) {
        StringBuilder sb = new StringBuilder();

        if (errors.size() == 1) {
            sb.append(errors.get(0));
        } else {
            int i = 0;
            for (; i < errors.size() - 1; i++) {
                sb.append(errors.get(i) + " ,");
            }

            sb.append(errors.get(i));
        }

        return sb.toString();

    }

}
