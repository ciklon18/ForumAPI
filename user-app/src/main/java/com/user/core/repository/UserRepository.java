package com.user.core.repository;

import com.user.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.login = :login OR u.email = " +
            ":login")
    boolean isProfileExistByLogin(@Param("login") String login);

    @Query("SELECT u FROM User u WHERE u.login = :login OR u.email = :login")
    List<User> findUsersAmongLoginAndEmailByLogin(String login);

    @Query("""
            SELECT CASE
                WHEN COUNT(u) > 0
                THEN true ELSE false
                END
            FROM User u
            WHERE (u.login = :login OR :login IS NULL)
                AND (u.email = :email OR :email IS NULL)
            """)
    boolean isProfileExistByLoginAndEmail(String login, String email);

    @Query("""
            SElECT u.email
            FROM User u
            WHERE u.id IN (:userIds)
            """)
    List<String> getUserEmails(@Param("userIds") List<UUID> userIds);
}
