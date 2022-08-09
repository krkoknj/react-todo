package com.bitcamp.todo.service;

import com.bitcamp.todo.model.UserEntity;
import com.bitcamp.todo.model.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 아이디(이메일) 유효성 및 중복검사
    public UserEntity create(final UserEntity userEntity) {
        // 1. userEntity 유효성 체크
        if (userEntity == null || userEntity.getUsername() == null) {
            throw new RuntimeException("Invalid arguments");
        }
        final String username = userEntity.getUsername();
        
        // 2. 중복 검사
        if (userRepository.existsByUsername(username)){
            log.warn("Username already exists {}", username);
            throw new RuntimeException("Username already exists");
        }

        return userRepository.save(userEntity);
    }

    // 아이디 & 비밀번호 일치 확인
    public UserEntity getByCredentials(final String username,
                                       final String password,
                                       final PasswordEncoder encoder) {
        final UserEntity originalUser = userRepository.findByUsername(username);

        // matches 메서드를 이용해 패스워드가 같은지 확인
        if (originalUser != null && encoder.matches(password, originalUser.getPassword())){
            return originalUser;
        }

        return null;
    }

}
