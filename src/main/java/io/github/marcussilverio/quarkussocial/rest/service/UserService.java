package io.github.marcussilverio.quarkussocial.rest.service;

import io.github.marcussilverio.quarkussocial.domain.model.User;
import io.github.marcussilverio.quarkussocial.domain.repository.UserRepository;
import io.github.marcussilverio.quarkussocial.rest.dto.CreateUserRequest;
import io.github.marcussilverio.quarkussocial.rest.dto.ResponseError;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.Set;

@ApplicationScoped
public class UserService {
  @Inject
  UserRepository userRepository;
  @Inject
  Validator validator;
  @Transactional
  public Response createUser(CreateUserRequest userRequest){
    Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(userRequest);
    if(!violations.isEmpty())
      return  ResponseError.createFromValidation(violations).withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);

    User user = new User();
    user.setAge(userRequest.getAge());
    user.setName(userRequest.getName());
    userRepository.persist(user);

    return Response.status(Response.Status.CREATED).entity(user).build();
  }
  public Response listAllUsers(){
    PanacheQuery<User> query = userRepository.findAll();
    return Response.ok(query.list()).build();
  }

  @Transactional
  public Response deleteUser(Long id){
    User user = userRepository.findById(id);
    if(user == null)
      return Response.status(Response.Status.NOT_FOUND).build();

    userRepository.delete(user);
    return Response.noContent().build();
  }

  @Transactional
  public Response updateUser(Long id, CreateUserRequest userData){
    User user = userRepository.findById(id);
    if(user == null)
      return Response.status(Response.Status.NOT_FOUND).build();

    user.setName(userData.getName());
    user.setAge(userData.getAge());
    return Response.noContent().build();
  }
}
