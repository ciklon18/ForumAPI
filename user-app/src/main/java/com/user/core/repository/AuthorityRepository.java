package com.user.core.repository;


import com.user.core.entity.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuthorityRepository extends JpaRepository<UserAuthority, UUID> {

    @Query("SELECT ua FROM UserAuthority ua WHERE ua.user.id = :userId")
    List<UserAuthority> findAllByUserId(UUID userId);
}
