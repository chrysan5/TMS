package com.tms.auth.repository;

import com.tms.auth.model.DeliveryUser;
import com.tms.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeliveryUserRepository extends JpaRepository<DeliveryUser, Long> {
    Optional<DeliveryUser> findByUser(User user);

    @Query(value = "SELECT du FROM p_delivery_users du WHERE du.user_id = :userId", nativeQuery = true)
    Optional<DeliveryUser> findByUserIgnoreIsDelete(@Param("userId") Long userId);
}