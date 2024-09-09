package com.tms.tms.repository;


import com.tms.tms.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, Long> {

  //Page<Store> findAllByOrderByCreatedAtAscUpdatedAtAsc(Pageable pageable);
  //List<Store> findAllByStoreNameContaining(String keyword);
}
