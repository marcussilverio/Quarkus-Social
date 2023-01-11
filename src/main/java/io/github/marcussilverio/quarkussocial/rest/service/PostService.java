package io.github.marcussilverio.quarkussocial.rest.service;

import io.github.marcussilverio.quarkussocial.domain.model.Post;
import io.github.marcussilverio.quarkussocial.domain.model.User;
import io.github.marcussilverio.quarkussocial.domain.repository.FollowerRepository;
import io.github.marcussilverio.quarkussocial.domain.repository.PostRepository;
import io.github.marcussilverio.quarkussocial.domain.repository.UserRepository;
import io.github.marcussilverio.quarkussocial.rest.dto.CreatePostRequest;
import io.github.marcussilverio.quarkussocial.rest.dto.PostResponse;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PostService {
  @Inject
  UserRepository userRepository;
  @Inject
  PostRepository postRepository;
  @Inject
  FollowerRepository followerRepository;

  @Transactional
  public Response savePost(Long userId, CreatePostRequest request){
    User user = userRepository.findById(userId);
    if(user == null)
      return Response.status(Response.Status.NOT_FOUND).build();

    Post post = new Post();
    post.setText(request.getText());
    post.setUser(user);

    postRepository.persist(post);

    return Response.status(Response.Status.CREATED).entity(post).build();
  }
  public Response listPosts(Long userId, Long followerId){
    User user = userRepository.findById(userId);

    if(user == null )
      return Response.status(Response.Status.NOT_FOUND).build();

    if(followerId == null)
      return Response.status(Response.Status.BAD_REQUEST).entity("followerId is required").build();

    User follower = userRepository.findById(followerId);
    if(!followerRepository.follows(user, follower))
      return  Response.status(Response.Status.FORBIDDEN).build();

    PanacheQuery<Post> query = postRepository
        .find("user", Sort.by("dateTime",
            Sort.Direction.Descending), user);
    List<PostResponse> responseList = query.list()
        .stream()
        .map(PostResponse::fromEntity)
        .collect(Collectors.toList());
    return Response.ok(responseList).build();
  }
  @Transactional
  public Response removePost(@PathParam("userId") Long userId, @PathParam("postId") Long id){
    User user = userRepository.findById(userId);
    if(user == null)
      return Response.status(Response.Status.NOT_FOUND).build();

    Post post = postRepository.findById(id);
    if(post == null)
      return Response.status(Response.Status.NOT_FOUND).build();

    postRepository.delete(post);
    return Response.noContent().build();
  }
  @Transactional
  public Response editPost(Long userId, Long id, CreatePostRequest request){
    User user = userRepository.findById(userId);
    if(user == null)
      return Response.status(Response.Status.NOT_FOUND).build();

    Post post = postRepository.findById(id);
    if(post == null)
      return Response.status(Response.Status.NOT_FOUND).build();

    post.setText(request.getText());
    post.prePersist();
    return Response.noContent().build();
  }
}
