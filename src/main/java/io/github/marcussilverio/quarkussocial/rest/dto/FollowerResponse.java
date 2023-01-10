package io.github.marcussilverio.quarkussocial.rest.dto;

import io.github.marcussilverio.quarkussocial.domain.model.Follower;
import lombok.Data;

@Data
public class FollowerResponse {
  private Long id;
  private String name;

  public FollowerResponse () {
  }
  public FollowerResponse (Follower follower) {
    this(follower.getFollower().getId(), follower.getFollower().getName());
  }
  public FollowerResponse (Long id, String name) {
    this.id = id;
    this.name= name;
  }
}
