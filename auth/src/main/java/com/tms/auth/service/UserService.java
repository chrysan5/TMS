package com.tms.auth.service;

import com.tms.auth.dto.UserRequestDto;
import com.tms.auth.model.User;
import com.tms.auth.model.UserRoleEnum;
import com.tms.auth.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {
    //private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    @Transactional
    public void signup(UserRequestDto requestDto) {
        String username = requestDto.getUsername();
        //String password = passwordEncoder.encode(requestDto.getPassword());
        String password = requestDto.getPassword();


        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        User user = new User(username, password);
        userRepository.save(user);
    }
}
