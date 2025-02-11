package com.tms.auth.service;

import com.tms.auth.dto.UserRequestDto;
import com.tms.auth.dto.UserResponseDto;
import com.tms.auth.exception.ErrorCode;
import com.tms.auth.exception.TmsCustomException;
import com.tms.auth.model.DeliveryUser;
import com.tms.auth.model.User;
import com.tms.auth.repository.DeliveryUserRepository;
import com.tms.auth.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class DeliveryUserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final DeliveryUserRepository deliveryUserRepository;
    private final UserService userService;


    //배송 담당자 테이블은 deliveryType 필수, deliveryType = INTRA일 경우 hubId 필수값 (권한과는 관련 없는 변수임)
    @Transactional
    public UserResponseDto signup(@Valid UserRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        if(requestDto.getDeliveryType() == null){
            throw new TmsCustomException(ErrorCode.DELIVERYTYPE_CANNOT_BE_NULL);
        }

        if(requestDto.getDeliveryType().equals("INTRA") && requestDto.getHubId() == null){
            throw new TmsCustomException(ErrorCode.HUBID_CANNOT_BE_NULL);
        }

        User user = userRepository.save(new User(username, password));
        deliveryUserRepository.save(new DeliveryUser(requestDto, user));

        return new UserResponseDto(user);
    }
}
