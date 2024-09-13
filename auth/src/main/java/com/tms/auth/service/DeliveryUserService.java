package com.tms.auth.service;

import com.tms.auth.dto.DeliveryUserResponseDto;
import com.tms.auth.exception.ErrorCode;
import com.tms.auth.exception.TmsCustomException;
import com.tms.auth.model.DeliveryUser;
import com.tms.auth.model.User;
import com.tms.auth.repository.DeliveryUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class DeliveryUserService {
    private final DeliveryUserRepository deliveryUserRepository;
    private final UserService userService;



    //자신의 정보만 확인 가능
    public DeliveryUserResponseDto getDeliveryUser(Long userId) {
        User user = userService.findByIdOrElseThrow(userId);
        DeliveryUser deliveryUser = deliveryUserRepository.findByUser(user).orElseThrow(
                () -> new TmsCustomException(ErrorCode.NOT_FOUND_DELIVERY_USER)
        );

        return new DeliveryUserResponseDto(deliveryUser);
    }
}
