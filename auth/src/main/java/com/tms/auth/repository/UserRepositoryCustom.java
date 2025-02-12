package com.tms.auth.repository;

import com.tms.auth.dto.UserRequestDto;
import com.tms.auth.dto.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<UserResponseDto> searchUsers(UserRequestDto userRequestDto, Pageable pageable);
}
