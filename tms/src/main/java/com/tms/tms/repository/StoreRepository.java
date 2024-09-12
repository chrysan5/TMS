package com.tms.tms.repository;


import com.tms.tms.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findAllByStoreNameContaining(String keyword);
}
