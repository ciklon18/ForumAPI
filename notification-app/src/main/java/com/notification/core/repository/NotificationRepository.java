package com.notification.core.repository;

import com.notification.core.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {

    @Query(nativeQuery = true, value = """
            SELECT COUNT(n.*)
            FROM notification n
            WHERE n.user_id = :userId
              AND (
                  (:query IS NULL)
                    OR (LOWER(n.header) LIKE CONCAT('%', LOWER(:query), '%'))
                    OR (LOWER(n.text) LIKE CONCAT('%', LOWER(:query), '%'))
                  )
            """)
    Integer getNotificationsCount(
            @Param("userId") UUID userId, @Param("query") String query
    );

    @Query(nativeQuery = true, value = """
            SELECT n.*
            FROM notification n
            WHERE n.user_id = :userId
              AND (
                  (:query IS NULL)
                    OR (LOWER(n.header) LIKE CONCAT('%', LOWER(:query), '%'))
                    OR (LOWER(n.text) LIKE CONCAT('%', LOWER(:query), '%'))
                  )
            LIMIT :pageSize OFFSET :offset
            """)
    List<NotificationEntity> getNotifications(
            @Param("userId") UUID userId,
            @Param("query") String query,
            @Param("pageSize") Integer pageSize,
            @Param("offset") Integer offset
    );

    @Query(nativeQuery = true, value = """
            SELECT COUNT(n.*)
            FROM notification n
            WHERE n.user_id = :userId
              AND n.notification_status = 'NEW'
            """)
    Integer getUnreadNotificationsCount(@Param("userId") UUID userId);

    @Modifying
    @Query(nativeQuery = true, value = """
        UPDATE notification n
        SET notification_status = CASE
                WHEN n.notification_status = 'NEW' THEN :status
                ELSE n.notification_status
            END,
            updated_at = CASE
                WHEN n.notification_status = 'NEW' THEN NOW()
                ELSE n.updated_at
            END
        WHERE n.id IN (:notifications)
        """)
    void updateNotificationsStatus(
            @Param("notifications") List<UUID> notifications,
            @Param("status") String status
    );
}
