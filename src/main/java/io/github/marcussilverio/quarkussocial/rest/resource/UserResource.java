package io.github.marcussilverio.quarkussocial.rest.resource;

import io.github.marcussilverio.quarkussocial.domain.model.User;
import io.github.marcussilverio.quarkussocial.domain.repository.UserRepository;
import io.github.marcussilverio.quarkussocial.rest.dto.CreateUserRequest;
import io.github.marcussilverio.quarkussocial.rest.dto.ResponseError;
import io.github.marcussilverio.quarkussocial.rest.service.UserService;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
  @Inject
  UserService userService;
  @POST
  public Response createUser(CreateUserRequest userRequest){
    return userService.createUser(userRequest);
  }
  @GET
  public Response listAllUsers(){
   return userService.listAllUsers();
  }
  @DELETE
  @Path("{id}")
  public Response deleteUser(@PathParam("id") Long id){
    return userService.deleteUser(id);
  }
  @PUT
  @Path("{id}")
  public Response updateUser(@PathParam("id") Long id, CreateUserRequest userData){
    return userService.updateUser(id, userData);
  }
}
