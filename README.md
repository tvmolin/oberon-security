#Oberon-security overview

The Base of this OAuth2 based system is the Subject and Client

The Subject, is the user, which wants to access a resource. 
The Client, is the application that provides that resource.

This service is meant to be called by another backend service, 
it should never be exposed to the frontend directly.

The authentication process goes as follows:

- The person interacts with the frontend to create a new user in the system;
- This requests gets processed by a service which will in turn make requests to the security service;
- The first request would be a POST to /subjects, containing all the data needed to create a new subject;
- If this request is successful, a new request should be made to /subjects/{subjectId}/link/{clientName};
- This request can be repeated many times, depending on how many clients that subject will be allowed to use;
- After creating the subject and linking the client to it the account activation is necessary (NOT IMPLEMENTED YET :/);
- After activating the account, a POST request can be done to /tokens, with the username and password;
- If the authentication goes well, a token will be returned, which can be used to access resources pertaining to the clients (and roles) that this subject is allowed;


Note that for testing purposes, we must manually insert new Client, Role, and client_role records. 
 