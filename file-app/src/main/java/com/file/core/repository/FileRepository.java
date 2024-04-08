package com.file.core.repository;

import com.file.core.entity.File;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<File, UUID> {

    @NotNull
    Optional<File> findById(@NotNull UUID id);

    void deleteById(@NotNull UUID id);
}
