package com.oberon.controllers;

import com.oberon.business.bos.TokenBO;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@RequestScoped
public class OAuthController {

    private JsonWebToken jwt;
    private TokenBO tokenBO;

    @GET
    @Path("/roles-allowed")
    @RolesAllowed({"Echoer", "Subscriber"})
    @Produces(MediaType.TEXT_PLAIN)
    public String helloRolesAllowed(@Context SecurityContext ctx) {
        Principal caller = ctx.getUserPrincipal();
        String name = caller == null ? "anonymous" : caller.getName();
        boolean hasJWT = jwt != null;
        String helloReply = String.format("hello + %s, isSecure: %s, authScheme: %s, hasJWT: %s", name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJWT);
        return helloReply;
    }

    @GET
    @Path("/token")
    @Produces(MediaType.TEXT_PLAIN)
    public String generateToken() throws Exception {
        String claimsJson = "/JwtClaims.json";
        return tokenBO.generateTokenString(claimsJson);
    }
}