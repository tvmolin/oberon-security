package com.oberon.exceptions;

import javax.ws.rs.ForbiddenException;

public class SubjectNotAllowedAccessToClientException extends ForbiddenException {

    public SubjectNotAllowedAccessToClientException() {
        super("This subject is not allowed to access this specific client");
    }
}
