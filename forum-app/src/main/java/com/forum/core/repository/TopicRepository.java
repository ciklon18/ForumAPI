package com.forum.core.repository;


import com.forum.core.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TopicRepository extends JpaRepository<Topic, UUID>{
    Optional<Topic> findByName(String name);

    void deleteById(UUID topicId);


    @Query(nativeQuery = true, value = """
        SELECT *
        FROM topic t
        ORDER BY t.created_at
        LIMIT :pageSize OFFSET :offset
        """
    )
    List<Topic> getTopics(Integer offset, Integer pageSize);

    @Query(nativeQuery = true, value = """
        SELECT COUNT(*) FROM topic t
        """
    )
    Integer getTopicsCount();

    @Query(nativeQuery = true, value = """
        SELECT *
        FROM topic t
        WHERE LOWER(t.name) LIKE CONCAT('%', LOWER(:query), '%')
        """
    )
    List<Topic> getTopicsByQuery(String query);
}
