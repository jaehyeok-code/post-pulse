package com.project.common.domain.repository;

import com.project.common.domain.entity.Like;
import com.project.common.domain.entity.Post;
import com.project.common.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
  Optional<Like> findByUserAndPost(User user, Post post);
  long countByPost(Post post);
}
