package com.tms.auth.repository;

import com.tms.auth.model.DeliveryUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryUserRepository extends JpaRepository<DeliveryUser, Long> {
}
