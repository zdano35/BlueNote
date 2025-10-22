package com.BlueNote.demo.repository;

import com.BlueNote.demo.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByTitleContainingIgnoreCase(String title);
    List<Article> findByAuthorContainingIgnoreCase(String author);
    List<Article> findByTags_NameIgnoreCase(String tagName);  // kluczowe dla wyszukiwania po tagu
    @Query("SELECT a FROM Article a JOIN a.tags t WHERE LOWER(t.name) = LOWER(:tag)")
    List<Article> findByTagName(String tag);
}
