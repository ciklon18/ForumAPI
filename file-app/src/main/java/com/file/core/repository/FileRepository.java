package com.file.core.repository;

import com.file.core.entity.File;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends JpaRepository<File, UUID> {

    @NotNull
    Optional<File> findById(@NotNull UUID id);

    void deleteById(@NotNull UUID id);
}
