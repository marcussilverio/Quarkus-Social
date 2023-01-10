package io.github.marcussilverio.quarkussocial.rest.dto;

import io.github.marcussilverio.quarkussocial.domain.model.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponse {
  private Long id;
  private String text;
  private LocalDateTime dateTime;
  public static PostResponse fromEntity(Post post){
    PostResponse response = new PostResponse();
    response.setId(post.getId());
    response.setText(post.getText());
    response.setDateTime(post.getDateTime());
    return response;
  }

}
