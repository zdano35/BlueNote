package com.BlueNote.demo.controller;

import com.BlueNote.demo.model.Article;
import com.BlueNote.demo.model.Tag;
import com.BlueNote.demo.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody ArticleRequest request) {
        Article article = articleService.createArticle(
                request.getTitle(),
                request.getDescription(),
                request.getAuthor(),
                request.getCreatedAt(),
                request.getTagDTOs()
        );
        return ResponseEntity.ok(article);
    }

    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles() {
        return ResponseEntity.ok(articleService.getAllArticles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(
            @PathVariable Long id,
            @RequestBody ArticleRequest request
    ) {
        return articleService.getArticleById(id).map(existing -> {
            existing.setTitle(request.getTitle());
            existing.setDescription(request.getDescription());
            existing.setAuthor(request.getAuthor());
            existing.setTags(articleService.convertTagDTOsToTags(request.getTagDTOs()));
            if (request.getCreatedAt() != null) {
                existing.setCreatedAt(request.getCreatedAt());
            }
            Article updated = articleService.saveArticle(existing);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticleById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Article>> searchArticles(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String tag
    ) {
        List<Article> result;

        if (title != null) {
            result = articleService.searchByTitle(title);
        } else if (author != null) {
            result = articleService.searchByAuthor(author);
        } else if (tag != null) {
            result = articleService.searchByTag(tag);
        } else {
            result = articleService.getAllArticles();
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/tags")
    public ResponseEntity<List<Tag>> getAllTags() {
        return ResponseEntity.ok(articleService.getAllTags());
    }

    // ==============================
    // DTOs
    // ==============================

    public static class ArticleRequest {
        private String title;
        private String description;
        private String author;
        private LocalDateTime createdAt;
        private List<TagDTO> tagDTOs;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }

        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

        public List<TagDTO> getTagDTOs() { return tagDTOs; }
        public void setTagDTOs(List<TagDTO> tagDTOs) { this.tagDTOs = tagDTOs; }
    }

    public static class TagDTO {
        private String name;
        private String color;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
    }
}
