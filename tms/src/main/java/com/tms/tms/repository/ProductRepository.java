package com.tms.tms.repository;


import com.tms.tms.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ProductRepository extends JpaRepository<Product, Long> {
  //List<Product> findAllByStore(Store store);
  //Page<Product> findAllByOrderByCreatedAtAscUpdatedAtAsc(Pageable pageable);
  //List<Product> findAllByProductNameContaining(String keyword);
}
