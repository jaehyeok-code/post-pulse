package com.project.post.service;

import com.project.common.domain.entity.User;
import com.project.common.domain.repository.UserRepository;
import com.project.common.service.S3Service;
import com.project.common.domain.entity.Post;
import com.project.common.domain.repository.PostRepository;
import com.project.common.domain.dto.PostRequest;
import com.project.post.search.PostSearchService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.project.common.UserVo;
import com.project.common.config.JwtAuthenticationProvider;
import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final JwtAuthenticationProvider jwtAuthenticationProvider;
  private final S3Service s3Service;
  private final UserRepository userRepository;
  private final PostSearchService postSearchService; // -> (ElasticSearch)

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

    Post createdPost = postRepository.save(post);
    postSearchService.indexPost(createdPost);
    return createdPost;
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
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

    post.setViews(post.getViews() == null ? 1 : post.getViews() + 1);
    postRepository.save(post);
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

