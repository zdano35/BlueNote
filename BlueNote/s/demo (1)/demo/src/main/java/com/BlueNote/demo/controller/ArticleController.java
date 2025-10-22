package com.BlueNote.demo.controller;

import com.BlueNote.demo.model.Article;
import com.BlueNote.demo.model.Tag;
import com.BlueNote.demo.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
                request.getTags()
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
            existing.setTags(articleService.convertTagNamesToTags(request.getTags()));
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

    // DTO – obiekt przyjmowany z frontu
    public static class ArticleRequest {
        private String title;
        private String description;
        private String author;
        private List<String> tags;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }

        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
    }
}
