package com.project.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
  ALREADY_REGISTER_USER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
  ALREADY_REGISTER_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임 입니다."),
  NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "일치하는 회원이 없습니다."),
  ALREADY_VERIFY(HttpStatus.BAD_REQUEST, "이미 인증이 완료되었습니다."),
  EXPIRE_CODE(HttpStatus.BAD_REQUEST, "인증시간이 만료되었습니다."),
  WRONG_VERIFICATION(HttpStatus.BAD_REQUEST, "잘못된 인증 시도 입니다."),
  FILE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, "파일 업로드에 실패하였습니다."),

  // login
  LOGIN_CHECK_FAIL(HttpStatus.BAD_REQUEST, "아이디나 패스워드를 확인해 주세요."),

  NOT_FOUND_POST(HttpStatus.BAD_REQUEST, "게시글을 찾을 수 없습니다."),
  UNAUTHORIZED_UPDATE(HttpStatus.BAD_REQUEST, "게시글의 수정 권한이 없습니다."),
  UNAUTHORIZED_DELETE(HttpStatus.BAD_REQUEST, "게시글을 삭제 권한이 없습니다."),

  NOT_FOUND_NOTIFICATION(HttpStatus.BAD_REQUEST,"알림을 찾을 수 없습니다."),
  UNAUTHORIZED_ACCESS(HttpStatus.BAD_REQUEST,"접근 권한이 없습니다."),
  INVALID_TOKEN(HttpStatus.BAD_REQUEST,"유효하지 않은 토큰입니다."),

  ALREADY_LIKED(HttpStatus.BAD_REQUEST, "이미 좋아요를 누른 게시글입니다."),
  NOT_FOUND_LIKE(HttpStatus.BAD_REQUEST, "좋아요 내역을 찾을 수 없습니다.");


  private final HttpStatus httpStatus;
  private final String detail;
}
