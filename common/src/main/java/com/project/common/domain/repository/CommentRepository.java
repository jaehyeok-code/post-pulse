package com.project.common.domain.repository;

import com.project.common.domain.entity.Comment;
import com.project.common.domain.entity.Post;
import com.project.common.domain.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  List<Comment> findByPostOrderByCreatedAtDesc(Post post);

  Optional<Comment> findByIdAndUser(Long id, User user);

}
