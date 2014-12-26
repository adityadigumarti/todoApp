package com.mashable.assignment.domain;

/**
 * 
 * @author Adi
 * 
 */
public class ErrorMessage {

    private int status;
    private String errorMessage;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return String.format("{status : %d, errorMessage : %s }", status, errorMessage);
    }

}
