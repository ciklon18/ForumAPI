package com.forum.core.repository;

import com.forum.core.entity.TopicSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TopicSubscriptionRepository extends JpaRepository<TopicSubscription, UUID> {
    void deleteByTopicIdAndUserId(UUID topicId, UUID userId);
}
