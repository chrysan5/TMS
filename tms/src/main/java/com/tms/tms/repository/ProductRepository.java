package com.tms.tms.repository;


import com.tms.tms.model.Product;
import com.tms.tms.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByStoreAndProductNameContaining(Store store, String productName);

    List<Product> findAllByStore(Store store);

}
