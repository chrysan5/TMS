package com.tms.tms.repository;

import com.tms.tms.model.Delivery;
import com.tms.tms.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findByOrder(Order order);

    List<Delivery> findAllByOrderIn(List<Order> orders);
}
