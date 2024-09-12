package com.tms.auth.service;

import com.tms.auth.dto.UserRequestDto;
import com.tms.auth.dto.UserResponseDto;
import com.tms.auth.exception.ErrorCode;
import com.tms.auth.exception.TmsCustomException;
import com.tms.auth.model.User;
import com.tms.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponseDto signup(UserRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        User user = userRepository.save(new User(username, password));
        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateUser(UserRequestDto requestDto, Long userId) {
        String password = passwordEncoder.encode(requestDto.getPassword());

        User user = findByIdOrElseThrow(userId);
        user.updateUser(requestDto, password);
        return new UserResponseDto(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = findByIdOrElseThrow(userId);
        user.setDelete(true);
        user.delete(user.getUsername());
    }

    public UserResponseDto getUser(Long userId) {
        User user = findByIdOrElseThrow(userId);
        return new UserResponseDto(user);
    }



    public User findByIdOrElseThrow(Long userId){
        return userRepository.findById(userId).orElseThrow(
                () -> new TmsCustomException(ErrorCode.NOT_FOUND_USER)
        );
    }

    public Boolean verifyUser(String username) {
        return userRepository.findByUsername(username).isPresent();
    }


}
