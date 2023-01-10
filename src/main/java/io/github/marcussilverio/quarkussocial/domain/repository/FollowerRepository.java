package io.github.marcussilverio.quarkussocial.domain.repository;

import io.github.marcussilverio.quarkussocial.domain.model.Follower;
import io.github.marcussilverio.quarkussocial.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository <Follower> {
  public boolean follows(User user, User follower){
    Map<String,Object> params = Parameters.with("follower",follower)
        .and("user", user).map();
    PanacheQuery<Follower> query =  find("follower =:follower and user=:user", params);
    return query.firstResultOptional().isPresent();
  }
  public List<Follower> findByUserId(Long userId){
    PanacheQuery<Follower> query = find("user.id", userId);
    return query.list();
  }

  public void deleteByFollowerAndUser (Long followerId, Long id) {
    var params = Parameters.with("userId", id)
        .and("followerId", followerId)
        .map();
    delete("follower.id =:followerId and user.id =:userId", params);
  }
}
