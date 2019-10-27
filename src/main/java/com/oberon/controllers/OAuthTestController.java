package com.oberon.controllers;

import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

@Path("/auth")
@RequestScoped
public class OAuthTestController {

    private JsonWebToken jwt;

    public OAuthTestController(JsonWebToken jwt) {
        this.jwt = jwt;
    }

    @GET
    @Path("/roles-allowed")
    @RolesAllowed({"Echoer", "Subscriber", "test"})
    @Produces(MediaType.TEXT_PLAIN)
    public String helloRolesAllowed(@Context SecurityContext ctx) {
        Principal caller = ctx.getUserPrincipal();
        String name = caller == null ? "anonymous" : caller.getName();
        boolean hasJWT = jwt != null;
        String helloReply = String.format("hello %s, isHttps: %s, authScheme: %s, hasJWT: %s", name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJWT);
        return helloReply;
    }

}