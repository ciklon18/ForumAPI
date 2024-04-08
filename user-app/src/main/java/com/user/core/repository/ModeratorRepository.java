package com.user.core.repository;

import com.user.core.entity.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.UUID;

public interface ModeratorRepository extends JpaRepository<Moderator, UUID> {
    @Query("DELETE FROM Moderator m WHERE m.userId = :userId")
    void deleteAllByUserId(UUID userId);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END FROM Moderator m WHERE m.userId = :userId")
    boolean existsByUserId(UUID userId);

    @Query("SELECT m.id FROM Moderator m WHERE m.userId = :id")
    Collection<UUID> findAllByUserId(UUID id);
}

