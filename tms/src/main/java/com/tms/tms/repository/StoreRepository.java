package com.tms.tms.repository;


import com.tms.tms.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findAllByStoreNameContaining(String keyword);

    Optional<Store> findByUsername(String username);
}
