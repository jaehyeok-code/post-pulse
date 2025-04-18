package com.project.post.service;

import com.project.common.UserVo;
import com.project.common.config.JwtAuthenticationProvider;
import com.project.common.domain.dto.PostEvent;
import com.project.common.domain.dto.PostRequest;
import com.project.common.domain.entity.Post;
import com.project.common.domain.entity.PostEventType;
import com.project.common.domain.entity.User;
import com.project.common.domain.repository.PostRepository;
import com.project.common.domain.repository.UserRepository;
import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import com.project.common.service.S3Service;
import com.project.post.search.PostSearchService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final JwtAuthenticationProvider jwtAuthenticationProvider;
  private final S3Service s3Service;
  private final UserRepository userRepository;
  private final PostSearchService postSearchService; // -> (ElasticSearch)
  //Kafka 와 Redis 인기게시글 적용
  private final RedisTemplate<String, String> redisTemplate;
  private final KafkaTemplate<String, PostEvent> kafkaTemplate;

  //게시글 작성
  @Transactional
  public Post createPost(String token, PostRequest request, MultipartFile file) {

    if (!jwtAuthenticationProvider.validateToken(token)) {
      throw new CustomException(ErrorCode.LOGIN_CHECK_FAIL);
    }

    UserVo userVo = jwtAuthenticationProvider.getUserVo(token);

    User user = userRepository.findById(userVo.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    String imageUrl = null;
    if (file != null && !file.isEmpty()) {
      try {
        imageUrl = s3Service.uploadImage(file);
      } catch (Exception e) {
        throw new CustomException(ErrorCode.FILE_UPLOAD_FAIL);
      }
    }

    Post post = Post.builder()
        .user(user)
        .title(request.getTitle())
        .content(request.getContent())
        .imageUrl(imageUrl)
        .build();

    Post saved= postRepository.save(post);
    postSearchService.indexPost(saved); // 비동기 처리 진행
    return saved;
  }

  //게시글 수정
  @Transactional
  public Post updatePost(String token, Long postId, PostRequest request, MultipartFile file) {

    if (!jwtAuthenticationProvider.validateToken(token)) {
      throw new CustomException(ErrorCode.LOGIN_CHECK_FAIL);
    }
    UserVo userVo = jwtAuthenticationProvider.getUserVo(token);

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

    if (!post.getUser().getId().equals(userVo.getId()))  {
      throw new CustomException(ErrorCode.UNAUTHORIZED_UPDATE);
    }

    if (request.getTitle() != null) {
      post.setTitle(request.getTitle());
    }
    if (request.getContent() != null) {
      post.setContent(request.getContent());
    }

    if (file != null && !file.isEmpty()) {
      try {
        String imageUrl = s3Service.uploadImage(file);
        post.setImageUrl(imageUrl);
      } catch (Exception e) {
        throw new CustomException(ErrorCode.FILE_UPLOAD_FAIL);
      }
    }

    return postRepository.save(post);
  }

  //게시글 삭제
  @Transactional
  public void deletePost(String token, Long postId) {

    if (!jwtAuthenticationProvider.validateToken(token)) {
      throw new CustomException(ErrorCode.LOGIN_CHECK_FAIL);
    }
    UserVo userVo = jwtAuthenticationProvider.getUserVo(token);

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

    if (!post.getUser().getId().equals(userVo.getId()))  {
      throw new CustomException(ErrorCode.UNAUTHORIZED_DELETE);
    }

    postRepository.delete(post);
  }


  //게시글 상세 조회
  public Post getPostDetail(String token, Long postId) {

    if (!jwtAuthenticationProvider.validateToken(token)) {
      throw new CustomException(ErrorCode.LOGIN_CHECK_FAIL);
    }

    Long userId = jwtAuthenticationProvider.getUserVo(token).getId();

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

    post.setViews(post.getViews() == null ? 1 : post.getViews() + 1);
    postRepository.save(post);

    // 중복 조회 방지 및 Kafka 발행
    String viewedKey = "post:viewed:" + postId;

    // add()가 1을 반환하면 처음 조회한것!
    if (redisTemplate.opsForSet().add(viewedKey, userId.toString()) == 1) {
      PostEvent evt = new PostEvent(postId, userId, PostEventType.VIEW, System.currentTimeMillis());
      kafkaTemplate.send("post-events", evt);
    }

    return post;
  }


  //게시글 목록 조회
  public List<Post> listPosts(String token, int page, int size) {

    if (!jwtAuthenticationProvider.validateToken(token)) {
      throw new CustomException(ErrorCode.LOGIN_CHECK_FAIL);
    }
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    return postRepository.findAll(pageable).getContent();
  }

}

