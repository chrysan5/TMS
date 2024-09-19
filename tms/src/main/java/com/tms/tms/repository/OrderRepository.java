package com.tms.tms.repository;

import com.tms.tms.model.OrderState;
import com.tms.tms.model.Store;
import com.tms.tms.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByStore(Store store);

    @Query("select o from Order o where o.state = 'ORDERED'")
    List<Order> findAllByOrderState();
}
