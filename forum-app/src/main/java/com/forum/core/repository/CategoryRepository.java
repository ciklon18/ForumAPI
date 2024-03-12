package com.forum.core.repository;

import com.forum.core.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Optional<Category> findByName(String name);

    @Query(nativeQuery = true, value = """
        WITH RECURSIVE category_tree AS (
            SELECT id, name, parent_id, created_at, updated_at, author_id
            FROM category ct
            WHERE parent_id IS NULL
            UNION ALL
            SELECT c.id, c.name, c.parent_id, c.created_at, c.updated_at, c.author_id
            FROM category c
                     JOIN category_tree ct ON c.parent_id = ct.id
        )
        SELECT * FROM category_tree ct1
        ORDER BY  ct1.parent_id, ct1.name
        """
    )
    List<Category> getCategoryHierarchy();

    @Query(nativeQuery = true, value = """
        WITH RECURSIVE category_tree AS (
            SELECT id, name, parent_id, created_at, updated_at, author_id
            FROM category
            WHERE id = :categoryId
            UNION ALL
            SELECT c.id, c.name, c.parent_id, c.created_at, c.updated_at, c.author_id
            FROM category c
                     INNER JOIN category_tree ct ON c.parent_id = ct.id
        )
        SELECT * FROM category_tree ct1
        ORDER BY ct1.name
        """
    )
    List<Category> getCategoriesHierarchyById(UUID categoryId);


    @Query(nativeQuery = true, value = """
        SELECT c.* FROM category c
        LEFT JOIN category c1 ON c1.parent_id = c.id
        WHERE c.id = :categoryId AND (c.parent_id IS NULL OR c1.id IS NOT NULL)
        LIMIT 1
        """
    )
    Category getCategoryIfLastLevel(UUID categoryId);

    @Query(nativeQuery = true, value = """
    SELECT * FROM category c
    WHERE LOWER(c.name) LIKE CONCAT('%', LOWER(:text), '%')
    """
    )
    List<Category> getCategoriesByQuery(String text);
}
