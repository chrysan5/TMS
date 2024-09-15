package com.tms.tms.repository;

import com.tms.tms.model.Store;
import com.tms.tms.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByStore(Store store);
}
