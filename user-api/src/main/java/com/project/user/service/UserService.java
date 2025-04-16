package com.project.user.service;

import com.project.common.domain.entity.User;
import com.project.common.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public Optional<User> findByIdAndEmail(Long id, String email) {
    return userRepository.findById(id)
        .stream().filter(user -> user.getEmail().equals(email))
        .findFirst();
  }

  public Optional<User> findValidUser(String email, String password) {
    return userRepository.findByEmail(email)
        .stream()
        .filter(
            user -> user.getPassword().equals(password) && user.isEmailVerified()
        )
        .findFirst();
  }

}
