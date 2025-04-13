package com.project.user.config.filter;



import com.project.common.UserVo;
import com.project.common.config.JwtAuthenticationProvider;
import com.project.user.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@WebFilter(urlPatterns = "/user/*") //ServletContainer 기반 -> @ServletComponentScan 필요
@RequiredArgsConstructor
public class UserFilter implements Filter {

  private final JwtAuthenticationProvider jwtAuthenticationProvider;
  private final UserService userService;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
    HttpServletRequest req = (HttpServletRequest) request;
    String token = req.getHeader("X-AUTH-TOKEN");
    if (!jwtAuthenticationProvider.validateToken(token)) {
      throw new ServletException("Invalid Access");
    }
    UserVo vo = jwtAuthenticationProvider.getUserVo(token);
    userService.findByIdAndEmail(vo.getId(), vo.getEmail())
        .orElseThrow(() -> new ServletException("Invalid Access"));
    chain.doFilter(request,response);
  }
}
