package com.oberon.controllers;

import com.oberon.business.bos.authentication.TokenBO;
import com.oberon.business.dtos.TokenDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/tokens")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TokenController {

    private TokenBO tokenBO;

    public TokenController(TokenBO tokenBO) {
        this.tokenBO = tokenBO;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createToken(TokenDTO tokenDTO) throws Exception {
        return Response.status(Response.Status.CREATED).entity(tokenBO.createToken(tokenDTO).getTokenString()).build();
    }
}
