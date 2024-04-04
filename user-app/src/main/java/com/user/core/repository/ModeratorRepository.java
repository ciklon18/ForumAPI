package com.user.core.repository;

import com.user.core.entity.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ModeratorRepository extends JpaRepository<Moderator, UUID> {
    @Query("DELETE FROM Moderator m WHERE m.userId = :userId")
    void deleteAllByUserId(UUID userId);
}

