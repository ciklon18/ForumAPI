package com.forum.core.repository;

import com.forum.core.entity.TopicSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TopicSubscriptionRepository extends JpaRepository<TopicSubscription, UUID> {
    void deleteByTopicIdAndUserId(UUID topicId, UUID userId);
    List<TopicSubscription> findAllByTopicId(UUID topicId);
    boolean existsTopicSubscriptionByTopicIdAndUserId(UUID topicId, UUID userId);

    @Query(nativeQuery = true, value = """
            SELECT COUNT(*)
            FROM topic_subscription ts
            WHERE ts.user_id = :userId
            """)
    Integer getSubscriptionsCount(UUID userId);

    @Query(nativeQuery = true, value = """
            SELECT *
            FROM topic_subscription ts
            WHERE ts.user_id = :userId
            ORDER BY ts.created_at DESC
            LIMIT :pageSize OFFSET :offset
            """)
    List<TopicSubscription> getSubscriptions(UUID userId, Integer offset, Integer pageSize);

}
