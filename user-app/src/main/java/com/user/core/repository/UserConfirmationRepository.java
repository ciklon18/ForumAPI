package com.user.core.repository;

import com.user.core.entity.UserConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserConfirmationRepository extends JpaRepository<UserConfirmation, UUID> {
    UserConfirmation findByConfirmationCode(UUID confirmationCode);
}
