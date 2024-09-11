package com.tms.tms.repository;

import com.tms.tms.model.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HubRepository extends JpaRepository<Hub, Long> {
    Page<Hub> findAll(Pageable pageable);

    List<Hub> findAllByHubRegionContaining(String keyword);

}
