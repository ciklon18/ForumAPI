package com.user.core.repository;

import com.user.core.entity.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ModeratorRepository extends JpaRepository<Moderator, UUID> {
    @Query("SELECT m FROM moderator m WHERE m.user.id = :userId")
    List<Moderator> findAllByUserId(UUID userId);
    @Query("SELECT m FROM moderator m WHERE m.category.id = :categoryId")
    List<Moderator> findAllByCategoryId(UUID categoryId);

    @Query("DELETE FROM moderator m WHERE m.user.id = :userId")
    void deleteAllByUserId(UUID userId);
}

