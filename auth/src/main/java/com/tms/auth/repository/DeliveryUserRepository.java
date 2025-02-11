package com.tms.auth.repository;

import com.tms.auth.model.DeliveryUser;
import com.tms.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DeliveryUserRepository extends JpaRepository<DeliveryUser, Long> {
    Optional<DeliveryUser> findByUser(User user);
}