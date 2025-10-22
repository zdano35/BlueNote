package com.BlueNote.demo.service;

import com.BlueNote.demo.model.Article;
import com.BlueNote.demo.model.Tag;
import com.BlueNote.demo.repository.ArticleRepository;
import com.BlueNote.demo.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;

    public ArticleService(ArticleRepository articleRepository, TagRepository tagRepository) {
        this.articleRepository = articleRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional
    public Article createArticle(String title, String description, String author, List<String> tagNames) {
        if (description.split("\\s+").length > 500) {
            throw new IllegalArgumentException("Opis nie może mieć więcej niż 500 słów");
        }

        Article article = new Article();
        article.setTitle(title);
        article.setDescription(description);
        article.setAuthor(author);

        Set<Tag> tags = convertTagNamesToTags(tagNames);
        article.setTags(tags);

        return articleRepository.save(article);
    }

    public List<Article> searchByTitle(String title) {
        return articleRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Article> searchByAuthor(String author) {
        return articleRepository.findByAuthorContainingIgnoreCase(author);
    }

    public List<Article> searchByTag(String tag) {
        return articleRepository.findByTagName(tag.toLowerCase());
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    @Transactional
    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    @Transactional
    public void deleteArticleById(Long id) {
        articleRepository.deleteById(id);
    }

    @Transactional
    public Set<Tag> convertTagNamesToTags(List<String> tagNames) {
        return tagNames.stream()
                .map(tagName -> tagRepository.findByNameIgnoreCase(tagName)
                        .orElseGet(() -> {
                            Tag newTag = new Tag();
                            newTag.setName(tagName);
                            return tagRepository.save(newTag);
                        }))
                .collect(Collectors.toSet());
    }
}
