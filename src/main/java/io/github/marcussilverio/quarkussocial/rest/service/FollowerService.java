package io.github.marcussilverio.quarkussocial.rest.service;

import io.github.marcussilverio.quarkussocial.domain.model.Follower;
import io.github.marcussilverio.quarkussocial.domain.model.User;
import io.github.marcussilverio.quarkussocial.domain.repository.FollowerRepository;
import io.github.marcussilverio.quarkussocial.domain.repository.UserRepository;
import io.github.marcussilverio.quarkussocial.rest.dto.FollowRequest;
import io.github.marcussilverio.quarkussocial.rest.dto.FollowerResponse;
import io.github.marcussilverio.quarkussocial.rest.dto.FollowersPerUserResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class FollowerService {
  @Inject
  UserRepository userRepository;
  @Inject
  FollowerRepository followerRepository;
  @Transactional
  public Response followUser (Long id, FollowRequest request) {
    if (id.equals(request.getFollowerId())) {
      return Response.status(Response.Status.CONFLICT).entity("You can't follow yourself").build();
    }
    User user = userRepository.findById(id);
    User follower = userRepository.findById(request.getFollowerId());
    if (user == null || follower == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    Follower entity = new Follower();
    if (!followerRepository.follows(user, follower)) {
      entity.setUser(user);
      entity.setFollower(follower);
      followerRepository.persist(entity);
    }
    return Response.status(Response.Status.CREATED).entity(entity).build();
  }
  public Response listFollowers (Long id){
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
  public Response unfollow(Long id,Long followerId){
    User user = userRepository.findById(id);
    if(user == null){
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    followerRepository.deleteByFollowerAndUser(followerId, id);
    return Response.noContent().build();
  }
}
