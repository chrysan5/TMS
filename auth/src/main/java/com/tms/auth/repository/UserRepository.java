package com.tms.auth.repository;

import com.tms.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT * FROM p_users WHERE is_delete = true", nativeQuery = true)
    List<User> findAllUsersIncludeDeleted();
}
