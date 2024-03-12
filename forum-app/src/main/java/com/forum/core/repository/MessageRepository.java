package com.forum.core.repository;

import com.forum.core.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query(nativeQuery = true, value = """
            SELECT COUNT(*)
            FROM message m
            WHERE m.topic_id = :topicId
            """)
    Integer getMessagesCount(UUID topicId);

    @Query(nativeQuery = true, value = """
            SELECT *
            FROM message m
            WHERE m.topic_id = :topicId
            ORDER BY m.created_at DESC
            LIMIT :pageSize OFFSET :offset
            """)
    List<Message> getMessagesByTopic(UUID topicId, Integer offset, Integer pageSize);

    @Query(nativeQuery = true, value = """
            SELECT m.*
            FROM message m
            JOIN public.topic t on t.id = m.topic_id
            JOIN public.category c on c.id = t.category_id
            WHERE (:text IS NULL OR LOWER(m.text) LIKE CONCAT('%', LOWER(:text), '%'))
            AND (CAST(:fromDate AS TIMESTAMP) IS NULL OR m.created_at >= CAST(:fromDate AS TIMESTAMP))
            AND (CAST(:toDate AS TIMESTAMP) IS NULL OR m.created_at <= CAST(:toDate AS TIMESTAMP))
            AND (:authorId IS NULL OR m.author_id = :authorId)
            AND (:topicId IS NULL OR m.topic_id = :topicId)
            AND (:categoryId IS NULL OR t.category_id = :categoryId)
            """)
    List<Message> getFilteredMessages(
            String text,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            UUID authorId,
            UUID topicId,
            UUID categoryId
    );

    @Query(nativeQuery = true, value = """
            SELECT *
            FROM message m
            WHERE LOWER(m.text) LIKE CONCAT('%', LOWER(:text), '%')
            """)
    List<Message> getMessagesByQuery(String text);
}
