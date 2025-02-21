package com.tms.tms.repository;

import com.tms.tms.model.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
}
