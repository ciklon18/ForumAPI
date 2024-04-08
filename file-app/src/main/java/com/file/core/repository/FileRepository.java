package com.file.core.repository;

import com.file.core.entity.File;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<File, UUID> {

    @NotNull
    Optional<File> findById(@NotNull UUID id);

    void deleteByMessageId(UUID messageId);

    @Query("SELECT f.id FROM File f WHERE f.messageId = :messageId")
    Optional<UUID> getFileIdByMessageId(UUID messageId);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN TRUE ELSE FALSE END FROM File f WHERE f.messageId = :messageId")
    boolean existsByMessageId(UUID messageId);
}
