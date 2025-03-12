package com.tms.tms.repository;

import com.tms.tms.model.Cart;
import com.tms.tms.model.CartProduct;
import com.tms.tms.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
    Optional<CartProduct> findByCartAndProduct(Cart cart, Product product);

    List<CartProduct> findByCartAndProductIn(Cart cart, List<Product> products);

    boolean existsByCart(Cart cart);

    List<CartProduct> findAllByCart(Cart cart);

    @Query("SELECT cp FROM CartProduct cp JOIN FETCH cp.product WHERE cp.cart = :cart")
    Page<CartProduct> findAllByCart(Cart cart, Pageable pageable);
}
