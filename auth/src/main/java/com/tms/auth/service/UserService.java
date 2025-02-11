package com.tms.auth.service;

import com.tms.auth.dto.RoleUpdateRequestDto;
import com.tms.auth.dto.UserRequestDto;
import com.tms.auth.dto.UserResponseDto;
import com.tms.auth.exception.ErrorCode;
import com.tms.auth.exception.TmsCustomException;
import com.tms.auth.model.DeliveryUser;
import com.tms.auth.model.User;
import com.tms.auth.model.UserRoleEnum;
import com.tms.auth.repository.DeliveryUserRepository;
import com.tms.auth.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final DeliveryUserRepository deliveryUserRepository;


    @Transactional
    public UserResponseDto signup(UserRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        User user = userRepository.save(new User(username, password));
        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateUser(UserRequestDto requestDto, Long userId) {
        User user = findByIdOrElseThrow(userId);

        //인증객체 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = (authentication != null) ? authentication.getName() : "알 수 없음";

        //MASTER 권한도 아니고, 본인이 아닌 경우 에러 발생
        if(!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_MASTER"))){
            if(!authentication.getName().equals(user.getUsername())){
                throw new TmsCustomException(ErrorCode.NOT_PERMITTED);
            }
        }

        String password = passwordEncoder.encode(requestDto.getPassword());
        user.updateUser(requestDto.getUsername(), password);

        //delivery user가 있을 경우 같이 수정
        Optional<DeliveryUser> deliveryUser = deliveryUserRepository.findByUser(user);
        if(deliveryUser.isPresent()){
            deliveryUser.get().updateDeliveryUser(requestDto, password, user);
        }

        return new UserResponseDto(user);
    }

    //role 변경
    @Transactional
    public UserResponseDto updateUserRole(RoleUpdateRequestDto requestDto, Long userId) {
        User user = findByIdOrElseThrow(userId);
        user.setRole(UserRoleEnum.valueOf(requestDto.getRole()));
        return new UserResponseDto(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = findByIdOrElseThrow(userId);

        //인증객체 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = (authentication != null) ? authentication.getName() : "알 수 없음";

        //MASTER 권한도 아니고, 본인이 아닌 경우 에러 발생
        if(!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_MASTER"))){
            if(!authentication.getName().equals(user.getUsername())){
                throw new TmsCustomException(ErrorCode.NOT_PERMITTED);
            }
        }

        user.setDelete(true);
        user.delete(user.getUsername());

        //delivery user가 있을 경우 같이 삭제
        Optional<DeliveryUser> deliveryUser = deliveryUserRepository.findByUser(user);
        if(deliveryUser.isPresent()){
            deliveryUser.get().setDelete(true);
            deliveryUser.get().delete(user.getUsername());
        }
    }

    public UserResponseDto getUser(Long userId) {
        User user = findByIdOrElseThrow(userId);
        return new UserResponseDto(user);
    }


    public List<UserResponseDto> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<UserResponseDto> getAllUsersIncludeDeleted(){
        List<User> deletedUsers = userRepository.findAllUsersIncludeDeleted();
        return deletedUsers.stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }


    public Boolean verifyUser(String username) {
        return userRepository.findByUsername(username).isPresent();
    }


    public User findByIdOrElseThrow(Long userId){
        return userRepository.findById(userId).orElseThrow(
                () -> new TmsCustomException(ErrorCode.NOT_FOUND_USER)
        );
    }
}
