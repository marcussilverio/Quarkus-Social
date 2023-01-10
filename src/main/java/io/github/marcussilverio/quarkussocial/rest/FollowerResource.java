package io.github.marcussilverio.quarkussocial.rest;

import io.github.marcussilverio.quarkussocial.domain.model.Follower;
import io.github.marcussilverio.quarkussocial.domain.model.User;
import io.github.marcussilverio.quarkussocial.domain.repository.FollowerRepository;
import io.github.marcussilverio.quarkussocial.domain.repository.UserRepository;
import io.github.marcussilverio.quarkussocial.rest.dto.FollowRequest;
import io.github.marcussilverio.quarkussocial.rest.dto.FollowerResponse;
import io.github.marcussilverio.quarkussocial.rest.dto.FollowersPerUserResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/users/{user_id}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {
  @Inject
  UserRepository userRepository;
  @Inject
  FollowerRepository followerRepository;

  @PUT
  @Transactional
  public Response followUser(@PathParam("user_id") Long id,  FollowRequest request){
    if(id.equals(request.getFollowerId())){
      return Response.status(Response.Status.CONFLICT).entity("You can't follow yourself").build();
    }
    User user = userRepository.findById(id);
    User follower = userRepository.findById(request.getFollowerId());
    if(user == null || follower == null){
      return Response.status(Response.Status.NOT_FOUND).build();
    }
      Follower entity = new Follower();
    if (!followerRepository.follows(user,follower)) {
      entity.setUser(user);
      entity.setFollower(follower);
      followerRepository.persist(entity);
    }

    return Response.status(Response.Status.CREATED).entity(entity).build();
  }
  @GET
  public Response listFollowers (@PathParam("user_id") Long id){
    User user = userRepository.findById(id);
    if(user == null){
      return Response.status(Response.Status.NOT_FOUND).build();
    }

    List<Follower> list = followerRepository.findByUserId(id);
    FollowersPerUserResponse responseObject = new FollowersPerUserResponse();
    responseObject.setFollowersCount(list.size());

    List<FollowerResponse> followerList = list.stream().map(FollowerResponse::new).collect(Collectors.toList());

    responseObject.setFollowers(followerList);
    return Response.ok(responseObject).build();
  }
  @Transactional
  @DELETE
  public Response unfollow(@PathParam("user_id") Long id, @QueryParam("followerId") Long followerId){
    User user = userRepository.findById(id);
    if(user == null){
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    followerRepository.deleteByFollowerAndUser(followerId, id);
    return Response.noContent().build();
  }
}
