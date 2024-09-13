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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

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
        String password = passwordEncoder.encode(requestDto.getPassword());
        user.updateUser(requestDto.getUsername(), password);

        //delivery user가 있을 경우 같이 변경
        Optional<DeliveryUser> deliveryUser = deliveryUserRepository.findByUser(user);
        if(deliveryUser.isPresent()){
            deliveryUser.get().updateDeliveryUser(requestDto.getUsername(), password);
        }

        return new UserResponseDto(user);
    }

    //role 변경
    @Transactional
    public UserResponseDto updateUserRole(RoleUpdateRequestDto requestDto, Long userId) {
        User user = findByIdOrElseThrow(userId);

        //다른 권한이 hub 권한으로 바뀔 경우 삭제처리된 경우 활성화, 없을 경우 delivery_user 생성
        if(requestDto.getRole().equals("HUB")) {
            Optional<DeliveryUser> deliveryUser = deliveryUserRepository.findByUserIgnoreIsDelete(userId);
            if(deliveryUser.isPresent()) {
                deliveryUser.get().setDelete(false);
                deliveryUser.get().activate();
            }else{
                deliveryUserRepository.save(new DeliveryUser(user, requestDto.getHubId()));
            }
        }
        //기존 hub 권한이 다른 권한으로 바꿜 경우 delivery_user을 삭제 처리
        if(!requestDto.getRole().equals("HUB") && String.valueOf(user.getRole()).equals("HUB")){
            DeliveryUser deliveryUser = deliveryUserRepository.findByUser(user).orElseThrow(
                    () -> new TmsCustomException(ErrorCode.NOT_FOUND_DELIVERY_USER)
            );
            deliveryUser.setDelete(true);
            deliveryUser.delete(user.getUsername());
        }

        user.setRole(UserRoleEnum.valueOf(requestDto.getRole()));
        return new UserResponseDto(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = findByIdOrElseThrow(userId);
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


    public Boolean verifyUser(String username) {
        return userRepository.findByUsername(username).isPresent();
    }


    public User findByIdOrElseThrow(Long userId){
        return userRepository.findById(userId).orElseThrow(
                () -> new TmsCustomException(ErrorCode.NOT_FOUND_USER)
        );
    }
}
