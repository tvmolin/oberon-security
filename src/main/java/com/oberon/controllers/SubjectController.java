package com.oberon.controllers;

import com.oberon.business.bos.authentication.SubjectBO;
import com.oberon.business.dtos.SubjectDTO;
import com.oberon.exceptions.SubjectAlreadyLinkedToClientException;
import com.oberon.persistence.entities.authentication.Client;
import com.oberon.persistence.entities.authentication.Subject;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/subjects")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class SubjectController {

    private SubjectBO subjectBO;

    public SubjectController(SubjectBO subjectBO) {
        this.subjectBO = subjectBO;
    }

    @GET
    @Path("/{subjectId}")
    public Response getSubjectById(@PathParam("subjectId") Long subjectId) {
        return Response.status(Response.Status.OK).entity(subjectBO.getSubject(subjectId)).build();
    }

    @POST
    @Path("/{subjectId}/link/{clientName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response linkSubjectToClient(@PathParam("subjectId") Long subjectId, @PathParam("clientName") String clientName) throws SubjectAlreadyLinkedToClientException {
        subjectBO.linkClientToSubject(Subject.findById(subjectId), Client.findByName(clientName));
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSubject(SubjectDTO subjectDTO) throws Exception {
        return Response.status(Response.Status.CREATED).entity(subjectBO.createSubject(subjectDTO).getId()).build();
    }

}
