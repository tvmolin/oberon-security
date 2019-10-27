package com.oberon.exceptions;

import javax.ws.rs.ForbiddenException;

public class WrongCredentialsException extends ForbiddenException {

    public WrongCredentialsException() {
        super("Wrong credentials for the subject");
    }
}
