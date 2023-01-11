package io.github.marcussilverio.quarkussocial.rest.resource;

import io.github.marcussilverio.quarkussocial.domain.model.Post;
import io.github.marcussilverio.quarkussocial.domain.model.User;
import io.github.marcussilverio.quarkussocial.domain.repository.FollowerRepository;
import io.github.marcussilverio.quarkussocial.domain.repository.PostRepository;
import io.github.marcussilverio.quarkussocial.domain.repository.UserRepository;
import io.github.marcussilverio.quarkussocial.rest.dto.CreatePostRequest;
import io.github.marcussilverio.quarkussocial.rest.dto.PostResponse;
import io.github.marcussilverio.quarkussocial.rest.service.PostService;
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
  PostService postService;

  @POST
  public Response savePost(@PathParam("userId") Long userId,CreatePostRequest request){
    return postService.savePost(userId, request);
  }
  @GET
  public Response listPosts(@PathParam("userId") Long userId,@HeaderParam("followerId") Long followerId){
    return postService.listPosts(userId,followerId);
  }
  @DELETE
  @Path("{postId}")
  public Response removePost(@PathParam("userId") Long userId, @PathParam("postId") Long id){
    return postService.removePost(userId, id);
  }
  @PUT
  @Path("{postId}")
  public Response editPost(@PathParam("userId") Long userId, @PathParam("postId") Long id, CreatePostRequest request){
    return postService.editPost(userId, id, request);
  }
}
