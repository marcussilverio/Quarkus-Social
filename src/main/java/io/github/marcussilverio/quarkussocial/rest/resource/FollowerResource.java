package io.github.marcussilverio.quarkussocial.rest.resource;

import io.github.marcussilverio.quarkussocial.domain.model.Follower;
import io.github.marcussilverio.quarkussocial.domain.model.User;
import io.github.marcussilverio.quarkussocial.domain.repository.FollowerRepository;
import io.github.marcussilverio.quarkussocial.domain.repository.UserRepository;
import io.github.marcussilverio.quarkussocial.rest.dto.FollowRequest;
import io.github.marcussilverio.quarkussocial.rest.dto.FollowerResponse;
import io.github.marcussilverio.quarkussocial.rest.dto.FollowersPerUserResponse;
import io.github.marcussilverio.quarkussocial.rest.service.FollowerService;

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
  FollowerService followerService;

  @PUT
  public Response followUser(@PathParam("user_id") Long id,  FollowRequest request){
    return followerService.followUser(id,request);
  }
  @GET
  public Response listFollowers (@PathParam("user_id") Long id){
    return followerService.listFollowers(id);
  }
  @DELETE
  public Response unfollow(@PathParam("user_id") Long id, @QueryParam("followerId") Long followerId){
    return followerService.unfollow(id, followerId);
  }
}
