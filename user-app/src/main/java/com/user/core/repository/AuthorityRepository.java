package com.user.core.repository;


import com.user.core.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, UUID> {

    @Query("SELECT ua FROM Authority ua WHERE ua.user.id = :userId")
    List<Authority> findAllByUserId(UUID userId);

    @Query("DELETE FROM Authority ua WHERE ua.user.id = :userId AND ua.role = 'ADMIN'")
    void deleteAllByUserId(UUID userId);
}
