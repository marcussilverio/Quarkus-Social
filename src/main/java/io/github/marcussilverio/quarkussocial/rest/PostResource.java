package io.github.marcussilverio.quarkussocial.rest;

import io.github.marcussilverio.quarkussocial.domain.model.Post;
import io.github.marcussilverio.quarkussocial.domain.model.User;
import io.github.marcussilverio.quarkussocial.domain.repository.FollowerRepository;
import io.github.marcussilverio.quarkussocial.domain.repository.PostRepository;
import io.github.marcussilverio.quarkussocial.domain.repository.UserRepository;
import io.github.marcussilverio.quarkussocial.rest.dto.CreatePostRequest;
import io.github.marcussilverio.quarkussocial.rest.dto.PostResponse;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {
  @Inject
  UserRepository userRepository;
  @Inject
  PostRepository postRepository;
  @Inject
  FollowerRepository followerRepository;
  @POST
  @Transactional
  public Response savePost(
      @PathParam("userId") Long userId,
      CreatePostRequest request){
    User user = userRepository.findById(userId);
    if(user == null){
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    Post post = new Post();
    post.setText(request.getText());
    post.setUser(user);

    postRepository.persist(post);


    return Response.status(Response.Status.CREATED)
        .entity(post)
        .build();

  }
  @GET
  public Response listPosts(@PathParam("userId") Long userId,@HeaderParam("followerId") Long followerId){
    User user = userRepository.findById(userId);

    if(user == null ){
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    if(followerId == null)
      return Response.status(Response.Status.BAD_REQUEST).entity("followerId is required").build();
    User follower = userRepository.findById(followerId);

    if(!followerRepository.follows(user, follower)){
      return  Response.status(Response.Status.FORBIDDEN).build();
    }
    PanacheQuery<Post> query = postRepository
        .find("user", Sort.by("dateTime",
            Sort.Direction.Descending), user);
    List<PostResponse> responseList = query.list()
        .stream()
        .map(PostResponse::fromEntity)
        .collect(Collectors.toList());
    return Response.ok(responseList).build();
  }
  @DELETE
  @Path("{postId}")
  @Transactional
  public Response removePost(@PathParam("userId") Long userId, @PathParam("postId") Long id){
    User user = userRepository.findById(userId);
    if(user == null){
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    Post post = postRepository.findById(id);

    if(post == null){
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    postRepository.delete(post);
    return Response.noContent().build();
  }
  @PUT
  @Path("{postId}")
  @Transactional
  public Response editPost(@PathParam("userId") Long userId, @PathParam("postId") Long id, CreatePostRequest request){
    User user = userRepository.findById(userId);
    if(user == null){
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    Post post = postRepository.findById(id);

    if(post == null){
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    post.setText(request.getText());
    post.prePersist();
    return Response.noContent().build();
  }
}
