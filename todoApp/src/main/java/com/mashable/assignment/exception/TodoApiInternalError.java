package com.mashable.assignment.exception;

import javax.ws.rs.core.Response.Status;

public class TodoApiInternalError extends TodoApiException {

    private static final long serialVersionUID = 1L;

    public TodoApiInternalError() {
        super("Internal Error. Try Again", Status.INTERNAL_SERVER_ERROR);
    }

}
